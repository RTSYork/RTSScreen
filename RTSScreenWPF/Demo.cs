using System;
using System.Windows.Controls;
using System.Windows.Media.Imaging;
using System.IO;

namespace RTSScreenWPF
{
    public abstract class Demo : UserControl
    {
        readonly string title;
        readonly string description;
        readonly Uri imageName;
        readonly int duration;
        private BitmapImage image;
        private bool active;
        private DemoBox box;

        public string Title
        {
            get { return title; }
        }

        public string Description
        {
            get { return description; }
        }

        public BitmapImage Image
        {
            get { return image; }
        }

        public int Duration
        {
            get { return duration; }
        }

        public virtual bool Active
        {
            get { return active; }
            set 
            {
                active = value;
                box.Highlighted = active;
            }
        }

        public DemoBox DemoBox
        {
            get { return box; }
        }

        public Demo(string title, string description, string imageName, int duration)
		{
            this.title = title;
            this.description = description;
            this.imageName = new Uri(Environment.CurrentDirectory + Path.DirectorySeparatorChar + imageName);
            try
            {
                this.image = new BitmapImage(this.imageName);
            }
            catch
            {
                this.image = null;
            }
            this.duration = duration;
            this.box = new DemoBox(title, image);
		}
    }
}
