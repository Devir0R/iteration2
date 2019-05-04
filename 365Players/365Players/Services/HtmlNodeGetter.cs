using System;
using HtmlAgilityPack;

namespace _365Players.Services
{
    public class HtmlNodeGetter
    {
        public HtmlNode GetHtmlNode(HtmlNode htmlNode, string nodeXpath)
        {
            HtmlNode node = null;
            try
            {
                node = htmlNode.SelectSingleNode(nodeXpath);
            }
            catch (Exception ex)
            {
               // string errorMessage = string.Format("{0} - Exception: {1}", MethodTrace.GetCurrentMethod(), ex);

                //Logging.Log(ELogCategory.ScannerExceptions, errorMessage);
            }

            return node;
        }

        public HtmlNodeCollection GetHtmlNodeCollection(HtmlNode htmlNode, string nodesXpath)
        {
            HtmlNodeCollection nodes = null;
            try
            {
                nodes = htmlNode.SelectNodes(nodesXpath);
            }
            catch (Exception ex)
            {
              //  string errorMessage = string.Format("{0} - Exception: {1}", MethodTrace.GetCurrentMethod(), ex);

            //    Logging.Log(ELogCategory.ScannerExceptions, errorMessage);
            }

            return nodes;
        }
    }
}
