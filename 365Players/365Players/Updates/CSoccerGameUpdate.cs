using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _365Players.Updates
{
    public enum ESoccerStages
    {
        CurrResult,
        HalfTime,
        End90Minutes,
        ExtraTime,
        Penalty
    }

    public class CSoccerGameUpdate : CGameUpdate
    {

        public CSportTypedCompetitorInGame<ESoccerStages> HomeTeam { get; private set; }

        public CSportTypedCompetitorInGame<ESoccerStages> AwayTeam { get; private set; }


        public CSoccerGameUpdate()
            : base()
        {
            HomeTeam = new CSportTypedCompetitorInGame<ESoccerStages>(AddCompetitor());
            HomeCompetitor = HomeTeam.Competitor.CompetitorNum;
            AwayTeam = new CSportTypedCompetitorInGame<ESoccerStages>(AddCompetitor());
        }

        public override void ConnectSportTypedCompetitors()
        {
            base.ConnectSportTypedCompetitors();
            HomeTeam = HomeTeam ?? new CSportTypedCompetitorInGame<ESoccerStages>(Competitors.FirstOrDefault());
            AwayTeam = AwayTeam ?? new CSportTypedCompetitorInGame<ESoccerStages>(Competitors.LastOrDefault());
        }

        public override int NumberOfStages
        {
            get { return Enum.GetValues(typeof(ESoccerStages)).Length; }
        }

    }
}
