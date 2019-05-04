using _365Players.Fetcher;
using _365Players.Scanners.Models;
using _365Players.Services;
using _365Players.Updates;
using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;


namespace _365Players.Parsers
{
    internal class SoccerWayInnerPageParser 
    {
        protected const string ApiFormat = "{0}{1}{2}";
        protected const string BASE_API_URL = "http://int.soccerway.com/a/";
        protected const string API_ExtraParam = "&callback_params={0}&action={1}&params={2}";

        protected const string MatchesByDate_Block = "block_date_matches?block_id=page_matches_1_block_date_matches_1";
        protected const string MatchesByDate_CallbackParam = "{{\"bookmaker_urls\":[],\"block_service_id\":\"matches_index_block_datematches\",\"date\":\"{0}\",\"display\":\"all\"}}";
        protected const string MatchesByDate_Past_CallbackParam = "{{\"date\":\"{0}\",\"display\":\"all\"}}";
        protected const string MatchesByDate_Action = "showMatches";
        protected const string MatchesByDate_Param = "{{\"competition_id\":{0}}}";

        private string MatchesDateFormat = "yyyy-MM-dd";

        protected const string Competitions_Block = "block_teams_index_club_teams?block_id=page_teams_1_block_teams_index_club_teams_2";
        protected const string Competitions_CallbackParam = "{{\"level\":{0}}}";
        protected const string Competitions_Action = "expandItem";
        protected const string Competitions_Param = "{{\"area_id\":{0},\"level\":2,\"item_key\":\"area_id\"}}";

        protected const string NationalCompetitors_Block = "block_teams_index_national_teams?block_id=page_teams_1_block_teams_index_national_teams_2";

        protected const string Competitors_Param = "{{\"competition_id\":{0},\"level\":3,\"item_key\":\"competition_id\"}}";

        protected const string PlayersStats_Block = "block_team_squad?block_id=page_team_1_block_team_squad_3";
        protected const string PlayersStats_CallbackParam = "{{\"team_id\":\"{0}\"}}";
        protected const string PlayersStats_Action = "changeView";
        protected const string PlayersStats_Action_ByCompetition = "changeSquadSeason";
        protected const string PlayersStats_Param = "{{\"view\":{0}}}";
        protected const string PlayersStats_Param_Season = "{{\"season_id\":\"{0}\"}}";
        private const int PlayersStats_DefaultView = 1;

        protected const string PlayerCareerStats_Block = "block_player_career?block_id=page_player_1_block_player_career_7";
        protected string PlayerCareerStats_CallbackParam = "{{\"people_id\":\"{0}\"}}";
        protected const string PlayerCareerStats_Action = "changeTab";

        private HtmlNodeGetter m_htmlNodeGetter;

        private IDataFetcher _dataFetcher = null;

        protected const string URL_GAME_CENTER = "http://int.soccerway.com/matches/{0}/england/premier-league/tottenham-hotspur-football-club/sunderland-association-football-club/{1}/?ICID=HP_MS_01_01";

        protected const string BASE_URL = "http://int.soccerway.com/{0}";

        protected const string CHART_URL = "http://int.soccerway.com/charts/statsplus/{0}/";

        protected const string PITCH_URL = "http://www.scoresway.com/?sport=soccer&page=match&view=pitchview&id={0}";

        public const string SCANNER_EXCEPTION_POLICY = "Scanners";

        public SoccerWayInnerPageParser(IDataFetcher fetcher)
        {
            _dataFetcher = fetcher;
            m_htmlNodeGetter = new HtmlNodeGetter();
        }

        #region GamingList

        public async Task<List<CompetitorData>> BuildGamingListByDateForCompetition(CompetitionData comp, DateTime date)
        {
            List<CompetitorData> competitors = null;

            try
            {
                var param = string.Format(API_ExtraParam,
                    HttpUtility.UrlEncode(string.Format(DateTime.UtcNow.Date > date.Date ? MatchesByDate_Past_CallbackParam : MatchesByDate_CallbackParam, date.ToString(MatchesDateFormat))),
                    MatchesByDate_Action,
                    HttpUtility.UrlEncode(string.Format(MatchesByDate_Param, comp.ID)));

                var url = string.Format(ApiFormat, BASE_API_URL, MatchesByDate_Block, param);

                var fetchResult = await _dataFetcher.FetchDom(url, true);

                competitors = fetchResult != null ? ParseGameList(fetchResult.HtmlDocument, date) : new List<CompetitorData>();

                foreach (var competitor in competitors)
                {
                    competitor.CompetitionData = new CompetitionData(comp);
                    competitor.NextScan.CompetitionData = comp;
                }
            }
            catch (Exception exception)
            {
            }

            return competitors;
        }

