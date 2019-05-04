using _365Players.Scanners.Models;
using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text.RegularExpressions;
using System.Web;
using static _365Players.Enums.PlayerEnums;

namespace _365Players.Parsers
{
    public static class ParseFunctions
    {
        public static Dictionary<string, string> EventDetailsPattren { get; set; }

        static ParseFunctions()
        {
            EventDetailsPattren = new Dictionary<string, string>();

            EventDetailsPattren.Add("scorerID", "<div>.*?player&id=(?<scorerID>\\d+)\">");
            EventDetailsPattren.Add("scorer", "<div>.*?<a.*?>(?<scorer>.*?)</a>");
            EventDetailsPattren.Add("subtype", "</a>.*?\\((?<subtype>.*?)\\).*?<span");
            EventDetailsPattren.Add("minute", "minute\">.*?(?<minute>\\d+).*?</span>");
            EventDetailsPattren.Add("assist", "assist by.*?<a.*?>(?<assist>.*?)</a>");
        }

        public static TimeSpan ParseTime(string rawDate)
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

                if (!int.TryParse(rawTimeArray[1], out timeArray[1]))
                {
                    corectlyParsed = false;
                }
            }



            TimeSpan time = corectlyParsed ? new TimeSpan(timeArray[0], timeArray[1], 0) : new TimeSpan();


