using System;
using System.Net.Sockets;
using System.Net;
using System.Text;
using System.IO;

namespace RTSScreenWPF
{
    public abstract class UDPDemo : Demo
    {
        protected UdpClient udpClient;
        protected IPEndPoint RemoteIpEndPoint;
        private bool open = false;
        private int port;

        public override bool Active
        {
            get { return base.Active; }
            set
            {
                base.Active = value;
                if (value)
                    openServer();
                else
                    closeServer();
            }
        }

        public UDPDemo(string title, string description, string imageName, int duration, int port) : base(title, description, imageName, duration)
        {
            this.port = port;
            IPEndPoint RemoteIpEndPoint = new IPEndPoint(IPAddress.Any, 0);
        }

        protected void openServer()
        {
            if (!open)
            {
                try
                {
                    open = true;
                    udpClient = new UdpClient(port);
                    udpClient.Client.ReceiveBufferSize = 1584000;
                    udpClient.BeginReceive(new AsyncCallback(ReceiveCallback), null);
                }
                catch (Exception e)
                {
                    App.LogException(e);
                }
            }
        }

        protected void closeServer()
        {
            if (open)
            {
                open = false;
                try
                {
                    udpClient.Close();
                }
                catch (Exception e)
                {
                    App.LogException(e);
                }
            }
        }

        public void ReceiveCallback(IAsyncResult ar)
        {
            try
            {
                if (open)
                {
                    byte[] bytes = udpClient.EndReceive(ar, ref RemoteIpEndPoint);
                    DataReceived(bytes);
                }

                if (open)
                {
                    udpClient.BeginReceive(new AsyncCallback(ReceiveCallback), null);
                }
            }
            catch (Exception e)
            {
                App.LogException(e);
            }
        }

        protected abstract void DataReceived(byte[] data);
    }
}
