using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _365Players.Updates
{
    public abstract class CGameUpdate 
    {
        #region Consts

        public const string NO_COUNTRY = "$NO_CNT";
        public const int NO_POSSESSION_COMPETITOR = -1;
        public const int NO_WINNER = -1;

        #endregion

        #region Members

        private List<CPrimitiveCompetitorInGame> m_Competitors;

        private string _gameKey = "";

        #endregion


        /// <summary>
        /// The game's competition's country
        /// </summary>
        
        public ParsableValue<int> Country { get; private set; }


        /// <summary>
        /// The game's competition
        /// </summary>
        
        public ParsableValue<long> Competition { get; private set; }

        /// <summary>
        /// Optional Season Name to be parsed
        /// </summary>
        
        public ParsableValue<int> Season { get; private set; }

        /// <summary>
        /// Optional Stage Name to be parsed
        /// </summary>
        
        public ParsableValue<int> Stage { get; private set; }

        
        public DateTime StartTime { get; set; }

        /// <summary>
        /// The game time
        /// </summary>
       
        public double GameTime { get; set; }

        
        public float? AddedTime { get; set; }

        /// <summary>
        /// the status of the game
        /// </summary>
        
        public int Status { get; set; }


        /// <summary>
        /// the index of the home competitor
        /// </summary>
        
        public int HomeCompetitor { get; protected set; }

        
        public bool CreateGame { get; set; }

        
        public bool NotForAutoDelete { get; set; }

        
        public bool IgnoreStatusvalidation { get; set; }

        
        public bool CreateNotifications { get; set; }

        
        public bool AllowEventDeletion { get; set; }

        
        public int CompetitorInPossession { get; set; }

        

        public int? Round { get; set; }

        
        public int? Winner { get; set; }

        
        public int? ToQualify { get; set; }

        
        public bool BlockScoreFix { get; set; }
        
        public bool BlockStarttimeUpdate { get; set; }

        public abstract int NumberOfStages { get; }


        public virtual Type StatisticsTypesEnum
        {
            get { return null; }
        }

        public virtual Type AthleteStatisticsTypesEnum
        {
            get { return null; }
        }

        public long ParsedToGame { get; set; }

        public CGameUpdate ConnectedGame { get; set; }

        /// <summary>
        /// a list of competitors in the game
        /// </summary>
        public IList<CPrimitiveCompetitorInGame> Competitors
        {
            get { return m_Competitors?.AsReadOnly(); }
        }

        public CPrimitiveCompetitorInGame GetCompetitorByCompetitorNum(int competitorNum) =>
            m_Competitors.FirstOrDefault(comp => comp.CompetitorNum == competitorNum);

        public int GetGameCompetitorNum(int competitorNum) =>
            GetCompetitorByCompetitorNum(competitorNum)?.GameCompetitorNum ?? competitorNum;

        public int GetCompetitorsOffset() => Competitors?.Count > 0
            ? 1 - Competitors.Min(comp => comp.CompetitorNum)
            : 0;

        public IList<long> CompetitorsParsedIDs
        {
            get
            {
                long[] alngAnswer = new long[Competitors.Count];
                for (int nCounter = 0; nCounter < alngAnswer.Length; nCounter++)
                {
                    alngAnswer[nCounter] = -1;
                    if (Competitors[nCounter].Name.WasParsed)
                    {
                        alngAnswer[nCounter] = Competitors[nCounter].Name.ParsedValue;
                    }
                }
                return alngAnswer;
            }
        }
        

        #region Methods

        /// <summary>
        /// Add a competitor to the game update
        /// </summary>
        /// <returns></returns>
        public virtual CPrimitiveCompetitorInGame AddCompetitor()
        {
            CPrimitiveCompetitorInGame answer = new CPrimitiveCompetitorInGame(NumberOfStages, m_Competitors.Count + 1);
            m_Competitors.Add(answer);

            return answer;
        }
        
        

        public virtual void SetHomeCompetitor(int homeCompetitorNum)
        {
            HomeCompetitor = homeCompetitorNum;
        }

        #endregion

        #region Factory
        public static CGameUpdate CreateUpdate(string DataSource)
        {
            CGameUpdate answer = new CSoccerGameUpdate(); 
            return answer;
        }

        public void ClearUnParsedCompetitors()
        {
            m_Competitors = m_Competitors.Where(comp => comp.Name.WasParsed).ToList();
        }
        #endregion

        public virtual void ConnectSportTypedCompetitors()
        {
        }
    }
}
