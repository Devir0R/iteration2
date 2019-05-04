using System;
using System.Collections.Concurrent;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Web;
using HtmlAgilityPack;
using Newtonsoft.Json.Linq;
namespace _365Players.Fetcher
{
    public class DataFetcher : IDataFetcher
    {
        protected const string ProxyPath = "http://btprox365.appspot.com/?url={0}";

        private ConcurrentDictionary<string, DomainRequestAssistant> _requestResetEvents;
        private const int NumberOfSequenctialRequests = 100;
        private const int MaxGoodGapBetweenCalls = 500;
        private const int MinGoodGapBetweenCalls = 200;

        private bool _bypassCache;
        private bool _noCacheUri;

        public const string ScannerExceptionPolicy = "Scanners";

        public static TimeSpan TimeOffset { get; set; }

        public TimeSpan Timeout { get; set; }

        public DataFetcher(bool bypassCache, bool noCacheUri) : this(bypassCache, noCacheUri, TimeSpan.FromSeconds(15))
        {

        }


        public DataFetcher(bool bypassCache, bool noCacheUri, TimeSpan timeout)
        {
            _bypassCache = bypassCache;
            _noCacheUri = noCacheUri;

            _requestResetEvents = new ConcurrentDictionary<string, DomainRequestAssistant>();

            Timeout = timeout;
        }

        public async Task<FetchResult> FetchDom(string url, bool isJsonContainer = false, bool returnNull = false)
        {
            Uri uri;
            if (Uri.TryCreate(url, UriKind.RelativeOrAbsolute, out uri))
            {
                return await FetchDom(uri, isJsonContainer, returnNull);
            }

            var fetchResult = new FetchResult();

            if (!returnNull)
            {
                fetchResult.HtmlDocument = new HtmlDocument();
            }

            return fetchResult;
        }

        public void Close(string url)
        {
            // Nothing to close
        }

