using System;
using System.Collections.Generic;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace RTSScreenWPF
{
	public partial class ConsoleDemo : UDPDemo
	{
        public ConsoleDemo(string title, string description, string imageName, int duration, int port) : base(title, description, imageName, duration, port)
		{
			this.InitializeComponent();
		}

        protected override void DataReceived(byte[] data)
        {
            if (data[0] == 0xFF && data[1] == 0xFF)
            {
                Dispatcher.Invoke((Action)delegate()
                {
                    console.Text = Encoding.GetEncoding(437).GetString(data, 2, data.Length - 2);
                });
            }
        }
	}
}