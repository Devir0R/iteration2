using _365Players.Updates;
using System;


namespace _365Players.Scanners.Models
{
    public class ScanMetaData
    {
        public CSoccerGameUpdate Update { get; set; }
        public DateTime LastScan { get; set; }
        public int AfteFinishedCount { get; set; }
        public string JsonGameRequestCode { get; set; }
    }
}
