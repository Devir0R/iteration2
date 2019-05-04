using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text.RegularExpressions;
using System.Threading;
using System.Threading.Tasks;
using System.Timers;
using _365Players.Diagnostic;
using _365Players.Enums;
using _365Players.Fetcher;
using _365Players.Parsers;
using _365Players.Scanners.Models;
using _365Players.Updates;
using HtmlAgilityPack;
using static _365Players.Enums.PlayerEnums;
using Timer = System.Timers.Timer;
using System.Net.Http;
using System.Web.Script.Serialization;
using System.Text;

namespace _365Players.Scanners
{
    public class SoccerWayScanner : Scanner
    {
        #region Consts

        public const string DataSourceName = "SoccerWayScanner";
        public const string ScannerName = "SoccerWayScanner";

        #endregion

        #region Members

        private volatile bool _isRunning;

        private ConcurrentDictionary<string, CompetitorData> _competitors;
        private ConcurrentDictionary<string, int> _noSquadCompetitors;

        public static Dictionary<string, int> _competitionNameFilter;
        public static Dictionary<string, int> _competitorsNamesFilter;

        private Dictionary<string, int> _countryName;

        private IDataFetcher _dataFetcher;

        private SoccerWayOuterPageParser _outerPageParser;

        private SoccerWayInnerPageParser _innerPageParser;

        private Regex _nationalityRgx;

        private Dictionary<string, string> _totalCareerCompetitions;
        private COperationStatistics _buildGamingListByDateOS;
        private COperationStatistics _buildCompetitionsOS;
        private COperationStatistics _buildNationalCompetitorsOS;
        private COperationStatistics _buildCompetitorsOS;
        private Dictionary<ScanType, COperationStatistics> _scansOS;
        private COperationStatistics _scanPlayerTotalStatisticsOS;

        private DateTime _manualDailyScanDate;
        private List<Task> tasks = new List<Task>();
        #endregion

        #region Properties

        public override string Name
        {
            get { return string.Format("{0} {1}", DataSourceName, ScannerName); }
        }

        #endregion

        #region Ctors

        public SoccerWayScanner()
            : base(true, 5)
        {
            _buildGamingListByDateOS = new COperationStatistics();
            _buildCompetitionsOS = new COperationStatistics();
            _buildNationalCompetitorsOS = new COperationStatistics();
            _buildCompetitorsOS = new COperationStatistics();
            _scanPlayerTotalStatisticsOS = new COperationStatistics();
            _scansOS = new Dictionary<ScanType, COperationStatistics>()
            {
                {ScanType.All, new COperationStatistics()},
                //{ScanType.Custom, new COperationStatistics()},
                //{ScanType.DailyGames, new COperationStatistics()},
            };

            _competitors = new ConcurrentDictionary<string, CompetitorData>();
            _noSquadCompetitors = new ConcurrentDictionary<string, int>();

            _totalCareerCompetitions = new Dictionary<string, string>
            {
                {
                    "Domestic Cups",
                    "{\"type\":\"club\",\"formats\":[\"Domestic cup\", \"Domestic super cup\"]}"},
                {
                    "Domestic Leagues",
                    "{\"type\":\"club\",\"formats\":[\"Domestic league\"]}"},
                {
                    "International Cups",
                    "{\"type\":\"club\",\"formats\":[\"International cup\", \"International super cup\"]}"
                }
            };

            _competitionNameFilter = new Dictionary<string, int>()
            {
                { "Primera División", 1},
                { "Premier League", 1},
                { "Ligue 1", 1},
                { "Bundesliga", 1},
                { "Ligat ha'Al", 1},
                { "Serie A", 1},
            };

            _competitorsNamesFilter = new Dictionary<string, int>()
            {
                { "barcelona", 1},
                { "hapoel beer sheva", 1},
                { "liverpool", 1},
            };
            _countryName = new Dictionary<string, int>()
            {
                { "Israel", 1},
                { "England", 1},
                { "France", 1},
                { "Spain", 1},
                { "Germany", 1},
                { "Italy", 1},
                { "Brazil", 1},
            };

            _dataFetcher = new DataFetcher(true, true);

            _innerPageParser = new SoccerWayInnerPageParser(_dataFetcher);
            _outerPageParser = new SoccerWayOuterPageParser(_dataFetcher);

            _nationalityRgx = new Regex(@"\d{2,}(?<cntry>[\sa-zA-Z-]*)_\d{2,}_", RegexOptions.Compiled);

            _manualDailyScanDate = DateTime.MinValue;
        }

