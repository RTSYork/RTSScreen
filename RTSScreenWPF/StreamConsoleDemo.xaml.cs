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
using System.IO;

namespace RTSScreenWPF
{
	public partial class StreamConsoleDemo : UDPDemo
	{
        private readonly int streamWidth;
        private readonly int streamHeight;
        BitmapSource b;
        byte[] imageData;

        public StreamConsoleDemo(string title, string description, string imageName, int duration, int port, int width, int height) : base(title, description, imageName, duration, port)
		{
			this.InitializeComponent();

            this.streamWidth = width;
            this.streamHeight = height;

            imageData = new byte[streamWidth * streamHeight * 2];
		}

        protected override void DataReceived(byte[] data)
        {
            if (data[0] == 0xFF && data[1] == 0xFF)
            {
                Dispatcher.Invoke((Action)delegate() {
                    console.Text = Encoding.GetEncoding(437).GetString(data, 2, data.Length - 2);
                });
            }
            else
            {
                int y = ((data[1] << 8) | data[0]);

                for (int x = 0; x < (data.Length - 2) * 2; x+=2)
                {
                    if (y < streamHeight && x < streamWidth * 2)
                    {
                        imageData[x + (y * streamWidth * 2)] = data[x + 2];
                        imageData[x + 1 + (y * streamWidth * 2)] = data[x + 3];
                    }
                }

                if (y == streamHeight / 2 - 1)
                {
                    updateImage();
                }
            }
        }

        private void updateImage()
        {
            Dispatcher.Invoke((Action)delegate()
            {
                b = BitmapSource.Create(streamWidth, streamHeight, 96, 96, PixelFormats.Bgr565, null, imageData, streamWidth * 2);
                graphics.Source = b;
            });
        }
	}
}