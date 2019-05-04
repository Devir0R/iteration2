using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static _365Players.Enums.PlayerEnums;

namespace _365Players.Updates
{
    public class CPrimitiveCompetitorInGame
    {

        public ParsableValue<long> Name { get; private set; }
        
        public IList<double> Scores { get; private set; }
        
        public IList<double> ExtraScores { get; private set; }
        
        public int GameCompetitorNum { get; set; }
        
        public int CompetitorNum { get; set; }
        
        public List<CPlayerUpdate> Lineup { get; private set; }
        
        public string Tag { get; set; }

        public CPrimitiveCompetitorInGame(int NumberOfStages, int nCompetitorNum)
        {
            Scores = new double[NumberOfStages];
            ExtraScores = new double[NumberOfStages];
            for (int i = 0; i < Scores.Count; i++)
            {
                Scores[i] = -1;
                ExtraScores[i] = -1;
            }
            Name = new ParsableValue<long>();
            CompetitorNum = nCompetitorNum;
            GameCompetitorNum = CompetitorNum;
            Lineup = new List<CPlayerUpdate>();
        }
    }

    [Serializable]
    public class CSportTypedCompetitorInGame<EStages>
    {
        public CPrimitiveCompetitorInGame Competitor { get; private set; }
        public double this[EStages stage]
        {
            get
            {
                double answer = default(double);
                if (Competitor != null)
                {
                    answer = Competitor.Scores[Convert.ToInt32(stage)];
                }
                return answer;
            }
            set
            {
                if (Competitor != null)
                {
                    Competitor.Scores[Convert.ToInt32(stage)] = value;
                }
            }
        }

        public string Name
        {
            get
            {
                string strAnswer = string.Empty;
                if (Competitor != null)
                {
                    strAnswer = Competitor.Name.Value;
                }
                return strAnswer;
            }
            set
            {
                if (Competitor != null)
                {
                    Competitor.Name.Value = value;
                }
            }
        }

        public CSportTypedCompetitorInGame(CPrimitiveCompetitorInGame competitor)
        {
            Competitor = competitor;
        }


        public void ResetScores()
        {
            if (typeof(EStages).IsEnum)
            {
                foreach (EStages eStage in Enum.GetValues(typeof(EStages)))
                {
                    this[eStage] = -1;
                }
            }
        }
    }
}
