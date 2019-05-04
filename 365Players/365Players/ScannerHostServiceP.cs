using _365Players.Scanners;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Configuration;

namespace _365Players
{
    public class ScannerHostServiceP
    {
        public List<Scanner> scanners = GetScanners();

        static void Main(string[] args)
        {
            var scanners = GetScanners();
            if (scanners?.Count > 0)
            {
                foreach (var scanner in scanners)
                {
                    scanner.Start();
                }
            }
        }

        public static List<Scanner> GetScanners()
        {
            List<Scanner> scanners = new List<Scanner>();
            scanners.Add(new SoccerWayScanner());
            /*
            if (CheckIfRunScanner("SoccerStatsScanner"))
            {
                scanners.Add(new SoccerStatsScanner());
            }*/

            return scanners;
        }

        public static bool CheckIfRunScanner(string name)
        {
            /*
            bool bAnswer = false;

            Boolean.TryParse(ConfigurationManager.AppSettings[name], out bAnswer);

            return bAnswer;
            */
            return true;
        }
    }
}