        public async Task<FetchResult> FetchDom(Uri uri, bool isJsonContainer = false, bool returnNull = false)
        {
            var fetchResult = new FetchResult();

            var domainRequestAssistant = _requestResetEvents.GetOrAdd(uri.Host, s => new DomainRequestAssistant());

            var timeBeforeWait = DateTime.Now;
            Console.WriteLine("Waiting before call");
            domainRequestAssistant.ResetEvent.WaitOne(2000);
            Console.WriteLine("Waited '{0}' before call", (DateTime.Now - timeBeforeWait).TotalMilliseconds);

            if (domainRequestAssistant.FrequentTotalRequestCounter > 0 &&
                domainRequestAssistant.FrequentTotalRequestCounter % NumberOfSequenctialRequests == 0)
            {
                Console.WriteLine("Reseting the domain request assistant");
                domainRequestAssistant.ResetEvent.Reset();

                Console.WriteLine("Delaying request... ({0})", uri.OriginalString);
                await Task.Delay(1000);

                Console.WriteLine("Setting the domain request assistant");
                domainRequestAssistant.ResetEvent.Set();
            }

            _requestResetEvents.AddOrUpdate(uri.Host, s => new DomainRequestAssistant(), (s, assistant) =>
            {
                if ((DateTime.UtcNow - domainRequestAssistant.UpdatedTime).TotalMilliseconds <
                    MaxGoodGapBetweenCalls)
                {
                    assistant.FrequentTotalRequestCounter++;
                }
                else
                {
                    assistant.FrequentTotalRequestCounter = 0;
                }

                assistant.UpdatedTime = DateTime.UtcNow;
                assistant.TotalRequestCounter++;

                return assistant;
            });

            try
            {
                if (isJsonContainer)
                {
                    fetchResult = await FetchJsonData(uri);
                }
                else
                {
                    fetchResult = await FetchData(uri);
                }

                Console.WriteLine("Handling result from: {0}", uri.PathAndQuery);
                if (!string.IsNullOrWhiteSpace(fetchResult.RawPage))
                {
                    fetchResult.HtmlDocument = BuildDom(fetchResult.RawPage, returnNull);
                }
                else
                {
                    fetchResult.HtmlDocument = null;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
            }

            if (!returnNull && fetchResult.HtmlDocument == null)
            {
                fetchResult.HtmlDocument = new HtmlDocument();
            }

            return fetchResult;
        }

        private async Task<FetchResult> FetchData(Uri uri)
        {
            var originalUri = uri;
            var fetchResult = new FetchResult();
            var tryCount = 3;
            var isSuccess = false;
            var failed = false;
            var rndTime = new Random((int)DateTime.Now.Ticks & 0x0000FFFF);

            while (tryCount > 0 && !isSuccess)
            {
                tryCount--;
                try
                {
                    if (failed)
                    {
                        failed = false;
                        fetchResult.UsedProxy = true;
                        uri = new Uri(string.Format(ProxyPath, System.Web.HttpUtility.UrlEncode(originalUri.OriginalString)));

                        var domainRequestAssistant = _requestResetEvents.GetOrAdd(originalUri.Host,
                            s => new DomainRequestAssistant());

                        Console.WriteLine("Reseting the domain request assistant");
                        domainRequestAssistant.ResetEvent.Reset();

                        Console.WriteLine("Delaying request (after error)... ({0})", originalUri);
                        await Task.Delay(rndTime.Next(MinGoodGapBetweenCalls, MaxGoodGapBetweenCalls));

                        Console.WriteLine("Setting the domain request assistant");
                        domainRequestAssistant.ResetEvent.Set();
                    }

                    var httpClient = _bypassCache ? new HttpClient(new BypassCacheHttpRequestHandler(_noCacheUri), true) : new HttpClient();

                    httpClient.Timeout = this.Timeout;

                    Console.WriteLine("Requesting: {0}", uri.PathAndQuery);
                    fetchResult.RawPage = await httpClient.GetStringAsync(uri);

                    isSuccess = true;
                }
                catch (ArgumentNullException aex)
                {
                    Console.WriteLine(aex);
                }
                catch (WebException wex)
                {
                    var res = (HttpWebResponse)wex.Response;

                    Console.WriteLine(wex);

                    int statusCode;
                    int.TryParse(res.StatusCode.ToString(), out statusCode);

                    if (res != null && statusCode == 429 || statusCode == 503 || statusCode == 500)
                    {
                        failed = true;
                    }
                }
                catch (HttpRequestException exception)
                {
                    Console.WriteLine(exception);

                    if (exception.Message.Contains("429") || exception.Message.Contains("503") || exception.Message.Contains("500"))
                    {
                        failed = true;
                    }
                }
                catch (TaskCanceledException exception)
                {
                    Console.WriteLine(exception);

                    failed = true;
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex);
                }
            }

            if (tryCount == 0 && !isSuccess)
            {
                var msg = string.Format("Failed all attempts to get url: {0}", originalUri.OriginalString);
                Console.WriteLine(msg);
            }

            return fetchResult;
        }

        private HtmlDocument BuildDom(string strData, bool returnNull = false)
        {
            var doc = new HtmlDocument();

            try
            {
                doc.LoadHtml(strData);
            }
            catch (Exception exception)
            {
                doc = null;

                Console.WriteLine(exception);
            }

            return !returnNull && doc == null ? new HtmlDocument() : doc;
        }

        private async Task<FetchResult> FetchJsonData(Uri url)
        {
            var answer = string.Empty;

            var fetchResult = new FetchResult();

            try
            {
                fetchResult = await FetchData(url);

                if (!string.IsNullOrWhiteSpace(fetchResult.RawPage))
                {
                    var json = JObject.Parse(fetchResult.RawPage);
                    answer = json["commands"].First["parameters"]["content"].Value<string>();
                    var timestamp = DateTimeOffset.Parse(json["timestamp"].Value<string>());
                    TimeOffset = timestamp.Offset;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
            }

            fetchResult.RawPage = answer;

            return fetchResult;
        }

        public void Dispose()
        {
            // TODO: Add Dispose logic
        }
    }
}
