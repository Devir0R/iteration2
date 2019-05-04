using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _365Players.Enums
{
    public class PlayerEnums
    {
        public enum EPlayerLineupStatus
        {
            None,
            Starting,
            Substitute,
            Injured,
            Management,
            Suspended
        }

        public enum ESoccerPlayerPositions
        {
            Management = 0,
            GoalKeeper = 1,
            Defense,
            Midfield,
            Striker
        }

        public enum ESoccerPlayerFormationPositions
        {
            GK = 1,
            RD,
            LB,
            SW,
            CB,
            RB,
            LM,
            CM,
            AM,
            RM,
            LF,
            CF,
            SS,
            RF,
            Manager,
            Coach,
            AssistCoach
        }

        public enum ESoccerPlayerStatistics
        {
            Goals = 1,
            Assists,
            YellowCards,
            RedCards,
            Appearences,
            Lineups,
            Substitutions,
            TimePlayed,
            MinutesPerGoal
        }

        public enum EPlayerSubstituteType
        {
            None,
            SubstituteIn,
            SubstituteOut
        }
    }
}