        public static CPlayerUpdate CreateUpdateAthleteObject()
        {
            return new CPlayerUpdate(DataSourceName);
        }

        #endregion

        #region Overrides

        public override void Start()
        {
            _isRunning = true;

            InitCompetitors().ContinueWith(t =>
            {
                if (_isRunning)
                {
                    
                    UpdateDailyScanTime().ContinueWith(innerT =>
                    {
                        //_dailyGameScanTimer.Start();
                        /*
                        Task.Factory.StartNew(
                            () => RunLoop(TimeSpan.FromMinutes(5), ScanType.DailyGames),
                            TaskCreationOptions.LongRunning);*/
                        
                    });
                    /*
                    Task.Delay(TimeSpan.FromMinutes(5)).ContinueWith(tsk =>
                    {
                        Task.Factory.StartNew(
                            () => RunLoop(TimeSpan.FromMinutes(30), ScanType.Custom),
                            TaskCreationOptions.LongRunning);
                    });
                    */
                    Task.Delay(TimeSpan.FromMinutes(5)).ContinueWith(tsk =>
                    {
                        Task.Factory.StartNew(() => RunLoop(TimeSpan.FromHours(12)),
                            TaskCreationOptions.LongRunning);
                    });
                }
            });
            Console.ReadLine();
        }

        public override void Stop()
        {
            _isRunning = false;
        }

        public override bool Scan()
        {
            return true;
        }

        public override bool Scan(DateTime start, DateTime end)
        {
            return true;
        }

        #endregion

        #region Private Methods

        #region Update Daily Games Schedule

        private Task UpdateDailyScanTime()
        {
            var dailyUpdate = GetDailyGamingList().ContinueWith(t =>
            {
                var competitorsPlayingToday = t.Result;

                foreach (var competitorPlayingToday in competitorsPlayingToday)
                {
                    CompetitorData competitorData = null;

                    if (_competitors.TryGetValue(competitorPlayingToday.Link, out competitorData))
                    {
                        if (competitorData.NextScan == null ||
                            competitorData.NextScan.Time < competitorPlayingToday.NextScan.Time)
                        {
                            competitorData.NextScan = competitorPlayingToday.NextScan;
                        }
                    }
                }
            });

            return dailyUpdate;
        }

        private Task<List<CompetitorData>> GetDailyGamingList()
        {
            var today = _manualDailyScanDate == DateTime.MinValue ? DateTime.Today : _manualDailyScanDate;

            return BuildGamingListByDate(today);
        }

        private async Task<List<CompetitorData>> BuildGamingListByDate(DateTime date)
        {
            var watch = new Stopwatch();
            watch.Start();

            var compList = await _outerPageParser.BuildCompetionListByDate(date.Date);

            var totalResults = new ConcurrentBag<CompetitorData>();

            try
            {
                var tasks = new List<Task>();

                for (var i = 0; i < compList.Count; i++)
                {
                    var competition = compList[i];
                    var task = _innerPageParser.BuildGamingListByDateForCompetition(competition, date).ContinueWith(t =>
                    {
                        var competitors = t.Result;
                        if (competitors != null && competitors.Count > 0)
                        {
                            competitors.ForEach(totalResults.Add);
                        }
                    });
                    tasks.Add(task);

                    if (tasks.Count > 0 && tasks.Count % 20 == 0)
                    {
                        Console.WriteLine("Waiting for {0} tasks cycle, finished {1}", 20, i);
                        var tasksToWaitFor = tasks.ToArray();
                        tasks.Clear();

                        await Task.WhenAll(tasksToWaitFor);
                    }
                }

                if (tasks.Count > 0)
                {
                    var tasksToWaitFor = tasks.ToArray();
                    tasks.Clear();

                    await Task.WhenAll(tasksToWaitFor);
                }
            }
            catch (Exception ex)
            {
            }

            _buildGamingListByDateOS.AddExecution(watch.Elapsed);
            watch.Stop();

            return totalResults.ToList();
        }

