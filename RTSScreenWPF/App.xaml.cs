using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Windows;
using System.Windows.Threading;
using System.Text;
using System.IO;
using System.Threading.Tasks;

namespace RTSScreenWPF
{
	/// <summary>
	/// Interaction logic for App.xaml
	/// </summary>
	public partial class App : Application
	{

        public App()
        {
            DispatcherUnhandledException += new DispatcherUnhandledExceptionEventHandler(App_DispatcherUnhandledException);
            TaskScheduler.UnobservedTaskException += TaskScheduler_UnobservedTaskException;
            Startup += new StartupEventHandler(App_Startup);
        }

        void App_Startup(object sender, StartupEventArgs e)
        {
            AppDomain.CurrentDomain.UnhandledException += CurrentDomain_UnhandledException;
        }

        void App_DispatcherUnhandledException(object sender, DispatcherUnhandledExceptionEventArgs e)
        {
            LogException(e.Exception);
            e.Handled = true;
        }

        void TaskScheduler_UnobservedTaskException(object sender, UnobservedTaskExceptionEventArgs e)
        {
            LogException(e.Exception);
            e.SetObserved();
        }

        void CurrentDomain_UnhandledException(object sender, UnhandledExceptionEventArgs e)
        {
            var exception = e.ExceptionObject as Exception;
            LogException(exception);
        }

        public static void LogException(Exception e)
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

                if (e.InnerException.InnerException != null)
                {
                    sb.AppendLine(e.InnerException.InnerException.Message);
                    sb.AppendLine(e.InnerException.InnerException.StackTrace);
                    sb.AppendLine();
                }
            }
            File.AppendAllText("log.txt", sb.ToString());
        }
	}
}