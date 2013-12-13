using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RTSScreenWPF
{
    class DemoJSON
    {
        public string title { get; set; }
        public string description { get; set; }
        public string image { get; set; }
        public string type { get; set; }
        public Dictionary<string, object> options { get; set; }
        public int duration { get; set; }
        public int port { get; set; }
    }
}
