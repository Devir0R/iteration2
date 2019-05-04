using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Web;
using _365Players.Fetcher;
using _365Players.Scanners.Models;
using _365Players.Updates;

namespace _365Players.Parsers
{
    public class SoccerWayOuterPageParser
    {
        IDataFetcher _dataFetcher = null;


        protected const string URL_MATCHES_BY_DATE = "http://int.soccerway.com/a/block_date_matches?block_id=page_matches_1_block_date_matches_1&callback_params={{\"bookmaker_urls\":[],\"block_service_id\":\"matches_index_block_datematches\",\"date\":\"{0}\",\"display\":\"all\"}}&action=showMatches&params={{\"competition_id\":{1}}}";

        protected const string URL_PAST_MATCHES = "http://int.soccerway.com/a/block_date_matches?block_id=page_matches_1_block_date_matches_1&callback_params={{\"date\":\"{0}\",\"display\":\"all\"}}&action=showMatches&params={{\"competition_id\":{1}}}";

        protected const string URL_MATCHES_LIVE = "http://int.soccerway.com/a/block_home_matches?block_id=block_home_matches_17&callback_params={{\"date\":\"{0}\",\"display\":\"all\"}}&action=filterContent&params={{\"display\":\"now_playing\"}}";

        protected const string BASE_API_URL = "http://int.soccerway.com/a/";

        protected const string URL_CHAMPIONS_LEAGUE_TEAMS = "{0}block_competition_tables?block_id=page_competition_1_block_competition_tables_4&callback_params={1}&action=changeTable&params={2}";

        protected const string URL_CHAMPIONS_LEAGUE_TEAMS_CB_PARAMS = "{{\"season_id\":{0},\"round_id\":{1},\"outgroup\":false}}";

        protected const string URL_CHAMPIONS_LEAGUE_TEAMS_PARAMS = "{{\"type\":\"{0}\"}}";

        protected const string URL_EVENTS = "http://int.soccerway.com/a/block_date_matches?block_id=block_home_matches_17&callback_params={{\"date\":\"{0}\", \"display\":\"now_playing\"}}&action=showEvents&params={{\"match_id\":{1},\"odd_even\":\"even\"}}";

        protected const string URL_EVENTS_PAST = "http://int.soccerway.com/a/block_date_matches?block_id=page_matches_1_block_date_matches_1&callback_params={{\"date\":\"{0}\", \"display\":\"now_playing\"}}&action=showEvents&params={{\"match_id\":{1},\"odd_even\":\"even\"}}";

        protected const string URL_Daily_Competitions = "http://int.soccerway.com/matches/{0}";

        protected const string URL_CLUBS = "http://int.soccerway.com/teams/club-teams/";

        protected const string URL_NATIONAL = "http://int.soccerway.com/teams/national-teams/";

        protected const string PROXY_PATH = "http://btprox365.appspot.com/?url={0}";

        protected readonly int[] COMPETITON_PRIORTY_LIST = new int[] { 70, 10, 13, 7, 43 };

        private string MatchesDateFormat = "yyyy-MM-dd";

        public const string SCANNER_EXCEPTION_POLICY = "Scanners";

        private const string DailyCompetitionsDateFormat = "yyyy/MM/dd/";
        private const string MatchesByDateSelector = "//tr[contains(@id,'date_matches-')]/.";


        public SoccerWayOuterPageParser(IDataFetcher fetcher)
        {
            _dataFetcher = (DataFetcher)fetcher;
        }

