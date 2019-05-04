using System;
using System.Net.Cache;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;

namespace _365Players.Fetcher
{
    public class BypassCacheHttpRequestHandler : WebRequestHandler
    {
        public bool NoCacheUri { get; set; }

        public BypassCacheHttpRequestHandler()
        {
            CachePolicy = new HttpRequestCachePolicy(HttpRequestCacheLevel.NoCacheNoStore);
        }

        public BypassCacheHttpRequestHandler(bool noCacheUri)
        {
            CachePolicy = new HttpRequestCachePolicy(HttpRequestCacheLevel.NoCacheNoStore);

            NoCacheUri = noCacheUri;
        }

        protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            if (NoCacheUri)
            {
                var newPath = request.RequestUri.OriginalString;
                var noCache = string.Format("nocache={0}", Guid.NewGuid());

                if (newPath.EndsWith("&"))
                {
                    newPath += noCache;
                }
                else if (newPath.EndsWith("/"))
                {
                    newPath += string.Format("?{0}", noCache);
                }
                else
                {
                    newPath += string.Format("&{0}", noCache);
                }

                request.RequestUri = new Uri(newPath);
            }

            return base.SendAsync(request, cancellationToken);
        }
    }
}
