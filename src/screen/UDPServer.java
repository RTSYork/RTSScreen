package screen;

import Extasys.Network.UDP.Server.ExtasysUDPServer;
import Extasys.Network.UDP.Server.Listener.UDPListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class UDPServer extends ExtasysUDPServer {

    private int mPort;
    private int[] mImageData;
    private boolean mListening;
    private int mFrameWidth = 440;
    private int mFrameHeight = 360;
    private WritableImage mImage;
    private Canvas mCanvas;
    private TextArea mConsole;
    private GraphicsContext mGraphics;
    private UDPServerCallback mController;
    private final Semaphore receiving = new Semaphore(1);
    private final int gcInterval = 120;
    private int gcCounter = 0;

    public UDPServer(int port, UDPServerCallback controller) throws UnknownHostException {
        super("", "", 1, 1);
        mController = controller;
        mPort = port;
        mCanvas = mController.getDemoCanvas();
        mCanvas.setWidth(mFrameWidth);
        mCanvas.setHeight(mFrameHeight);
        mGraphics = mCanvas.getGraphicsContext2D();
        this.AddListener("", java.net.InetAddress.getByName("0.0.0.0"), mPort, mFrameWidth * 2 + 2, 2000);
    }

    public void startListening() throws SocketException {
        mImageData = new int[mFrameWidth * mFrameHeight];
        Arrays.fill(mImageData, 0xFFFFFFFF);
        mImage = new WritableImage(mFrameWidth, mFrameHeight);
        this.Start();
    }

    public void stopListening() {
        this.Stop();
    }

    @Override
    public void OnDataReceive(UDPListener listener, DatagramPacket packet)
    {
        //receiving.acquireUninterruptibly();

        byte[] bytes = packet.getData();

        int y = ((bytes[1] & 0xFF) << 8) | (bytes[0] & 0xFF);

        //System.arraycopy(bytes, 2, mImageData, y * mFrameWidth, mFrameWidth * 2);

        //byte r;
        //byte g;
        //byte b;
        int x = 0;
        for (int i = 2; i < bytes.length; i+=2) {
            //r = (byte);
            //g = (byte)(((bytes[i+1] & 0x07) << 5) | ((bytes[i] & 0xE0) >> 3));
            //b = (byte)((bytes[i] & 0x1F) << 3);
            //mImageData[x + (y * mFrameWidth * 4)] = b;
            //mImageData[x + (y * mFrameWidth * 4) + 1] = g;
            //mImageData[x + (y * mFrameWidth * 4) + 2] = r;
            mImageData[x + (y * mFrameWidth)] = 0xFF000000 | ((bytes[i+1] & 0xF8) << 16) | (((bytes[i+1] & 0x07) << 13) | ((bytes[i] & 0xE0) << 5)) | ((bytes[i] & 0x1F) << 3);
            x++;
        }

        if (y >= mFrameHeight - 2)
        {
            updateImage();
        }

        //receiving.release();
    }

    private void updateImage() {
        PixelWriter pixelWriter = mGraphics.getPixelWriter();
        WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
        pixelWriter.setPixels(0, 0, mFrameWidth, mFrameHeight, pixelFormat, mImageData, 0, mFrameWidth);
        //mController.updateImage(mImage);

        if (gcCounter == gcInterval) {
            System.gc();
            gcCounter = 0;
        }
        else {
            gcCounter++;
        }
    }
}
