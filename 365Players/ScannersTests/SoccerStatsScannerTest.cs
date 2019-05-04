using System;
using System.Collections.Generic;
using _365Players;
using _365Players.Scanners;
using _365Players.Enums;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace ScannersTests
{
    [TestClass]
    public class SoccerStatsScannerTest
    {
        [TestMethod]
        public void PlayerUpdateTest()
        {
            var scanner = new SoccerStatsScanner();
            List<CPlayerUpdate> playersUpdates = scanner.ScanPlayersList();

            Assert.IsNotNull(playersUpdates);
            Assert.AreEqual(1, playersUpdates.Count);
            //Assert.AreEqual(2, playersUpdates[0].Competitions?.Count);
            //Assert.AreEqual("Israeli Premier League", playersUpdates[0].Competitions[0]);
            //Assert.AreEqual("Israeli FA Cup", playersUpdates[0].Competitions[1]);
            //Assert.AreEqual(2, playersUpdates[0].Competitors?.Count);
            //Assert.AreEqual("Hapoel Beer Sheva", playersUpdates[0].Competitors[0]);
            //Assert.AreEqual("Israel", playersUpdates[0].Competitors[1]);
            Assert.AreEqual("Israel", playersUpdates[0].Country);
            Assert.AreEqual("TestDataSource", playersUpdates[0].DataSource);
            Assert.AreEqual(new DateTime(1984,10,30), playersUpdates[0].DOB);
            Assert.AreEqual((int)PlayerEnums.ESoccerPlayerFormationPositions.AM, playersUpdates[0].FormationPosition);
            Assert.AreEqual(1.75, playersUpdates[0].Height);
            Assert.AreEqual(70, playersUpdates[0].Weight);
            Assert.AreEqual("Maor Melikson", playersUpdates[0].Name);
            //Assert.AreEqual(2, playersUpdates[0].Nationality.Count);
            Assert.AreEqual("Poland", playersUpdates[0].Nationality[0]);
            Assert.AreEqual("Israel", playersUpdates[0].Nationality[1]);
            Assert.AreEqual((int)PlayerEnums.ESoccerPlayerPositions.Midfield, playersUpdates[0].Position);
            Assert.AreEqual(1, playersUpdates[0].Statistics.Count);
            Assert.AreEqual(1, playersUpdates[0].Statistics[0].Stats.Count);
            Assert.AreEqual((int)PlayerEnums.ESoccerPlayerStatistics.Goals, playersUpdates[0].Statistics[0].Stats[0].StatisticType);
            Assert.AreEqual("1", playersUpdates[0].Statistics[0].Stats[0].Value);
            Assert.AreEqual(24, playersUpdates[0].JerseyNum);
        }
    }
}