        private List<CompetitorData> ParseGameList(HtmlDocument doc, DateTime date)
        {
            var playingTeams = new List<CompetitorData>();

            try
            {
                var rawGames = doc.DocumentNode.SelectNodes("//tr[contains(@class,'match')]/.");
                if (rawGames != null)
                {
                    foreach (var rawGame in rawGames)
                    {
                        try
                        {
                            var teamsInGame = ParseBasicGameDetails(rawGame, date);

                            if (teamsInGame != null)
                            {
                                playingTeams.AddRange(teamsInGame);
                            }
                        }
                        catch (Exception ex)
                        {
                        }
                    }
                }
            }
            catch (Exception exception)
            {
            }

            return playingTeams;
        }

        private List<CompetitorData> ParseBasicGameDetails(HtmlNode node, DateTime date)
        {
            List<CompetitorData> game = null;

            try
            {
                var home = new CompetitorData();
                var away = new CompetitorData();

                home.Name = ParseFunctions.ExtractValueFromNode(node, "//td[@class='team team-a ']/a/.", "title");
                away.Name = ParseFunctions.ExtractValueFromNode(node, "//td[@class='team team-b ']/a/.", "title");

                home.Link = ParseFunctions.ExtractValueFromNode(node, "//td[@class='team team-a ']/a/.", "href");
                away.Link = ParseFunctions.ExtractValueFromNode(node, "//td[@class='team team-b ']/a/.", "href");

                home.Id = ParseFunctions.ParsePositiveNumber(home.Link, "/(?<num>\\d{2,})");
                away.Id = ParseFunctions.ParsePositiveNumber(away.Link, "/(?<num>\\d{2,})");

                var rawGameStartTime = ParseFunctions.ExtractValueFromNode(node, "//td[@class='score-time status']");
                var rawScore = ParseFunctions.ExtractValueFromNode(node, "//td[@class='score-time score']");

                if (!string.IsNullOrEmpty(rawGameStartTime))
                {
                    var time = ParseFunctions.ParseTime(rawGameStartTime);
                    var gameTime = date.Date.Add(time).Subtract(DataFetcher.TimeOffset);
                    var scanContext = new ScanContext(gameTime);
                    home.NextScan = scanContext;
                    away.NextScan = scanContext;
                }
                else
                {
                    var gameTime = date.Date.AddHours(16);
                    var scanContext = new ScanContext(gameTime);
                    home.NextScan = scanContext;
                    away.NextScan = scanContext;
                }

                game = new List<CompetitorData>() { home, away };
            }
            catch (Exception exception)
            {
            }

            return game;
        }

        #endregion

        public async Task<List<CompetitionData>> BuildCompetionListByCountry(int areaId, string countryName)
        {
            var competitionList = new List<CompetitionData>();

            try
            {
                var param = string.Format(API_ExtraParam,
                    HttpUtility.UrlEncode(string.Format(Competitions_CallbackParam, 1)),
                    Competitions_Action,
                    HttpUtility.UrlEncode(string.Format(Competitions_Param, areaId)));

                var url = string.Format(ApiFormat, BASE_API_URL, Competitions_Block, param);

                var fetchResult = await _dataFetcher.FetchDom(url, true);

                var rawComptitions = fetchResult.HtmlDocument.DocumentNode.SelectNodes("//ul/li/.");
                if (rawComptitions != null)
                {
                    foreach (var rawComp in rawComptitions)
                    {
                        try
                        {
                            var comp = ParseFunctions.ParseCompetition(rawComp, countryName);
                            if (comp == null || comp.Name.ToLowerInvariant() == "other")
                            {
                                Console.WriteLine("Skipped competition of country {0}", countryName);
                                continue;
                            }

                            competitionList.Add(comp);
                        }
                        catch (Exception ex)
                        {
                        }
                    }
                }
            }
            catch (Exception exception)
            {
            }

            return competitionList;
        }