        public async Task<Dictionary<int, string>> GetClubCountries()
        {
            var fetchResult = await _dataFetcher.FetchDom(URL_CLUBS);

            var allCountries = new Dictionary<int, string>();

            var countryList = fetchResult.HtmlDocument.DocumentNode.SelectNodes("//li[contains(@class,'expandable')]/.");
            foreach (var country in countryList)
            {
                try
                {
                    var areaId = country.GetAttributeValue("data-area_id", "");
                    if (!string.IsNullOrWhiteSpace(areaId))
                    {
                        var countryId = ParseFunctions.ParsePositiveNumber(areaId);
                        var countryName = HttpUtility.HtmlDecode(country.InnerText).Trim();
                        if (!allCountries.ContainsKey(countryId) && (countryName.ToLower() == "spain"|| countryName.ToLower() == "england"||
                                                                     countryName.ToLower() == "europe") )
                        {
                            allCountries.Add(countryId, countryName);
                        }
                    }
                }
                catch (Exception exception)
                {
                }
            }

            return allCountries;
        }

        public async Task<Dictionary<int, string>> GetNationalCountries()
        {
            var fetchResult = await _dataFetcher.FetchDom(URL_NATIONAL);

            var allCountries = new Dictionary<int, string>();

            var countryList = fetchResult.HtmlDocument.DocumentNode.SelectNodes("//li[contains(@class,'expandable')]/.");
            foreach (var country in countryList)
            {
                try
                {
                    var areaId = country.GetAttributeValue("data-area_id", "");
                    if (!string.IsNullOrWhiteSpace(areaId))
                    {
                        var countryId = ParseFunctions.ParsePositiveNumber(areaId);
                        var countryName = HttpUtility.HtmlDecode(country.InnerText).Trim();
                        if (!allCountries.ContainsKey(countryId))
                        {
                            allCountries.Add(countryId, countryName);
                        }
                    }
                }
                catch (Exception exception)
                {
                }
            }

            return allCountries;
        }

        public int[] ParseScore(string rawData)
        {
            string[] rawScoreArray = rawData.Split('-');
            List<int> scores = new List<int>();

            if (rawScoreArray.Length >= 2)
            {
                foreach (var rawScore in rawScoreArray)
                {
                    int score = -1;
                    if (!int.TryParse(rawScore.Trim(), out score))
                    {
                        // TODO: add log    
                    }
                    scores.Add(score);
                }
            }

            return scores.ToArray();
        }

        public void ParseSingleStageScore()
        {

        }

        public TimeSpan ParseTime(string rawDate)
        {

            int[] timeArray = new int[] { 16, 0 };
            string[] rawTimeArray = rawDate.Split(':');
            bool corectlyParsed = true;

            if (rawTimeArray.Length >= 2)
            {
                if (!int.TryParse(rawTimeArray[0], out timeArray[0]))
                {
                    corectlyParsed = false;
                }
            }

            if (!int.TryParse(rawTimeArray[1], out timeArray[1]))
            {
                corectlyParsed = false;
            }

            TimeSpan time = corectlyParsed ? new TimeSpan(timeArray[0], timeArray[1], 0) : new TimeSpan();


            return time;
        }

        public async Task<List<string>> BuildChampionsLeagueTeams()
        {
            var teamIds = new List<string>();

            var cbParam = string.Format(URL_CHAMPIONS_LEAGUE_TEAMS_CB_PARAMS, 9939, 25773); //9939,25773(14-15),8381,21299(13-14)
            var param = string.Format(URL_CHAMPIONS_LEAGUE_TEAMS_PARAMS, "competition_attendance_table");

            var url = string.Format(URL_CHAMPIONS_LEAGUE_TEAMS, BASE_API_URL, HttpUtility.UrlEncode(cbParam), HttpUtility.UrlEncode(param));

            var fetchResult = await _dataFetcher.FetchDom(url, true);

            var rawTeams = fetchResult.HtmlDocument.DocumentNode.SelectNodes("//table[contains(@class,'leaguetable')]/tbody/tr/.");
            foreach (var rawTeam in rawTeams)
            {
                var rawTeadCell = rawTeam.SelectSingleNode(rawTeam.XPath + "//td[1]/a/.");
                if (rawTeadCell != null)
                {
                    var rawId = rawTeadCell.GetAttributeValue("href", "");
                    if (!string.IsNullOrWhiteSpace(rawId))
                    {
                        teamIds.Add(rawId);
                    }
                }
            }

            return teamIds;
        }

