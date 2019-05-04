using System;

namespace _365Players.Scanners.Models
{
    public class ScanContext
    {
        public DateTime Time { get; set; }

        public CompetitionData CompetitionData { get; set; }

        public ScanContext()
        {

        }

        public ScanContext(DateTime time)
        {
            Time = time;
        }

        public ScanContext(DateTime time, CompetitionData competitionData) : this(time)
        {
            CompetitionData = competitionData;
        }
    }
}
