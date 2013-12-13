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
using System.Windows.Media.Animation;
using System.Timers;
using Newtonsoft.Json;
using System.IO;
using System.Windows.Threading;

namespace RTSScreenWPF
{
	/// <summary>
	/// Interaction logic for MainWindow.xaml
	/// </summary>
	public partial class MainWindow : Window
	{
        List<Demo> demos;
        Storyboard fadeOut;
        Storyboard fadeIn;
        int currentDemo;
        int nextDemo;
        DispatcherTimer demoTimer;
        String oldJson = "";

		public MainWindow()
		{
			this.InitializeComponent();

            if (SystemParameters.PrimaryScreenWidth < (SystemParameters.VirtualScreenWidth / 2))
                this.Left = SystemParameters.PrimaryScreenWidth;

            fadeOut = (Storyboard)FindResource("fadeOut");
            fadeIn = (Storyboard)FindResource("fadeIn");

            demoTimer = new DispatcherTimer();
            demoTimer.Tick += new EventHandler(demoTimer_Tick);

            fadeOut.Completed += new EventHandler(fadeOut_Completed);

            demos = loadJSON();

            foreach (Demo d in demos)
                demoBoxes.Children.Add(d.DemoBox);

            currentDemo = 0;
            nextDemo = (currentDemo + 1) % demos.Count;
            setActiveDemo(0);
		}

        void demoTimer_Tick(object sender, EventArgs e)
        {
            rotateDemo();
        }

        private List<Demo> loadJSON()
        {
            List<Demo> newDemos = new List<Demo>();
            String jsonString = File.ReadAllText(Environment.CurrentDirectory + Path.DirectorySeparatorChar + "demos.json");

            if (jsonString != oldJson)
            {
                oldJson = jsonString;

                List<DemoJSON> demoJSONs = JsonConvert.DeserializeObject<List<DemoJSON>>(jsonString);

                foreach (DemoJSON demo in demoJSONs)
                {
                    Demo d = constructDemo(demo);
                    if (d != null)
                        newDemos.Add(d);
                }

                return newDemos;
            }
            else
                return null;
        }

        private Demo constructDemo(DemoJSON json)
        {
            if (json.type == "console")
            {
                return new ConsoleDemo(json.title, json.description, json.image, json.duration, json.port);
            }
            else if (json.type == "graphics")
            {
                return new StreamDemo(json.title, json.description, json.image, json.duration, json.port, (int)(long)json.options["graphics_width"], (int)(long)json.options["graphics_height"]);
            }
            else if (json.type == "graphics+console")
            {
                return new StreamConsoleDemo(json.title, json.description, json.image, json.duration, json.port, (int)(long)json.options["graphics_width"], (int)(long)json.options["graphics_height"]);
            }
            else if (json.type == "video")
            {
                return new VideoDemo(json.title, json.description, json.image, json.duration, (string)json.options["video_id"]);
            }

            return null;
        }

        private void rotateDemo()
        {
            demoTimer.Stop();

            foreach (Demo d in demos)
                d.Active = false;

            nextDemo = (currentDemo + 1) % demos.Count;
            fadeOut.Begin();
        }

        private void setActiveDemo(int number)
        {
            Demo demo = demos[number];
            demoArea.Children.Clear();
            demoArea.Children.Add(demo);
            demoTitle.Content = demo.Title;
            demoDescription.Text = demo.Description;
            demoImage.Source = demo.Image;
            demo.Active = true;

            demoTimer.Interval = new TimeSpan(0, 0, demo.Duration);
            demoTimer.Start();

            currentDemo = number;

            fadeIn.Begin();
        }

		private void Window_KeyUp(object sender, System.Windows.Input.KeyEventArgs e)
		{
            if ((e.KeyboardDevice.IsKeyDown(Key.LeftCtrl) || e.KeyboardDevice.IsKeyDown(Key.RightCtrl)) && e.Key == Key.Q)
				Application.Current.Shutdown();
            else if ((e.KeyboardDevice.IsKeyDown(Key.LeftCtrl) || e.KeyboardDevice.IsKeyDown(Key.RightCtrl)) && e.Key == Key.F)
            {
				this.Topmost = true;
				this.WindowStyle = WindowStyle.None;
                if (SystemParameters.VirtualScreenWidth > SystemParameters.PrimaryScreenWidth)
                    this.Left = SystemParameters.PrimaryScreenWidth;
                else
                    this.Left = 0;
				this.Top = 0;
			}
			else if (e.Key == Key.Escape) {
				this.Topmost = false;
				this.WindowStyle = WindowStyle.SingleBorderWindow;
			}
            else if (e.Key == Key.Right)
            {
                rotateDemo();
            }
		}

        private void fadeOut_Completed(object sender, System.EventArgs e)
        {
            if (nextDemo == 0)
            {
                List<Demo> newDemos = loadJSON();

                if (newDemos != null)
                {
                    demos = newDemos;
                    demoBoxes.Children.Clear();
                    foreach (Demo d in demos)
                        demoBoxes.Children.Add(d.DemoBox);
                }
            }

            setActiveDemo(nextDemo);
        }
	}
	
	
}