        public async Task<IDictionary<string, CSoccerGameUpdate>> BuildGameListByDate(DateTime date, IList<string> CompetitionsWhiteList = null)
        {
            Task<IList<CompetitionData>> competitionTask = BuildCompetionListByDate(date.Date);

            IList<CompetitionData> compList = await competitionTask;
            IDictionary<string, CSoccerGameUpdate>[] arrayResult = null;
            IDictionary<string, CSoccerGameUpdate> result = new Dictionary<string, CSoccerGameUpdate>();

            try
            {
                List<Task<IDictionary<string, CSoccerGameUpdate>>> tasks = new List<Task<IDictionary<string, CSoccerGameUpdate>>>();
                int tasksCount = 0;

                compList = FilterCompetitionsListByWhitelist(compList, CompetitionsWhiteList);

                foreach (var comp in compList)
                {
                    tasks.Add(BuildGameListByCompetiionId(comp, date));
                    tasksCount++;
                    if (tasksCount % 20 == 0)
                    {
                        await Task.Delay(1000);
                    }

                }

                arrayResult = await Task.WhenAll(tasks);

                foreach (var partial in arrayResult)
                {
                    foreach (var kvp in partial)
                    {
                        if (!result.ContainsKey(kvp.Key))
                        {
                            result.Add(kvp.Key, kvp.Value);
                        }
                    }
                }


            }
            catch (Exception ex)
            {
            }

            return (Dictionary<string, CSoccerGameUpdate>)result;

        }

        private IList<CompetitionData> FilterCompetitionsListByWhitelist(IList<CompetitionData> compList, IList<string> CompetitionsWhiteList)
        {
            IList<CompetitionData> answer = compList;

            if (CompetitionsWhiteList != null)
            {
                answer = new List<CompetitionData>();
                foreach (var competition in compList)
                {
                    foreach (var compFilter in CompetitionsWhiteList)
                    {
                        if (competition.Name.ToLower().Contains(compFilter) || competition.Country.ToLower() == compFilter)
                        {
                            answer.Add(competition);
                            break;
                        }


                    }
                }
            }

            return answer;
        }

        // Builds game list with basic data for a game
        public async Task<IDictionary<string, CSoccerGameUpdate>> BuildGameListByCompetiionId(CompetitionData comp, DateTime date)
        {
            var strURL = string.Format(DateTime.UtcNow.Date > date.Date ? URL_PAST_MATCHES : URL_MATCHES_BY_DATE, date.ToString(MatchesDateFormat), comp.ID);

            var fetchResult = await _dataFetcher.FetchDom(strURL, true, true);

            IDictionary<string, CSoccerGameUpdate> gamesDictionary = null;

            gamesDictionary = fetchResult.HtmlDocument != null ? ParseGameList(fetchResult.HtmlDocument, date) : new Dictionary<string, CSoccerGameUpdate>();

            foreach (CSoccerGameUpdate game in gamesDictionary.Values)
            {
                game.Country.Value = comp.Country;
                game.Competition.Value = string.Format("{0} {1}", comp.Name, game.Competition.Value).Trim();

            }

            return gamesDictionary;
        }


        public async Task<IList<CompetitionData>> BuildCompetionListByDate(DateTime date)
        {
            var competitionList = new List<CompetitionData>();

            string matchesForDateUrl = string.Format(URL_Daily_Competitions, date.ToString(DailyCompetitionsDateFormat));
            var fetchResult = await _dataFetcher.FetchDom(matchesForDateUrl);
            if (fetchResult.HtmlDocument == null)
            {
                return competitionList;
            }

            var rawComptitions = fetchResult.HtmlDocument.DocumentNode.SelectNodes(MatchesByDateSelector);
            if (rawComptitions != null)
            {
                foreach (var rawComp in rawComptitions)
                {
                    try
                    {
                        var comp = ParseCompetition(rawComp);
                        competitionList.Add(comp);
                    }
                    catch (Exception ex)
                    {
                    }
                }
            }

            return competitionList;
        }


