using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _365Players.Updates
{
    [Serializable]
    public class ParsableValue<TParse>
    {
        private string m_Value;

        public ParsableValue()
        {
            Value = string.Empty;
        }
        public ParsableValue(string value)
        {
            this.Value = value;
        }
        public ParsableValue(TParse value)
        {
            this.Value = value.ToString();
            Parse(value);
        }

        public string Value
        {
            get { return m_Value; }
            set
            {
                m_Value = value;
                WasParsed = false;
                ParsedValue = default(TParse);
            }
        }
        public TParse ParsedValue { get; private set; }
        public bool WasParsed { get; private set; }

        public override string ToString()
        {
            return Value.ToString();
        }

        public void Parse(TParse parse)
        {
            ParsedValue = parse;
            WasParsed = true;
        }

        public void ClearParsedValue()
        {
            ParsedValue = default(TParse);
            WasParsed = false;
        }
    }
}
