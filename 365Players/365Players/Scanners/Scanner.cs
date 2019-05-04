using _365Players.Diagnostic;
using System;
using System.Collections.Generic;

using System.Threading;


namespace _365Players.Scanners
{
    public abstract class Scanner: IAsyncHandlingSupport<ICollection<CPlayerUpdate>>
    {
        public CAsyncUpdatesQueue<ICollection<CPlayerUpdate>> UpdatesQueue { get; protected set; }
        public const string SCANNER_EXCEPTION_POLICY = "Scanners";
        abstract public string Name { get; }
        public virtual ScannerStatus Status { get; private set; }
        private object SyncRoot { get; set; }
        public string Server { get; set; }

        public bool AsyncHandle { get; set; }


        public Scanner(bool async = false, int NumberOfAsyncHandlers = 1)
        {
            SyncRoot = new object();
            AsyncHandle = async;
            if (AsyncHandle)
            {
                UpdatesQueue = new CAsyncUpdatesQueue<ICollection<CPlayerUpdate>>(this.GetType().Name, true, NumberOfAsyncHandlers);
                UpdatesQueue.UpdateToHandle += UpdatesQueue_UpdateToHandle;
            }
        }
        void UpdatesQueue_UpdateToHandle(object sender, UpdateHandleEventArgs<ICollection<CPlayerUpdate>> e)
        {
        }

        public abstract void Start();
        public abstract void Stop();

        public abstract bool Scan();

        /// <summary>
        /// Scan the entities in the time range, one time
        /// </summary>
        /// <param name="start"></param>
        /// <param name="end"></param>
        /// <returns></returns>
        public abstract bool Scan(DateTime start, DateTime end);

        public virtual bool Scan(DateTime start, DateTime end, TimeSpan waitBetweenDays)
        {
            bool bAnswer = true;

            try
            {
                while (start.Date <= end.Date)
                {
                    bAnswer = Scan(start, start) && bAnswer;

                    Thread.Sleep(waitBetweenDays);

                    start = start.AddDays(1);
                }
            }
            catch (Exception e)
            {
               
            }

            return bAnswer;
        }

        public virtual bool IsActive { get { return true; } }

        public bool HasErrors { get; private set; }

        public ICollection<string> Errors { get; private set; }

        protected virtual void SetHasErrors(bool hasErrors)
        {
            this.HasErrors = hasErrors;
            if (!hasErrors)
            {
                Errors = null;
            }
        }

        public virtual void RaiseEvent(ICollection<CPlayerUpdate> Updates)
        {
            
        }
        
    }

    public enum ScannerStatus
    {
        Idle,
        Running,
        ScanningOnce
    }

}