        public async Task<CSoccerGameUpdate> BuildGameEvents(int gameId, DateTime date, CSoccerGameUpdate game)
        {
            var url = string.Format(DateTime.UtcNow.Date > date.Date ? URL_EVENTS_PAST : URL_EVENTS, date.ToString(MatchesDateFormat), gameId);

            var fetchResult = await _dataFetcher.FetchDom(url, true);

            var matchEventsCollectionRaw = fetchResult.HtmlDocument.DocumentNode.SelectNodes("//tr[contains(@class,'event')]/.");
            game = ParseGameGoals(matchEventsCollectionRaw, game);

            return game;
        }

        public CompetitionData ParseCompetition(HtmlNode node)
        {
            var rawTitle = node.SelectSingleNode(node.XPath + "//h3/.");
            var escapedValues = HttpUtility.HtmlDecode(rawTitle.InnerText);
            var values = escapedValues.Split('-');
            var comp = new CompetitionData();

            if (values.Length >= 2)
            {
                comp.Country = values[0].Trim();
                comp.Name = values[1].Trim();
                if (values.Length > 2)
                {
                    comp.Name += values[2].Trim();
                }
            }

            var isClickable = node.GetAttributeValue("class", "");

            if (isClickable.Contains("loaded"))
            {
                comp.Loaded = true;
            }

            comp.ID = ParseCompetionId(node);

            return comp;
        }

        public int ParseCompetionId(HtmlNode comp)
        {
            Regex rgxDigit = new Regex(@"(?<num>\d+)", RegexOptions.IgnoreCase | RegexOptions.Singleline);
            var rawId = comp.GetAttributeValue("id", "");
            MatchCollection matchs = rgxDigit.Matches(rawId);
            int compId = 0;
            if (matchs != null)
            {
                if (!int.TryParse(matchs[0].Groups[0].Value, out compId))
                {
                    //TODO: Add log        
                }
            }

            return compId;
        }

        

        public IDictionary<string, CSoccerGameUpdate> ParseGameList(HtmlDocument doc, DateTime date)
        {
            var gamesDictionary = new Dictionary<string, CSoccerGameUpdate>();

            var rawGames = doc.DocumentNode.SelectNodes("//tr[contains(@class,'match')]/.");

            foreach (var rawGame in rawGames)
            {
                try
                {
                    CSoccerGameUpdate game = ParseBasicGameDetails(rawGame, date);
                    if (game != null)
                    {
                        string gameUri = ParseGameUri(rawGame);

                        string leaguePath = new StringBuilder().Append(rawGame.XPath).Append("/preceding-sibling::tr[contains(@class,'round-head') or contains(@class,'group-head')]").ToString();
                        string midComp = string.Empty;

                        if (!string.IsNullOrEmpty(gameUri) && !gamesDictionary.ContainsKey(gameUri))
                        {
                            HtmlNodeCollection leagueParent = null;
                            try
                            {
                                leagueParent = rawGame.SelectNodes(leaguePath);
                            }
                            catch (Exception ex)
                            {
                            }
                            if (leagueParent != null)
                            {
                                HtmlNode midCompNode = leagueParent.LastOrDefault();
                                if (midCompNode.Attributes["class"].Value.Contains("round-head"))
                                {
                                    midComp = ExtractMidCompetitionName(midCompNode);

                                }

                            }
                            game.Competition.Value = midComp;
                            gamesDictionary.Add(gameUri, game);
                        }

                    }
                }
                catch (Exception ex)
                {
                }
            }

            return gamesDictionary;

        }

