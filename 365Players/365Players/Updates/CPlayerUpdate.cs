using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _365Players
{
    public class CPlayerUpdate
    {
        public CPlayerUpdate(string DataSource)
        {
            this.DataSource = DataSource;
            Statistics = new List<CAthleteStatisticsUpdate>();
        }

        /// <summary>
        /// The player's name
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// The competition's country
        /// </summary>
        public string Country { get; set; }

        /// <summary>
        /// The competition
        /// </summary>
        
        public string Competition { get; set; }

        /// <summary>
        /// the competitor
        /// </summary>
        public string Competitor { get; set; }

        /// <summary>
        /// Nationality
        /// </summary>
        public string Nationality { get; set; }

        /// <summary>
        /// JerseyNum
        /// </summary>
        public int JerseyNum { get; set; }

        /// <summary>
        /// Position
        /// </summary>
        public int Position { get; set; }

        /// <summary>
        /// Formation Position
        /// </summary>
        public int FormationPosition { get; set; }

        /// <summary>
        /// Date of birth
        /// </summary>
        public DateTime DOB { get; set; }

        public IList<CAthleteStatisticsUpdate> Statistics { get; set; }

        public CAthleteInjuryUpdate Injury { get; set; }
        public CAthleteSuspensionUpdate Suspension { get; set; }

        /// <summary>
        /// Athlete weight in grams
        /// </summary>
        public double? Weight { get; set; }

        /// <summary>
        /// Athlete height in centimeters
        /// </summary>
        public double? Height { get; set; }

        public string DataSource { get; set; }

        public string Message { get; set; }

        public override string ToString()
        {
            return string.Format("Athlete Update from {0} for {1} of {2} ({3}: {4})", DataSource, Name, Competitor, Country, Competition);
        }

        public CPlayerUpdate Copy()
        {
            var ath = new CPlayerUpdate(DataSource)
            {
                Competition =  Competition ,
                Competitor = Competitor,
                Country = Country,
                DOB = DOB,
                FormationPosition = FormationPosition,
                Position = Position,
                Nationality = Nationality,
                JerseyNum = JerseyNum,
                Name = Name,
                Weight = Weight,
                Height = Height
            };

            return ath;
        }

        public virtual DateTime WebRequestTime { get; set; }
        public virtual DateTime WebResponseTime { get; set; }
        public bool UsedProxy { get; set; }
    }
}
