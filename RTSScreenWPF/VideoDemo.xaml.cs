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
	public partial class VideoDemo : Demo
	{
        private string videoId;
        private string videoUri;

        public override bool Active
        {
            get { return base.Active; }
            set
            {
                base.Active = value;

                if (value)
                {
                    if (browser.IsBrowserInitialized)
                        browser.Load(videoUri);
                    else
                        browser.Address = videoUri;
                }
                else
                {
                    if (browser.IsBrowserInitialized)
                        browser.Load("about:blank");
                    else
                        browser.Address = "about:blank";
                }
            }
        }

		public VideoDemo(string title, string description, string imageName, int duration, string id) : base(title, description, imageName, duration)
		{
			this.InitializeComponent();

            this.videoId = id;
            videoUri = String.Format("http://www.youtube.com/embed/{0}?playlist={0}&rel=0&controls=0&disablekb=1&hd=1&showinfo=0&modestbranding=1&loop=1&fs=0&enablejsapi=1&playerapiid=player1&autoplay=1", videoId);
            
            // Force HD playback
            browser.LoadCompleted += new CefSharp.LoadCompletedEventHandler(browser_LoadCompleted);

            browser.Cursor = Cursors.None;
            browser.IsEnabled = false;
		}
        
        void browser_LoadCompleted(object sender, CefSharp.LoadCompletedEventArgs url)
        {
            /*
            try
            {
                if (url.Url != "about:blank")
                {
                    browser.ExecuteScript("function onChange() {player1.setPlaybackQuality('hd1080');player1.setPlaybackQuality('hd1080');player1.setPlaybackQuality('hd1080');player1.setPlaybackQuality('hd1080');}setTimeout(function(){player1.addEventListener('onStateChange', 'onChange');onChange();}, 1000);");
                }
            }
            catch (Exception e)
            {
                StringBuilder sb = new StringBuilder();
                sb.AppendLine("============================================");
                sb.AppendLine(DateTime.Now.ToShortDateString() + " " + DateTime.Now.ToLongTimeString());
                sb.AppendLine("============================================");
                if (e != null)
                {
                    sb.AppendLine(e.Message);
                    sb.AppendLine(e.StackTrace);
                    sb.AppendLine();
                }
                if (e.InnerException != null)
                {
                    sb.AppendLine(e.InnerException.Message);
                    sb.AppendLine(e.InnerException.StackTrace);
                    sb.AppendLine();
                }
                if (e.InnerException.InnerException != null)
                {
                    sb.AppendLine(e.InnerException.InnerException.Message);
                    sb.AppendLine(e.InnerException.InnerException.StackTrace);
                    sb.AppendLine();
                }
                File.AppendAllText("log.txt", sb.ToString());
            }
            */
        }
	}
}