
namespace _365Players.Scanners.Models
{
    public class CompetitionData
    {
        public CompetitionData()
        {

        }

        public CompetitionData(CompetitionData competitionData)
        {
            ID = competitionData.ID;
            Name = competitionData.Name;
            Country = competitionData.Country;
            Loaded = competitionData.Loaded;
        }
        public int ID { get; set; }
        public string Name { get; set; }
        public string Country { get; set; }
        public bool Loaded { get; set; }

        public override string ToString()
        {
            return string.Format("#{0}. {1}: {2}", ID, Country, Name);
        }
    }
}