        #endregion

        #region Init Competitors List

        private async Task InitCompetitors()
        {
            var competitionList = await BuildCompetitionList();

            var competitors = await BuildCompetitorList(competitionList);
            if (competitors != null)
            {
                _competitors = competitors;
            }
        }

        private async Task<List<CompetitionData>> BuildCompetitionList()
        {
            var watch = new Stopwatch();
            watch.Start();

            var totalResults = new ConcurrentBag<CompetitionData>();

            var countryList = await _outerPageParser.GetClubCountries();

            var index = 0;

            try
            {
                
                foreach (var country in countryList)
                {
                    index++;

                    var task =
                        _innerPageParser.BuildCompetionListByCountry(country.Key, country.Value).ContinueWith(t =>
                        {
                            var competitions = t.Result;
                            if (competitions != null && competitions.Count > 0)
                            {
                                competitions.ForEach(totalResults.Add);
                            }
                        });
                    tasks.Add(task);

                    if (tasks.Count > 0 && tasks.Count % 20 == 0)
                    {
                        Console.WriteLine("Waiting for {0} tasks cycle, finished {1}", 20, index);
                        var tasksToWaitFor = tasks.ToArray();
                        tasks.Clear();

                        await Task.WhenAll(tasksToWaitFor);
                    }
                }

                if (tasks.Count > 0)
                {
                    var tasksToWaitFor = tasks.ToArray();
                    tasks.Clear();

                    await Task.WhenAll(tasksToWaitFor);
                }
            }
            catch (Exception exception)
            {
            }

            _buildCompetitionsOS.AddExecution(watch.Elapsed);
            watch.Stop();

            return totalResults.ToList();
        }

        private async Task<ConcurrentDictionary<string, CompetitorData>> BuildCompetitorList(
            List<CompetitionData> allCompetitions)
        {
            var watch = new Stopwatch();
            watch.Start();

            var allCompetitors = new ConcurrentDictionary<string, CompetitorData>();
            var rnd = new Random();
            var index = 0;

            try
            {
                var totalResults = new ConcurrentBag<CompetitorData>();

                var tasks = new List<Task>();
                foreach (var competition in allCompetitions)
                {
                    index++;

                    var task = _innerPageParser.BuildCompetitorsListByCompetition(competition).ContinueWith(t =>
                    {
                        var competititors = t.Result;
                        if (competititors != null && competititors.Count > 0)
                        {
                            competititors.ForEach(x =>
                            {
                                x.TotalCareerScanCounter = rnd.Next(0, 15);
                                totalResults.Add(x);
                            });
                        }
                    });
                    tasks.Add(task);

                    if (tasks.Count > 0 && tasks.Count % 20 == 0)
                    {
                        Console.WriteLine("Waiting for {0} tasks cycle, finished {1}", 20, index);
                        var tasksToWaitFor = tasks.ToArray();
                        tasks.Clear();

                        await Task.WhenAll(tasksToWaitFor);
                    }
                }

                if (tasks.Count > 0)
                {
                    var tasksToWaitFor = tasks.ToArray();
                    tasks.Clear();

                    await Task.WhenAll(tasksToWaitFor);
                }

                foreach (var item in totalResults)
                {
                    foreach (var competitor in _competitorsNamesFilter.Keys)
                    {
                        if (item.Link.ToLower().Contains(competitor.ToLower()))
                        {
                            if (!allCompetitors.ContainsKey(item.Link))
                            {
                                allCompetitors.TryAdd(item.Link, item);
                            }
                        }
                    }
                }

                var nationalCompetitors = await AddNationalCompetitors();
            }
            catch (Exception exception)
            {
            }

            _buildCompetitorsOS.AddExecution(watch.Elapsed);
            watch.Stop();

            return allCompetitors;
        }

