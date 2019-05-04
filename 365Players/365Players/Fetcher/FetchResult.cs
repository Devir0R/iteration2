using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _365Players.Fetcher
{
    public class FetchResult
    {
        public bool UsedProxy { get; set; }

        public string RawPage { get; set; }

        public HtmlDocument HtmlDocument { get; set; }
    }
}
