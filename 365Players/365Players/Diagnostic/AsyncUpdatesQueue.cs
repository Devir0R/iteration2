using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using System.Threading.Tasks.Dataflow;


namespace _365Players.Diagnostic
{
    public interface IAsyncHandlingSupport<TUpdate>
    {
        CAsyncUpdatesQueue<TUpdate> UpdatesQueue { get; }
        bool AsyncHandle { get; set; }
    }

    public class CAsyncUpdatesQueue<TUpdate> : IDisposable
    {
        private string OwnerName { get; set; }
        public int MaxNumberOfConcurrentHandlers { get; set; }
        public event EventHandler<UpdateHandleEventArgs<TUpdate>> UpdateToHandle;

        public TaskCreationOptions TaskCreationType { get; set; }

        private IPropagatorBlock<TUpdate, TUpdate> Updates { get; set; }
        public IList<ActionBlock<TUpdate>> Consumers { get; set; }
        public CancellationTokenSource TaskCancallation { get; set; }
        private Task lastMonitoringTask { get; set; }
        public DateTime LastTimeHandled { get; set; }
        public DateTime LastTimeAdded { get; set; }
        public bool AutoReset { get; set; }
        public int AutoResetThreshold { get; set; }

        private long m_TotalUpdatesAdded = 0;
        private long m_TotalUpdatesHandled = 0;
        private int m_activeWorkers = 0;
        private int m_WorkersInProccessing = 0;
        private int m_AutoResetCount = 0;
        private long m_AutoResetUpdatesLost = 0;

        public int ActiveWorkers
        {
            get { return m_activeWorkers; }
            set { m_activeWorkers = value; }
        }

        public long TotalUpdatesAdded { get { return m_TotalUpdatesAdded; } }
        public long TotalUpdatesHandled { get { return m_TotalUpdatesHandled; } }
        public long AutoResetCount { get { return m_AutoResetCount; } }
        public long AutoResetUpdatesLost { get { return m_AutoResetUpdatesLost; } }

        public CAsyncUpdatesQueue(string name, bool start = true, int MaxConcurrentHandlers = 1)
        {
            MaxNumberOfConcurrentHandlers = MaxConcurrentHandlers;
            TaskCancallation = new CancellationTokenSource();
            Updates = InitBufferBlock(MaxConcurrentHandlers, out var consumers);
            this.Consumers = consumers;
            AutoReset = false;
            AutoResetThreshold = -1;

            OwnerName = name;
            TaskCreationType = TaskCreationOptions.None;

        }

        private IPropagatorBlock<TUpdate, TUpdate> InitBufferBlock(int maxConcurrentHandlers, out IList<ActionBlock<TUpdate>> consumers)
        {
            var queue = new BufferBlock<TUpdate>();
            InitializeConsumers(queue, maxConcurrentHandlers, out consumers);
            return queue;
        }

        private void InitializeConsumers(BufferBlock<TUpdate> queue, int maxConsumers, out IList<ActionBlock<TUpdate>> consumerBlocks)
        {
            var consumerOptions = new ExecutionDataflowBlockOptions { BoundedCapacity = 1 };
            if (maxConsumers <= 0)
            {
                maxConsumers = 1;
            }
            consumerBlocks = new List<ActionBlock<TUpdate>>(maxConsumers);
            for (int i = 0; i < maxConsumers; i++)
            {
                var consumer = new ActionBlock<TUpdate>(new Action<TUpdate>(ConsumeQueueItem), consumerOptions);
                queue.LinkTo(consumer);
                consumerBlocks.Add(consumer);
            }
        }

        public long Count
        {
            get
            {
                int nAnswer = 0;
                if (Updates is BufferBlock<TUpdate> updatesBuffer)
                {
                    nAnswer = updatesBuffer.Count;
                }
                return nAnswer;
            }
        }

        public void Start()
        {
            int nWorkersToAdd = MaxNumberOfConcurrentHandlers - m_activeWorkers;
            if (nWorkersToAdd > 0) 
            {
                if (nWorkersToAdd <= 0)
                {
                    nWorkersToAdd = 1;
                }

                for (int i = 0; i < nWorkersToAdd; i++)
                {
                    try
                    {
                        bool bSucceed = false;
                        int nTries = 3;

                        while (!bSucceed && nTries-- > 0)
                        {
                            var task = MonitorQueue();

                            if (task != null)
                            {
                                bSucceed = true;
                            }
                        }

                        if (!bSucceed)
                        {
                        }
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
        }

        public void Stop()
        {
            try
            {
                if (Updates != null)
                {
                    Updates.Complete();
                    if (Consumers != null)
                    {
                        foreach (var consumer in Consumers)
                        {
                            consumer.Complete();
                        }
                    }
                    m_activeWorkers = 0;
                }
            }
            catch { }
        }

        public async Task<bool> Enqueue(TUpdate update)
        {
            bool bAnswer = false;

            if (AutoReset && AutoResetThreshold > 0)
            {
                if (Count > AutoResetThreshold)
                {
                    Interlocked.Increment(ref m_AutoResetCount);
                    Interlocked.Add(ref m_AutoResetUpdatesLost, Count);
                    ClearQueue();
                }
            }

            if (Updates != null)
            {
                bAnswer = await Updates.SendAsync(update);
                LastTimeAdded = DateTime.UtcNow;
                Interlocked.Increment(ref m_TotalUpdatesAdded);
            }

            return bAnswer;
        }

        private async Task MonitorQueue()
        {
            try
            {
                var updates = Updates;
                Interlocked.Increment(ref m_activeWorkers);

                while (await updates.OutputAvailableAsync())
                {
                    try
                    {
                        TUpdate update = await updates.ReceiveAsync();
                        LastTimeHandled = DateTime.UtcNow;
                        ConsumeQueueItem(update);
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
            catch (Exception e)
            {
            }
        }

        private void ConsumeQueueItem(TUpdate update)
        {
            if (update != null)
            {
                if (UpdateToHandle != null)
                {
                    try
                    {
                        UpdateToHandle(this, new UpdateHandleEventArgs<TUpdate>(update));
                    }
                    catch (Exception e)
                    {
                    }
                }
                Interlocked.Increment(ref m_TotalUpdatesHandled);
            }
        }

        #region IDisposable Members

        public void Dispose()
        {
            Stop();
        }

        #endregion

        public int ActiveHandlers { get { return ActiveWorkers; } }

        public bool ClearQueue()
        {
            bool bAnswer = false;
            try
            {
                Stop();
                Updates = InitBufferBlock(MaxNumberOfConcurrentHandlers, out var consumers);
                this.Consumers = consumers;
                bAnswer = true;
            }
            catch { }
            return bAnswer;
        }
    }

    public class UpdateHandleEventArgs<TUpdate> : EventArgs
    {
        public TUpdate Update { get; set; }
        public UpdateHandleEventArgs(TUpdate update)
        {
            Update = update;
        }
    }
}