        public async Task<List<CompetitorData>> BuildNationalCompetitorsListByCountry(int areaId, string countryName)
        {
            var competitorsList = new List<CompetitorData>();

            try
            {
                var param = string.Format(API_ExtraParam,
                    HttpUtility.UrlEncode(string.Format(Competitions_CallbackParam, 2)),
                    Competitions_Action,
                    HttpUtility.UrlEncode(string.Format(Competitions_Param, areaId)));

                var url = string.Format(ApiFormat, BASE_API_URL, NationalCompetitors_Block, param);

                var fetchResult = await _dataFetcher.FetchDom(url, true);

                var rawCompetitors = fetchResult.HtmlDocument.DocumentNode.SelectNodes("//ul/li/.");
                if (rawCompetitors != null)
                {
                    foreach (var rawComp in rawCompetitors)
                    {
                        try
                        {
                            var rawLink = rawComp.SelectSingleNode(rawComp.XPath + "//a/.");

                            var link = rawLink.GetAttributeValue("href", "");
                            if (string.IsNullOrEmpty(link))
                            {
                            }
                            else
                            {
                                var comp = new CompetitorData
                                {
                                    Link = link,
                                    Id = ParseFunctions.ParsePositiveNumber(link, "/(?<num>\\d{2,})"),
                                    Name = HttpUtility.HtmlDecode(rawLink.InnerText),
                                    CompetitionData = null
                                };

                                competitorsList.Add(comp);
                            }
                        }
                        catch (Exception ex)
                        {
                        }
                    }
                }
            }
            catch (Exception exception)
            {
            }

            return competitorsList;
        }

        public async Task<List<CompetitorData>> BuildCompetitorsListByCompetition(CompetitionData comptition)
        {
            var competitorList = new List<CompetitorData>();

            try
            {
                var param = string.Format(API_ExtraParam,
                    HttpUtility.UrlEncode(string.Format(Competitions_CallbackParam, 2)),
                    Competitions_Action,
                    HttpUtility.UrlEncode(string.Format(Competitors_Param, comptition.ID)));

                var url = string.Format(ApiFormat, BASE_API_URL, Competitions_Block, param);

                var fetchResult = await _dataFetcher.FetchDom(url, true);

                var rawComptitor = fetchResult.HtmlDocument.DocumentNode.SelectNodes("//ul/li/.");
                if (rawComptitor != null)
                {
                    foreach (var rawComp in rawComptitor)
                    {
                        try
                        {
                            var rawLink = rawComp.SelectSingleNode(rawComp.XPath + "//a/.");

                            var link = rawLink.GetAttributeValue("href", "");
                            if (string.IsNullOrEmpty(link))
                            {
                            }
                            else
                            {
                                var comp = new CompetitorData
                                {
                                    Link = link,
                                    Id = ParseFunctions.ParsePositiveNumber(link, "/(?<num>\\d{2,})"),
                                    Name = HttpUtility.HtmlDecode(rawLink.InnerText),
                                    CompetitionData = comptition
                                };

                                competitorList.Add(comp);
                            }
                        }
                        catch (Exception ex)
                        {
                        }
                    }
                }
            }
            catch (Exception exception)
            {
            }

            return competitorList;
        }

        public async Task<FetchResult> GetCompetitorPlayerStatBlock(int competitorId, string seasonId = "")
        {
            var param = string.Format(API_ExtraParam,
               HttpUtility.UrlEncode(string.Format(PlayersStats_CallbackParam, competitorId)),
               PlayersStats_Action,
               HttpUtility.UrlEncode(string.Format(PlayersStats_Param, PlayersStats_DefaultView)));

            if (seasonId != "")
            {
                param = string.Format(API_ExtraParam,
                    HttpUtility.UrlEncode(string.Format(PlayersStats_CallbackParam, competitorId)),
                    PlayersStats_Action_ByCompetition,
                    HttpUtility.UrlEncode(string.Format(PlayersStats_Param_Season, seasonId)));
            }

            var url = string.Format(ApiFormat, BASE_API_URL, PlayersStats_Block, param);

            var fetchResult = await _dataFetcher.FetchDom(url, true, true);

            return fetchResult;
        }

        public async Task<FetchResult> GetCochCardBlock(string coachUrl)
        {
            var url = "http://int.soccerway.com" + coachUrl;

            var fetchResult = await _dataFetcher.FetchDom(url);

            return fetchResult;
        }