        private async Task<List<CompetitorData>> AddNationalCompetitors()
        {
            var watch = new Stopwatch();
            watch.Start();

            var totalResults = new ConcurrentBag<CompetitorData>();

            var countryList = await _outerPageParser.GetNationalCountries();

            var index = 0;

            try
            {
                var tasks = new List<Task>();
                foreach (var country in countryList)
                {
                    index++;

                    var task =
                        _innerPageParser.BuildNationalCompetitorsListByCountry(country.Key, country.Value).ContinueWith(t =>
                        {
                            var nationalCompetitors = t.Result;
                            if (nationalCompetitors != null && nationalCompetitors.Count > 0)
                            {
                                nationalCompetitors.ForEach(totalResults.Add);
                            }
                        });
                    tasks.Add(task);

                    if (tasks.Count > 0 && tasks.Count % 20 == 0)
                    {
                        Console.WriteLine("Waiting for {0} tasks cycle, finished {1}", 20, index);
                        var tasksToWaitFor = tasks.ToArray();
                        tasks.Clear();

                        await Task.WhenAll(tasksToWaitFor);
                    }
                }

                if (tasks.Count > 0)
                {
                    var tasksToWaitFor = tasks.ToArray();
                    tasks.Clear();

                    await Task.WhenAll(tasksToWaitFor);
                }
            }
            catch (Exception exception)
            {
            }

            _buildCompetitionsOS.AddExecution(watch.Elapsed);
            watch.Stop();

            return totalResults.ToList();
        }

        #endregion

        #region Scan Competitors

        private List<CompetitorData> GetCompetitorsForScanning(ScanType scanType)
        {
            List<CompetitorData> res = null;

            switch (scanType)
            {
                case ScanType.Custom:
                    var list = new List<CompetitorData>();
                    foreach (var co in _competitors)
                    {
                        if (co.Value.CompetitionData == null)
                        {
                            continue;
                        }

                        if (_countryName.ContainsKey(co.Value.CompetitionData.Country))
                        {
                            if (_competitionNameFilter.ContainsKey(co.Value.CompetitionData.Name))
                            {
                                list.Add(co.Value);
                            }
                        }
                    }

                    res = list;
                    break;
                case ScanType.DailyGames:
                    var currentTime = _manualDailyScanDate == DateTime.MinValue ? DateTime.UtcNow : _manualDailyScanDate;
                    res = _competitors.Where(p => p.Value.NextScan != null &&
                                                  p.Value.NextScan.CompetitionData != null &&
                                                  p.Value.NextScan.Time > DateTime.MinValue &&
                                                  currentTime >= (_manualDailyScanDate == DateTime.MinValue ? p.Value.NextScan.Time : p.Value.NextScan.Time.Date) &&
                                                  currentTime < p.Value.NextScan.Time.AddHours(6))
                        .Select(x => x.Value)
                        .ToList();
                    break;
                case ScanType.All:
                    res = _competitors.Values.Where(x => x.CompetitionData != null).ToList();
                    break;
            }

            return res;
        }

        private async Task RunLoop(TimeSpan timeInterval, ScanType type = ScanType.All)
        {
            while (_isRunning)
            {
                try
                {
                    await Task.WhenAll(Task.Delay(timeInterval), Scan(type));

                }
                catch (Exception ex)
                {
                }
            }
        }

        private async Task Scan(ScanType scanType)
        {
            var competitorsForScanning = GetCompetitorsForScanning(scanType);
            if (competitorsForScanning == null)
            {
                return;
            }

            var watch = new Stopwatch();
            watch.Start();

            var skip = 0;
            var count = competitorsForScanning.Count;
            var competitors = competitorsForScanning.Take(10).ToList();

            while (count > 0)
            {
                try
                {
                    var result = ScanBatch(competitors, scanType);

                    await Task.WhenAll(Task.Delay(TimeSpan.FromSeconds(5)), result);

                    skip += 10;
                    count -= 10;
                    competitors = competitorsForScanning.Skip(skip).Take(10).ToList();
                }
                catch (Exception ex)
                {
                }
            }

            _scansOS[scanType].AddExecution(watch.Elapsed);
            watch.Stop();
        }

