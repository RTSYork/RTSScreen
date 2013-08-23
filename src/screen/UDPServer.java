package screen;

import Extasys.Network.UDP.Server.ExtasysUDPServer;
import Extasys.Network.UDP.Server.Listener.UDPListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferShort;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;

public class UDPServer extends ExtasysUDPServer {

    private int mPort;
    private short[] mImageData;
    private boolean mListening;
    private int mFrameWidth = 440;
    private int mFrameHeight = 360;
    private BufferedImage mImage;
    private UDPServerCallback mController;

    public UDPServer(int port, UDPServerCallback controller) throws UnknownHostException {
        super("", "", 1, 2);
        mController = controller;
        mPort = port;
        this.AddListener("", java.net.InetAddress.getByName("0.0.0.0"), mPort, mFrameWidth * 2 + 2, 1000);
    }

    public void startListening() throws SocketException {
        mImageData = new short[mFrameWidth * mFrameHeight];
        mImage = new BufferedImage(mFrameWidth, mFrameHeight, BufferedImage.TYPE_USHORT_565_RGB);
        this.Start();
    }

    public void stopListening() {
        this.Stop();
    }

    @Override
    public void OnDataReceive(UDPListener listener, DatagramPacket packet)
    {
        byte[] bytes = packet.getData();

        int y = ((bytes[1] & 0xFF) << 8) | (bytes[0] & 0xFF);
        int interlace = (y & 0x1);

        for (int x = 0; x < bytes.length - 2; x++)
        {
            if (y < mFrameHeight && x < mFrameWidth)
            {
                mImageData[x + (y * mFrameWidth)] = (short)(((bytes[x*2+3] & 0xFF) << 8) | (bytes[x*2+2] & 0xFF));
            }
        }

        if (y >= mFrameHeight - 2)
        {
            updateImage();
        }
    }

    private void updateImage() {
        short[] imgData = ((DataBufferShort)mImage.getRaster().getDataBuffer()).getData();
        System.arraycopy(mImageData, 0, imgData, 0, mImageData.length);
        try {
            mController.updateImage(createImage(mImage));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static javafx.scene.image.Image createImage(java.awt.Image image) throws IOException {
        if (!(image instanceof RenderedImage)) {
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                    image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();

            image = bufferedImage;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write((RenderedImage) image, "png", out);
        out.flush();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        return new javafx.scene.image.Image(in);
    }
}
