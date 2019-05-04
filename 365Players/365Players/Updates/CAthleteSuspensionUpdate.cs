using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _365Players
{
    public class CAthleteSuspensionUpdate
    {
        public CAthleteSuspensionUpdate()
        {
            Active = true;
        }

        public int SuspensionType { get; set; }
        public string Country { get; set; }
        public string Competition { get; set; }
        public DateTime StartDate { get; set; }
        public int GamesCount { get; set; }
        public int GamesBannedLeft { get; set; }
        public bool Active { get; set; }
    }
}
