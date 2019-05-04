using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Runtime.Serialization;

namespace _365Players.Diagnostic
{
    
    public class COperationStatistics
    {
        
        long m_nExecutionCount;
        
        long m_nTotalTime;
        private object SyncRoot { get; set; }
        
        public TimeSpan LastExecutionTime { get; private set; }
        public TimeSpan MinExecutionTime { get; private set; }
        public TimeSpan MaxExecutionTime { get; private set; }
        public COperationStatistics SinceReset { get; set; }
        public DateTime ResetTime { get; set; }

        public COperationStatistics()
        {
            m_nExecutionCount = 0;
            m_nTotalTime = 0;
            LastExecutionTime = TimeSpan.Zero;
            MinExecutionTime = TimeSpan.Zero;
            MaxExecutionTime = TimeSpan.Zero;
            SyncRoot = new object();
            ResetTime = DateTime.UtcNow;
        }

        public void Reset()
        {
            SinceReset = new COperationStatistics();
        }

        public long ExecutionCount { get { return m_nExecutionCount; } }
        public long TotalTime { get { return m_nTotalTime; } }
        public TimeSpan AvarageExecutionTime
        {
            get
            {
                if (ExecutionCount != 0)
                {
                    double avg = (TotalTime * 1.0) / ExecutionCount;
                    return TimeSpan.FromTicks((long)avg);
                }

                return TimeSpan.Zero;
            }
        }

        public void AddExecution(TimeSpan executionTime)
        {
            Interlocked.Increment(ref m_nExecutionCount);
            Interlocked.Add(ref m_nTotalTime, executionTime.Ticks);
            LastExecutionTime = executionTime;
            if (MinExecutionTime == TimeSpan.Zero || executionTime < MinExecutionTime)
            {
                MinExecutionTime = executionTime;
            }
            if (MaxExecutionTime == TimeSpan.Zero || executionTime > MaxExecutionTime)
            {
                MaxExecutionTime = executionTime;
            }
            if (SinceReset != null)
            {
                SinceReset.AddExecution(executionTime);
            }
        }
    }
}