        public string ParseGameUri(HtmlNode node)
        {
            var rawLink = node.SelectSingleNode(node.XPath + "//td[contains(@class,'score-time')]/a/.");

            if (rawLink == null)
            {
            }

            return rawLink.GetAttributeValue("href", "");
        }

        public CSoccerGameUpdate ParseBasicGameDetails(HtmlNode node, DateTime date)
        {
            var game = new CSoccerGameUpdate();

            game.HomeTeam.Name = ParseFunctions.ExtractValueFromNode(node, "//td[@class='team team-a ']/a/.", "title");
            game.AwayTeam.Name = ParseFunctions.ExtractValueFromNode(node, "//td[@class='team team-b ']/a/.", "title");

            var rawGameStartTime = ParseFunctions.ExtractValueFromNode(node, "//td[@class='score-time status']");
            var rawScore = ParseFunctions.ExtractValueFromNode(node, "//td[@class='score-time score']");
            var rawStatusMinute = ParseFunctions.ExtractValueFromNode(node, "//td[@class='minute visible']");

            if (!string.IsNullOrEmpty(rawScore))
            {
                var scores = ParseScore(rawScore);
                game.HomeTeam.Competitor.Scores[(int)ESoccerStages.CurrResult] = scores[0];
                game.AwayTeam.Competitor.Scores[(int)ESoccerStages.CurrResult] = scores[1];
            }

            if (!string.IsNullOrEmpty(rawGameStartTime))
            {
                var time = ParseFunctions.ParseTime(rawGameStartTime);
                game.StartTime = date.Date.Add(time).Subtract(DataFetcher.TimeOffset);
            }
            else
            {
                game.StartTime = date.Date.AddHours(16);
            }

            return game;

        }

        public CSoccerGameUpdate ParseGameGoals(HtmlNodeCollection matchEventsCollectionRaw, CSoccerGameUpdate game)
        {
            // Select evnet rows
            if (matchEventsCollectionRaw != null && matchEventsCollectionRaw.Count > 0)
            {

                int goalCount = 1;
                foreach (HtmlAgilityPack.HtmlNode gameEventRaw in matchEventsCollectionRaw)
                {
                    try
                    {
                        // Select team event
                        HtmlAgilityPack.HtmlNode[] goalListRaw = new HtmlAgilityPack.HtmlNode[]
                            { gameEventRaw.SelectSingleNode("td[@class='player player-a']"),
                              gameEventRaw.SelectSingleNode("td[@class='player player-b']")
                            };


                        CSportTypedCompetitorInGame<ESoccerStages> team = null;

                        for (int teamId = 0; teamId < 2; teamId++)
                        {
                            var eventRow = goalListRaw[teamId];

                            if (teamId == 0)
                            {
                                team = game.HomeTeam;
                            }
                            else
                            {
                                team = game.AwayTeam;
                            }
                            /*
                            if (eventRow != null)
                            {
                                LocalGameEvent goalEvent = ParseFunctions.ParseGoalEvent(eventRow);

                                if (goalEvent.PlayerData != null && !string.IsNullOrEmpty(goalEvent.PlayerData.Name))
                                {
                                    goalCount++;
                                    game.AddEvent(
                                        ESoccerEventTypes.Goal, goalCount,
                                        team,
                                        (int)goalEvent.GoalSubType,
                                        (int)goalEvent.GameTime, goalEvent.PlayerData.Name
                                        );
                                }
                            }*/
                        }


                    }
                    catch (Exception ex)
                    {
                    }


                }
            }


            return game;
        }

        private string ExtractMidCompetitionName(HtmlNode midCompNode)
        {
            const string MID_COMP_PATH = "./th/h4/a";
            string midComp = string.Empty;
            HtmlNode valNode = midCompNode.SelectSingleNode(MID_COMP_PATH);
            if (valNode != null)
            {
                midComp = valNode.InnerText.Trim();
            }

            return midComp;
        }

    }
}