        private async Task ScanBatch(List<CompetitorData> competitors, ScanType scanType)
        {
            try
            {
                var tasks = new List<Task>();
                foreach (var competitor in competitors)
                {
                    var currentCompetition = scanType == ScanType.DailyGames && competitor.NextScan != null
                        ? competitor.NextScan.CompetitionData : competitor.CompetitionData;

                    var task = ScanSingleCompetitor(competitor, scanType)
                        .ContinueWith(t => HandleCompetitorScan(competitor, scanType, t.Result));
                    tasks.Add(task);
                }

                if (tasks.Count > 0)
                {
                    var tasksToWaitFor = tasks.ToArray();
                    tasks.Clear();

                    await Task.WhenAll(tasksToWaitFor);
                }
            }
            catch (Exception exception)
            {
            }
        }

        private async Task<Dictionary<string, CPlayerUpdate>> ScanSingleCompetitor(CompetitorData competitorData,
            ScanType scanType)
        {
            var list = new Dictionary<string, CPlayerUpdate>();

            try
            {
                var requestTime = DateTime.UtcNow;
                var fetchResult = await _innerPageParser.GetCompetitorPlayerStatBlock(competitorData.Id);
                var responseTime = DateTime.UtcNow;
                var defaultRawData = fetchResult.HtmlDocument;

                if (defaultRawData == null)
                {
                    return null;
                }

                string chosenLeague = string.Empty;
                var seasonDic =
                    ParseFunctions.ParseSeasonsSelect(defaultRawData.DocumentNode.SelectNodes("//select/optgroup/."), out chosenLeague);
                if (seasonDic == null)
                {
                    return null;
                }

                var rawData = defaultRawData;
                if (scanType == ScanType.DailyGames &&
                    seasonDic.ContainsKey(competitorData.NextScan.CompetitionData.Name))
                {
                    requestTime = DateTime.UtcNow;
                    fetchResult =
                        await
                            _innerPageParser.GetCompetitorPlayerStatBlock(competitorData.Id,
                                seasonDic[competitorData.NextScan.CompetitionData.Name].Key);
                    responseTime = DateTime.UtcNow;

                    rawData = fetchResult.HtmlDocument;
                    if (rawData == null)
                    {
                        return null;
                    }
                }

                var table = rawData.DocumentNode.SelectSingleNode("//table[contains(@class,table)]");
                if (table == null)
                {
                    return null;
                }

                var seasonId = table.GetAttributeValue("data-season_id", "");

                var season = seasonDic.FirstOrDefault(x => x.Value.Key == seasonId);

                var coachesNodes = defaultRawData.DocumentNode.SelectNodes("//table[@class='table squad']//tbody//tr//td[@class='name large-link']/a");

                if (coachesNodes != null)
                {
                    foreach (var coachNode in coachesNodes)
                    {
                        string coachPageLink = GetCoachPageLink(coachNode);

                        if (!string.IsNullOrWhiteSpace(coachPageLink))
                        {
                            FetchResult coachFetchResult = await _innerPageParser.GetCochCardBlock(coachPageLink);
                            HtmlDocument coachPageDoc = coachFetchResult.HtmlDocument;

                            string nationality;
                            string coachFullName;
                            DateTime? bornDate;
                            ParseCoachPersonalDetails(coachPageDoc, out nationality, out bornDate, out coachFullName);

                            CPlayerUpdate coachUpdate = GetCoachUpdate(competitorData, nationality, coachFullName, bornDate);

                            if (coachUpdate != null)
                            {
                                list.Add(coachPageLink, coachUpdate);
                            }
                        }
                    }
                }


                var dataRows = rawData.DocumentNode.SelectNodes("//tbody/tr");
                if (dataRows == null)
                {
                    return null;
                }

                var index = 1;

                foreach (var row in dataRows)
                {
                    try
                    {
                        string link = null;
                        var cAthleteUpdate = ParsePlayerCareerInSquadByCompetition(competitorData, season, row,
                            scanType, out link, chosenLeague);
                        if (cAthleteUpdate != null)
                        {
                            if (string.IsNullOrEmpty(link))
                            {
                                link = index.ToString();
                                index++;
                            }

                            if (!list.ContainsKey(link))
                            {
                                var currentCompetition = scanType == ScanType.DailyGames && competitorData.NextScan != null
                                    ? competitorData.NextScan.CompetitionData : competitorData.CompetitionData;
                                cAthleteUpdate.WebRequestTime = requestTime;
                                cAthleteUpdate.WebResponseTime = responseTime;
                                cAthleteUpdate.UsedProxy = fetchResult.UsedProxy;
                                list.Add(link, cAthleteUpdate);
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                       
                    }
                }
            }
            catch (Exception exception)
            {
            }

            return list;
        }

        private void ParseCoachPersonalDetails(HtmlDocument coachPageDoc, out string nationality, out DateTime? bornDate, out string coachFullName)
        {
            nationality = null;
            bornDate = null;
            coachFullName = null;

            var coachDetailsNodes = coachPageDoc.DocumentNode.SelectNodes("//div[@class='clearfix']//dl//dd");

            if (coachDetailsNodes != null && coachDetailsNodes.Count > 4)
            {
                coachFullName = coachDetailsNodes[0].InnerText.Trim() + " " + coachDetailsNodes[1].InnerText.Trim();
                nationality = coachDetailsNodes[2].InnerText.Trim();
                string bornDateValue = coachDetailsNodes[3].InnerText.Trim();
                DateTime coachBornDate;
                if (DateTime.TryParse(bornDateValue, out coachBornDate))
                {
                    bornDate = coachBornDate;
                }
            }
        }

        private CPlayerUpdate GetCoachUpdate(CompetitorData competitorData, string nationality, string coachFullName, DateTime? bornDate = null)
        {
            CPlayerUpdate coachUpdate = null;

            try
            {
                if (!string.IsNullOrWhiteSpace(coachFullName))
                {
                    coachUpdate = CreateUpdateAthleteObject();

                    coachUpdate.Name = coachFullName;

                    if (bornDate.HasValue)
                    {
                        coachUpdate.DOB = bornDate.Value;
                    }

                    coachUpdate.Position = (int)ESoccerPlayerPositions.Management;
                    coachUpdate.FormationPosition = (int)ESoccerPlayerFormationPositions.Coach;
                    coachUpdate.Competitor = competitorData.Name;
                    coachUpdate.Competition = competitorData.CompetitionData.Name;
                    coachUpdate.Country = competitorData.CompetitionData.Country;
                }
            }
            catch (Exception ex)
            {
                coachUpdate = null;
            }

            return coachUpdate;
        }

        private string GetCoachPageLink(HtmlNode coachNode)
        {
            string coachPageLink = coachNode.GetAttributeValue("href", string.Empty);

            return coachPageLink;
        }

        private void HandleCompetitorScan(CompetitorData competitorData, ScanType scanType, Dictionary<string, CPlayerUpdate> competititorUpdates)
        {
            if (scanType == ScanType.DailyGames)
            {
                competitorData.LastScaned = new ScanContext(DateTime.UtcNow);
                if (competitorData.NextScan != null)
                {
                    competitorData.LastScaned.CompetitionData = competitorData.NextScan.CompetitionData;
                }
            }

            if (competititorUpdates != null && competititorUpdates.Count > 0)
            {
                var currentCompetition = scanType == ScanType.DailyGames && competitorData.NextScan != null
                    ? competitorData.NextScan.CompetitionData : competitorData.CompetitionData;

                RaiseEvent(competititorUpdates.Values);

                if (competitorData.TotalCareerScanCounter >= 15)
                {

                    ScanPlayerTotalStats(competititorUpdates).ContinueWith(t =>
                    {
                        competitorData.TotalCareerScanCounter = 0;
                    });
                }
                else
                {
                    competitorData.TotalCareerScanCounter++;
                }
            }
            else
            {
                var noSquadIndication = _noSquadCompetitors.GetOrAdd(competitorData.Link, s => 0);
                Interlocked.Increment(ref noSquadIndication);
                if (noSquadIndication > 3)
                {
                    CompetitorData removedCompetitorData;
                    if (!_competitors.TryRemove(competitorData.Link, out removedCompetitorData))
                    {
                    }
                    else
                    {
                        int counter;
                        _noSquadCompetitors.TryRemove(competitorData.Link, out counter);
                    }
                }
            }
        }

        private CPlayerUpdate ParsePlayerCareerInSquadByCompetition(CompetitorData competitorData, KeyValuePair<string, KeyValuePair<string, string>> season,
            HtmlNode dataRow, ScanType scanType, out string link, string chosenLeague = null)
        {
            var ath = CreateUpdateAthleteObject();

            var infoRow = dataRow.ChildNodes.Where(rp => rp.Name == "td").ToArray();

            var rawLink = dataRow.SelectSingleNode(dataRow.XPath + "//td[contains(@class,'name large-link')]/a/.");

            link = rawLink != null ? rawLink.GetAttributeValue("href", "") : null;

            var cr = ParseFunctions.ParseCareerRecord(infoRow);

            if (cr != null)
            {
                ath.Name = ParseFunctions.ParseValue(dataRow, dataRow.XPath + "//td[contains(@class,'name')]/a/.");
                var rawJersey = ParseFunctions.ParseValue(dataRow,
                    dataRow.XPath + "//td[contains(@class,'shirtnumber')]/.");

                var jersey = -1;
                if (int.TryParse(rawJersey, out jersey))
                {
                    ath.JerseyNum = jersey;
                }

                var rawAge = ParseFunctions.ParseValue(dataRow, dataRow.XPath + "//td[contains(@class,'age')]/.");

                var age = -1;
                if (int.TryParse(rawAge, out age))
                {
                    ath.DOB = new DateTime(DateTime.Today.AddYears(-1 * age).Year, 1, 1);
                }

                var rawPosition = dataRow.SelectSingleNode(dataRow.XPath + "//td[contains(@class,'position')]/span/.");

                var position = -1;
                if (rawPosition != null)
                {
                    var strPosition = rawPosition.GetAttributeValue("title", "");

                    if (!string.IsNullOrEmpty(strPosition))
                    {
                        position = ParseFunctions.ParsePosition(strPosition);
                    }
                }

                var rawNationalityContainer =
                    dataRow.SelectSingleNode(dataRow.XPath + "//td[contains(@class,'flag')]/span/.");

                var rawNationality = rawNationalityContainer.GetAttributeValue("class", "");

                if (rawNationality != null)
                {
                    var m = _nationalityRgx.Match(rawNationality);
                    if (m.Success)
                    {
                        ath.Nationality = m.Groups["cntry"].Value.Trim();
                    }
                }

                if (scanType != ScanType.DailyGames && competitorData.CompetitionData == null)
                {
                    return null;
                }

                var currentCompetition = scanType == ScanType.DailyGames
                    ? competitorData.NextScan.CompetitionData.Name
                    : competitorData.CompetitionData.Name;

                var currentCompetitionCountry = scanType == ScanType.DailyGames
                    ? competitorData.NextScan.CompetitionData.Country
                    : competitorData.CompetitionData.Country;

                if (string.IsNullOrEmpty(chosenLeague))
                {
                    ath.Competition = competitorData.CompetitionData == null
                        ? currentCompetition
                        : competitorData.CompetitionData.Name;
                }
                else
                {
                    ath.Competition = chosenLeague;
                }
                ath.Country = competitorData.CompetitionData == null ? currentCompetitionCountry : competitorData.CompetitionData.Country;
                ath.Competitor = competitorData.Name;
                ath.Position = position;

                string statisticsCompetition = season.Key;
                string statisticsCountry = currentCompetition == season.Key ? currentCompetitionCountry : season.Key;

                var athleteStatisticsUpdate = new CAthleteStatisticsUpdate(cr)
                {
                    Season = new ParsableValue<int>(season.Value.Value),
                    Country = new ParsableValue<int>(statisticsCountry),
                    Competition = new ParsableValue<string>("A")
                };

                ath.Statistics = new List<CAthleteStatisticsUpdate>() { athleteStatisticsUpdate };
            }

            return ath;
        }

        #endregion

        #region Total Career Stats

        private async Task ScanPlayerTotalStats(Dictionary<string, CPlayerUpdate> athleteUpdates)
        {
            var watch = new Stopwatch();
            watch.Start();

            var rgx = new Regex("[\\w]*/(?<num>\\d+)/",
                RegexOptions.Singleline | RegexOptions.IgnoreCase | RegexOptions.Compiled);
            var count = 0;
            var tasks = new List<Task>();
            var totalUpdates = new ConcurrentBag<CPlayerUpdate>();

            foreach (var k in athleteUpdates)
            {
                var match = rgx.Match(k.Key);
                if (match.Success)
                {
                    count++;

                    var playerId = match.Groups["num"].Value;
                    var task = BuildPlayerCareer(playerId).ContinueWith((t) =>
                    {
                        if (t != null)
                        {
                            try
                            {
                                var result = t.Result;
                                var ath = k.Value.Copy();
                                ath.Statistics = new List<CAthleteStatisticsUpdate>()
                                {
                                    new CAthleteStatisticsUpdate(result)
                                    {
                                        Season = new ParsableValue<int>("CareerTotal"),
                                    }
                                };

                                totalUpdates.Add(ath);
                            }
                            catch (Exception ex)
                            {
                            }
                        }
                    });
                    tasks.Add(task);
                }

                if (tasks.Count > 0 && tasks.Count % 20 == 0)
                {
                    Console.WriteLine("Waiting for {0} tasks cycle, finished {1}", 20, count);
                    var tasksToWaitFor = tasks.ToArray();
                    tasks.Clear();

                    await Task.WhenAll(tasksToWaitFor);
                }
            }

            if (tasks.Count > 0)
            {
                var tasksToWaitFor = tasks.ToArray();
                tasks.Clear();

                await Task.WhenAll(tasksToWaitFor);
            }

            if (totalUpdates.Count > 0)
            {
                RaiseEvent(totalUpdates.ToList());
            }

            _scanPlayerTotalStatisticsOS.AddExecution(watch.Elapsed);
            watch.Stop();
        }
        //change function name.
        public override async void RaiseEvent(ICollection<CPlayerUpdate> Updates)
        {

            using (var client = new HttpClient())
            {
                foreach (var playerUpdate in Updates)
                {
                    try
                    {
                        var playerJson = new JavaScriptSerializer().Serialize(playerUpdate);
                        var response = await client.PostAsync(
                            "http://localhost:53121/api/PlayerUpdate",
                             new StringContent(playerJson, Encoding.UTF8, "application/json"));
                        var x = 0;
                    }
                    catch(Exception ex)
                    {

                    }

                }

            }
        }

        private async Task<List<CPlayerIndividualStat>> BuildPlayerCareer(string playerId)
        {
            Dictionary<int, CPlayerIndividualStat> dic = null;

            foreach (var comp in _totalCareerCompetitions)
            {
                var res = await _innerPageParser.BuildPlayerCareerByCompetition(playerId, comp.Value);
                if (res != null)
                {
                    if (dic == null)
                    {
                        dic = new Dictionary<int, CPlayerIndividualStat>();
                        foreach (var stat in res)
                        {
                            if (!dic.ContainsKey(stat.StatisticType))
                            {
                                dic.Add(stat.StatisticType, stat);
                            }
                        }
                    }
                    else
                    {
                        foreach (var stat in res)
                        {
                            if (dic.ContainsKey(stat.StatisticType))
                            {
                                var total = -1;
                                if (int.TryParse(dic[stat.StatisticType].Value, out total))
                                {
                                    var value = -1;
                                    if (int.TryParse(stat.Value, out value))
                                    {
                                        dic[stat.StatisticType].Value = (total + value).ToString();
                                    }
                                }
                            }
                            else
                            {
                                dic.Add(stat.StatisticType, stat);
                            }
                        }
                    }
                }
            }

            return dic != null ? dic.Values.ToList() : null;
        }

        #endregion

        #endregion

 
    }
}
