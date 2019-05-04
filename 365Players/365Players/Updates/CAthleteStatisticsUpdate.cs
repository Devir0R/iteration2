using _365Players.Updates;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _365Players
{
    public class CAthleteStatisticsUpdate
    {
        public CAthleteStatisticsUpdate()
        {
            Stats = new List<CPlayerIndividualStat>();
        }

        public CAthleteStatisticsUpdate(IList<CPlayerIndividualStat> stats) : this()
        {
            Stats = stats;
        }

        public ParsableValue<int> Country { get; set; }

        public ParsableValue<string> Competition { get; set; }

        public ParsableValue<int> Season { get; set; }

        public IList<CPlayerIndividualStat> Stats { get; set; }

        public void AddStat(int StatType, string val)
        {
            if (Stats == null || Stats.IsReadOnly)
            {
                Stats = new List<CPlayerIndividualStat>(Stats);
            }
            Stats.Add(new CPlayerIndividualStat() { StatisticType = StatType, Value = val });
        }
    }
}
