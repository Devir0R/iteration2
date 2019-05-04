
namespace _365Players.Scanners.Models
{
    public class CompetitorData
    {
        public CompetitorData()
        {

        }

        public CompetitorData(CompetitorData comp)
        {
            Id = comp.Id;
            Name = comp.Name;
            Link = comp.Link;
            CompetitionData = new CompetitionData(comp.CompetitionData);
        }

        public int Id { get; set; }
        public string Name { get; set; }
        public CompetitionData CompetitionData { get; set; }
        public string Link { get; set; }
        //public ScanContext LastScaned { get; set; }
        //public ScanContext NextScan { get; set; }
        public int TotalCareerScanCounter { get; set; }

        public ScanContext LastScaned { get; set; }
        public ScanContext NextScan { get; set; }

        public override string ToString()
        {
            return string.Format("{0}-{1}: {2}", CompetitionData?.Country, CompetitionData?.Name, Name);
        }
    }
}