            return time;
        }

        public static string ExtractValueFromNode(HtmlNode node, string xpath, string attribute = null)
        {
            var rawContainer = node.SelectSingleNode(node.XPath + xpath);
            string value = "";
            if (rawContainer != null)
            {
                if (attribute == null)
                {
                    value = HttpUtility.HtmlDecode(rawContainer.InnerText).Trim();
                }
                else
                {
                    value = rawContainer.GetAttributeValue(attribute, "");
                }
            }

            return value;
        }

        public static LocalGameEvent ParseGoalEvent(HtmlNode node)
        {

            LocalGameEvent goalEvent = new LocalGameEvent();
            goalEvent.PlayerData = null;
            goalEvent.GameTime = 0;

            foreach (KeyValuePair<string, string> kvp in EventDetailsPattren)
            {
                Regex rgxPlayer = new Regex(kvp.Value, RegexOptions.IgnoreCase | RegexOptions.Singleline, TimeSpan.FromSeconds(10));
                Match eventDetails = rgxPlayer.Match(node.InnerHtml);

                if (eventDetails != null && eventDetails.Success)
                {
                    if (eventDetails.Groups[kvp.Key] != null)
                    {
                        switch (kvp.Key)
                        {
                            /*
                                        case "scorerID":
                                            int id = 0;

                                            if(eventDetails.Groups[kvp.Key].Value != null && int.TryParse(eventDetails.Groups[kvp.Key].Value.ToString().Trim(), out id))
                                            {
                                                goalEvent.Player = GetPlayerDetails(id);
                                            }

                                            break;
                                                        */
                            case "scorer":
                                if (goalEvent.PlayerData == null)
                                {
                                    goalEvent.PlayerData = new PlayerData();
                                    goalEvent.PlayerData.Name = eventDetails.Groups[kvp.Key].Value.ToString().Trim();
                                }
                                break;
                            case "assist":
                                if (goalEvent.AssistPlayerData == null)
                                {
                                    goalEvent.AssistPlayerData = new PlayerData()
                                    {
                                        Name = eventDetails.Groups[kvp.Key].Value.Trim()
                                    };
                                }
                                break;
                            /*
                            case "subtype":
                                switch (eventDetails.Groups[kvp.Key].Value.ToString().Trim().ToUpper())
                                {
                                    case "G":
                                        goalEvent.GoalSubType = ESoccerGoalEventSubType.Field;
                                        break;
                                    case "PG":
                                        goalEvent.GoalSubType = ESoccerGoalEventSubType.Penalty;
                                        break;
                                    case "OG":
                                        goalEvent.GoalSubType = ESoccerGoalEventSubType.Own;
                                        break;
                                    default:
                                        goalEvent.GoalSubType = ESoccerGoalEventSubType.Field;
                                        break;
                                }
                                break;*/
                            case "minute":
                                int min = 0;

                                if (int.TryParse(eventDetails.Groups[kvp.Key].Value.ToString().Trim(), out min))
                                {
                                    goalEvent.GameTime = min;
                                }
                                break;
                            default:
                                break;

                        }
                    }
                }
            }
            /*
            //Handle cases that the penalty goal doesnt parsed form the switch case statement
            if (node.InnerText.Contains("(PG)"))
            {
                goalEvent.GoalSubType = ESoccerGoalEventSubType.Penalty;
            }*/
            return goalEvent;
        }

        public static CPlayerIndividualStat ParseSingleStat(string itemName, string escapedTest)
        {
            CPlayerIndividualStat stat = null;

            if (itemName.ToLower().Contains("lineups"))
            {
                stat = new CPlayerIndividualStat() { StatisticType = (int)ESoccerPlayerStatistics.Lineups, Value = escapedTest };
            }
            else if (itemName.ToLower().Contains("game-minutes"))
            {
                stat = new CPlayerIndividualStat() { StatisticType = (int)ESoccerPlayerStatistics.TimePlayed, Value = escapedTest };
            }
            else if (itemName.ToLower().Contains("appearances"))
            {
                stat = new CPlayerIndividualStat() { StatisticType = (int)ESoccerPlayerStatistics.Appearences, Value = escapedTest };
            }
            else if (itemName.ToLower().Contains("subs-in"))
            {
                stat = new CPlayerIndividualStat() { StatisticType = (int)ESoccerPlayerStatistics.Substitutions, Value = escapedTest };
            }
            else if (itemName.ToLower().Contains("goals"))
            {
                stat = new CPlayerIndividualStat() { StatisticType = (int)ESoccerPlayerStatistics.Goals, Value = escapedTest };
            }
            else if (itemName.ToLower().Contains("assists"))
            {
                if (!string.IsNullOrWhiteSpace(escapedTest) && escapedTest.Trim() != "0")
                {
                    stat = new CPlayerIndividualStat() { StatisticType = (int)ESoccerPlayerStatistics.Assists, Value = escapedTest };
                }
            }
            else if (itemName.ToLower().Contains("yellow") && !itemName.ToLower().Contains("2nd"))
            {
                stat = new CPlayerIndividualStat() { StatisticType = (int)ESoccerPlayerStatistics.YellowCards, Value = escapedTest };
            }
            else if (itemName.ToLower().Contains("yellow") && itemName.ToLower().Contains("2nd"))
            {
                stat = new CPlayerIndividualStat() { StatisticType = (int)ESoccerPlayerStatistics.RedCards, Value = escapedTest };
            }
            else if (itemName.ToLower().Contains("red-cards") || itemName.ToLower().Contains("red_cards"))
            {
                stat = new CPlayerIndividualStat() { StatisticType = (int)ESoccerPlayerStatistics.RedCards, Value = escapedTest };
            }

            return stat;

        }

        public static List<CPlayerIndividualStat> ParseCareerRecord(HtmlNode[] dataItems)
        {
            List<CPlayerIndividualStat> cr = new List<CPlayerIndividualStat>();
            CultureInfo enUS = new CultureInfo("en-US");


            if (dataItems == null)
            {
                return null;
            }

            foreach (var dataItem in dataItems)
            {
                try
                {
                    var itemName = dataItem.GetAttributeValue("class", "");
                    var escapedTest = HttpUtility.HtmlDecode(dataItem.InnerText.Trim());

                    var stat = ParseSingleStat(itemName, escapedTest);
                    if (stat != null)
                    {
                        var originalStat = cr.FirstOrDefault(S => S.StatisticType == stat.StatisticType);
                        if (originalStat == null)
                        {
                            cr.Add(stat);
                        }
                        else
                        {
                            try
                            {
                                float originalValue = 0;
                                float newValue = 0;

                                if (!float.TryParse(originalStat.Value, out originalValue)) { originalValue = 0; }
                                if (!float.TryParse(stat.Value, out newValue)) { newValue = 0; }
                                originalStat.Value = (originalValue + newValue).ToString();
                            }
                            catch (Exception e)
                            {
                            }
                        }
                    }
                }
                catch (Exception ex)
                {
                }


            }

            return cr;
        }

        public static Dictionary<string, KeyValuePair<string, string>> ParseSeasonsSelect(HtmlNodeCollection nodes, out string currLeague)
        {
            var dic = new Dictionary<string, KeyValuePair<string, string>>();
            currLeague = string.Empty;
            if (nodes == null)
            {
                return null;
            }

            foreach (var node in nodes)
            {
                var leagueName = node.GetAttributeValue("label", "");
                if (!string.IsNullOrEmpty(leagueName))
                {
                    var currentSeason = node.SelectSingleNode(node.XPath + "//option/.");
                    if (currentSeason != null)
                    {
                        if (currentSeason.Attributes["selected"] != null &&
                            !string.IsNullOrEmpty(currentSeason.Attributes["selected"].Value))
                        {
                            currLeague = leagueName;
                        }
                        var seasonId = currentSeason.GetAttributeValue("value", "");
                        if (!string.IsNullOrEmpty(seasonId))
                        {
                            if (currentSeason.NextSibling != null)
                            {
                                var seasonName = HttpUtility.HtmlDecode(currentSeason.NextSibling.InnerText);
                                if (!string.IsNullOrWhiteSpace(seasonName))
                                {
                                    dic.Add(leagueName, new KeyValuePair<string, string>(seasonId, seasonName));
                                }
                            }
                        }
                    }
                }
            }

            return dic;
        }

        public static Dictionary<string, string> ParseSelect(HtmlNodeCollection nodes, bool hasGroups = false, string key = "value")
        {
            var dic = new Dictionary<string, string>();

            if (nodes == null)
            {
                return null;
            }

            foreach (var node in nodes)
            {
                var value = node.GetAttributeValue(key, "");

                if (!string.IsNullOrEmpty(value))
                {
                    var inner = HttpUtility.HtmlDecode(node.InnerText);
                    dic.Add(value, inner);
                }
            }

            return dic;
        }

        public static CompetitionData ParseCompetition(HtmlNode node, string countryName)
        {
            try
            {
                var rawTitle = node.SelectSingleNode(node.XPath + "//a[contains(@class,'competition')]/.");
                if (rawTitle != null)
                {
                    var rawId = node.GetAttributeValue("data-competition_id", "");
                    var comp = new CompetitionData
                    {
                        Name = HttpUtility.HtmlDecode(rawTitle.InnerText),
                        ID = ParsePositiveNumber(rawId),
                        Country = countryName
                    };

                    return comp;
                }
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }

            return null;
        }

        public static CompetitionData ParseCompetitor(HtmlNode node, string countryName)
        {
            try
            {
                var rawTitle = node.SelectSingleNode(node.XPath + "//a/.");
                if (rawTitle != null)
                {
                    var rawId = node.GetAttributeValue("data-competition_id", "");
                    var comp = new CompetitionData
                    {
                        Name = HttpUtility.HtmlDecode(rawTitle.InnerText),
                        ID = ParsePositiveNumber(rawId),
                        Country = countryName
                    };

                    return comp;
                }
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }

            return null;
        }

        public static int ParsePositiveNumber(string strToParse, string pattren = @"(?<num>\d+)", string propertyName = "num")
        {
            var id = -1;

            // pattren to parse id from attribute value
            try
            {
                var rgxPlayerID = new Regex(pattren, RegexOptions.IgnoreCase | RegexOptions.Singleline, TimeSpan.FromSeconds(10));
                var playerIDRaw = rgxPlayerID.Match(strToParse);

                if (playerIDRaw.Success)
                {
                    if (!int.TryParse(playerIDRaw.Groups[propertyName].Value.Trim(), out id))
                    {
                        id = -1;
                    }
                }
                else
                {
                    id = -1;
                }
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }

            return id;
        }

        public static int ParsePosition(string strPosition)
        {
            switch (strPosition.ToLowerInvariant())
            {
                case "midfielder":
                    return (int)ESoccerPlayerPositions.Midfield;
                case "attacker":
                    return (int)ESoccerPlayerPositions.Striker;
                case "defender":
                    return (int)ESoccerPlayerPositions.Defense;
                case "goalkeeper":
                    return (int)ESoccerPlayerPositions.GoalKeeper;
                default:
                    return -1;
            }
        }
       

        public static double ParseGameTime(string rawTime)
        {
            double time = -1;

            if (rawTime != null)
            {
                var splited = rawTime.Split('+');

                if (splited.Length >= 1)
                {
                    var strTime = splited[0].Replace("\'", "").Trim();
                    if (!double.TryParse(strTime, out time))
                    {
                        time = -1;
                    }
                }

            }
            else
            {
                time = -1;
            }

            return Convert.ToDouble(time);

        }

        public static string ParseValue(HtmlNode node, string path)
        {
            var rawValue = node.SelectSingleNode(path);

            string value = null;

            if (rawValue != null)
            {
                value = HttpUtility.HtmlDecode(rawValue.InnerText);
            }

            return value;
        }
    }
}
