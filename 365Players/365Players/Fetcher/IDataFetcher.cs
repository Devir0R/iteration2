using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _365Players.Fetcher
{
    public interface IDataFetcher : IDisposable
    {
        Task<FetchResult> FetchDom(Uri uri, bool isJsonContainer = false, bool returnNull = false);

        Task<FetchResult> FetchDom(string url, bool isJsonContainer = false, bool returnNull = false);

        void Close(string url);

    }
}
