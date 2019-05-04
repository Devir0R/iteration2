using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _365Players.Enums
{
    public class SoccerEnums
    {
        public enum ESoccerEventTypes
        {
            Goal = 0,
            YellowCard,
            RedCard,
            MissedPenalty
        }

        public enum ESoccerStages
        {
            CurrResult,
            HalfTime,
            End90Minutes,
            ExtraTime,
            Penalty
        }

    }
}
