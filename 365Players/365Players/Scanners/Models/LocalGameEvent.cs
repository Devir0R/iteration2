using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static _365Players.Enums.SoccerEnums;

namespace _365Players.Scanners.Models
{
    public class LocalGameEvent
    {

        public ESoccerEventTypes EventType { get; set; }
        //public ESoccerGoalEventSubType GoalSubType { get; set; }
        public int GameTime { get; set; }
        public PlayerData PlayerData { get; set; }
        public int GoalSeq { get; set; }
        public PlayerData AssistPlayerData { get; set; }
    }
}