        public async Task<List<CPlayerIndividualStat>> BuildPlayerCareerByCompetition(string playerId, string competitionParams)
        {
            var param = string.Format(API_ExtraParam,
                    HttpUtility.UrlEncode(string.Format(PlayerCareerStats_CallbackParam, playerId)),
                    PlayerCareerStats_Action,
                    HttpUtility.UrlEncode(competitionParams));

            var url = string.Format(ApiFormat, BASE_API_URL, PlayerCareerStats_Block, param);

            var fetchResult = await _dataFetcher.FetchDom(url, true);

            var rawTotalRow = fetchResult.HtmlDocument.DocumentNode.SelectSingleNode("//tfoot/tr/.");
            if (rawTotalRow == null)
            {
                return null;
            }

            var totalRow = rawTotalRow.ChildNodes.Where(rp => rp.Name == "td").ToArray();

            var careerRecord = ParseFunctions.ParseCareerRecord(totalRow);
            return careerRecord;
        }

        public async Task BuildGameFullDetails(string gameUrl, ScanMetaData scanMetaData)
        {
            scanMetaData.Update = scanMetaData.Update ?? new CSoccerGameUpdate() { CreateGame = false };

            CSoccerGameUpdate gameUpdate = scanMetaData.Update;

            try
            {
                var fetchResult = await _dataFetcher.FetchDom(string.Format(BASE_URL, gameUrl));
                var doc = fetchResult.HtmlDocument;

                scanMetaData.JsonGameRequestCode = GetJsonGameRequestCode(doc);
                if (string.IsNullOrWhiteSpace(scanMetaData.JsonGameRequestCode))
                {
                    SetTeamsLineups(doc, gameUpdate);
                    SetTeamsSubsEvents(doc, gameUpdate);
                }
            }
            catch (Exception ex)
            {
            }
        }

        private void SetTeamsSubsEvents(HtmlDocument doc, CSoccerGameUpdate gameUpdate)
        {
            SetHomeTeamSubsEvents(doc, gameUpdate.HomeTeam.Competitor);
            SetAwayTeamSubsEvents(doc, gameUpdate.AwayTeam.Competitor);
        }



        private void SetHomeTeamSubsEvents(HtmlDocument doc, CPrimitiveCompetitorInGame homeCompetitor)
        {
            string homeTeamSubstitutesPlayersXpath =
                "//div[@class='content-column']//div[@class='combined-lineups-container'][2]//div[@class='container left']//table[@class='playerstats lineups substitutions table']//tbody//tr";

            HtmlNodeCollection homeTeamSubstitutesPlayersNodes = m_htmlNodeGetter.GetHtmlNodeCollection(doc.DocumentNode, homeTeamSubstitutesPlayersXpath);

            if (homeTeamSubstitutesPlayersNodes?.Count > 0)
            {
            }
        }

        private void SetAwayTeamSubsEvents(HtmlDocument doc, CPrimitiveCompetitorInGame awayCompetitor)
        {
            string awayTeamSubstitutesPlayersXpath =
               "//div[@class='content-column']//div[@class='combined-lineups-container'][2]//div[@class='container right']//table[@class='playerstats lineups substitutions table']//tbody//tr";

            HtmlNodeCollection awayTeamSubstitutesPlayersNodes = m_htmlNodeGetter.GetHtmlNodeCollection(doc.DocumentNode, awayTeamSubstitutesPlayersXpath);

            if (awayTeamSubstitutesPlayersNodes?.Count > 0)
            {
            }
        }
        
        private void SetTeamsLineups(HtmlDocument doc, CSoccerGameUpdate gameUpdate)
        {
            SetHomeTeamLineup(doc, gameUpdate.HomeTeam.Competitor);
            SetAwayTeamLineup(doc, gameUpdate.AwayTeam.Competitor);
        }

        private void SetHomeTeamLineup(HtmlDocument doc, CPrimitiveCompetitorInGame homeCompetitor)
        {
            string homeTeamStartingPlayersXpath =
                "//div[@class='content-column']//div[@class='combined-lineups-container'][1]//div[@class='container left']//table[@class='playerstats lineups table']//tbody//tr";
            string homeTeamSubstitutesPlayersXpath =
                "//div[@class='content-column']//div[@class='combined-lineups-container'][2]//div[@class='container left']//table[@class='playerstats lineups substitutions table']//tbody//tr";

            HtmlNodeCollection homeTeamStartingPlayersNodes = m_htmlNodeGetter.GetHtmlNodeCollection(doc.DocumentNode, homeTeamStartingPlayersXpath);
            HtmlNodeCollection homeTeamSubstitutesPlayersNodes = m_htmlNodeGetter.GetHtmlNodeCollection(doc.DocumentNode, homeTeamSubstitutesPlayersXpath);

            if (homeTeamSubstitutesPlayersNodes?.Count > 0 && homeTeamStartingPlayersNodes?.Count > 0)
            {
               // SetTeamStartingPlayers(homeTeamStartingPlayersNodes, homeCompetitor);
               // SetTeamSubstitutesPlayers(homeTeamSubstitutesPlayersNodes, homeCompetitor);
            }
            /*
            if (homeCompetitor.Lineup.Count < 12)
            {
                homeCompetitor.Lineup.ClearLineup();
            }*/
        }

