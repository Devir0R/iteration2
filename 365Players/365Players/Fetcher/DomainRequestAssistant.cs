using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace _365Players.Fetcher
{
    public class DomainRequestAssistant
    {
        public ManualResetEvent ResetEvent { get; set; }

        public DateTime UpdatedTime { get; set; }

        public long FrequentTotalRequestCounter { get; set; }

        public long TotalRequestCounter { get; set; }

        public DomainRequestAssistant()
        {
            ResetEvent = new ManualResetEvent(true);

            UpdatedTime = DateTime.UtcNow;
        }
    }
}
