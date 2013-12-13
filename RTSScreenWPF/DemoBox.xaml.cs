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
using System.Windows.Media.Animation;

namespace RTSScreenWPF
{
	/// <summary>
	/// Interaction logic for DemoBox.xaml
	/// </summary>
	public partial class DemoBox : UserControl
	{
		private bool highlighted;

        public bool Highlighted
        {
            get { return highlighted; }
            set
            {
                if (highlighted != value) {
					highlighted = value;
					fade(highlighted);
				}
            }
        }
		
		public DemoBox(string title, BitmapImage image)
		{
			this.InitializeComponent();
			
			this.title.Content = title;
			this.image.Source = image;
			highlighted = false;
		}
		
		private void fade(bool highlight) {
            Storyboard storyboard;
			if (highlight)
                storyboard = (Storyboard)FindResource("highlight");
            else
                storyboard = (Storyboard)FindResource("unhighlight");

            storyboard.Begin();
        }
	}
}