        private void SetAwayTeamLineup(HtmlDocument doc, CPrimitiveCompetitorInGame awayCompetitor)
        {
            string awayTeamStartingPlayersXpath =
                "//div[@class='content-column']//div[@class='combined-lineups-container'][1]//div[@class='container right']//table[@class='playerstats lineups table']//tbody//tr";
            string awayTeamSubstitutesPlayersXpath =
                "//div[@class='content-column']//div[@class='combined-lineups-container'][2]//div[@class='container right']//table[@class='playerstats lineups substitutions table']//tbody//tr";

            HtmlNodeCollection awayTeamStartingPlayersNodes = m_htmlNodeGetter.GetHtmlNodeCollection(doc.DocumentNode, awayTeamStartingPlayersXpath);
            HtmlNodeCollection awayTeamSubstitutesPlayersNodes = m_htmlNodeGetter.GetHtmlNodeCollection(doc.DocumentNode, awayTeamSubstitutesPlayersXpath);

            if (awayTeamSubstitutesPlayersNodes?.Count > 0 && awayTeamStartingPlayersNodes?.Count > 0)
            {
            }
        }
        

        private int GetPlayerJerseyNum(HtmlNode playerNode)
        {
            int playerJerseyNum = 0;

            string playerJerseyNumXpath = "./td[@class='shirtnumber']";

            HtmlNode playerJerseyNumNode = m_htmlNodeGetter.GetHtmlNode(playerNode, playerJerseyNumXpath);

            if (playerJerseyNumNode != null)
            {
                if (!int.TryParse(playerJerseyNumNode.InnerText, out playerJerseyNum))
                {
                    playerJerseyNum = 0;
                }
            }

            return playerJerseyNum;
        }

        private string GetCoachName(HtmlNode coachNode)
        {
            string coachName = null;

            string coachNameXpath = "./td//a";

            HtmlNode coachNameNode = m_htmlNodeGetter.GetHtmlNode(coachNode, coachNameXpath);

            if (coachNameNode != null)
            {
                coachName = coachNameNode.InnerText.Trim();
            }

            return coachName;
        }

        private string GetStartingPlayerName(HtmlNode startingPlayerNode)
        {
            string playerName = null;

            string playerNameXpath = "./td[@class='player large-link']//a";

            HtmlNode playerNameNode = m_htmlNodeGetter.GetHtmlNode(startingPlayerNode, playerNameXpath);

            if (playerNameNode != null)
            {
                playerName = playerNameNode.InnerText.Trim();
            }

            return playerName;
        }
        
        private string GetSubstitutePlayerName(HtmlNode substitutePlayerNode)
        {
            string playerName = null;

            string playerNameXpath = "./td[@class='player large-link']//p[@class='substitute substitute-in']//a";

            HtmlNode playerNameNode = m_htmlNodeGetter.GetHtmlNode(substitutePlayerNode, playerNameXpath);

            if (playerNameNode != null)
            {
                playerName = playerNameNode.InnerText.Trim();
            }

            return playerName;
        }

        private string GetJsonGameRequestCode(HtmlDocument doc)
        {
            string jsonGameRequestCode = null;
            string jsonGameRequestCodeXpath = "//opta-widget[@sport='football']";

            HtmlNode jsonGameRequestCodeNode = null;

            try
            {
                jsonGameRequestCodeNode = doc.DocumentNode.SelectSingleNode(jsonGameRequestCodeXpath);
            }
            catch (Exception)
            {

            }

            if (jsonGameRequestCodeNode != null)
            {
                jsonGameRequestCode = jsonGameRequestCodeNode.GetAttributeValue("match", String.Empty);
            }

            return jsonGameRequestCode;
        }
    }
}
