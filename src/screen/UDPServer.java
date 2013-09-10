package screen;

import Extasys.Network.UDP.Server.ExtasysUDPServer;
import Extasys.Network.UDP.Server.Listener.UDPListener;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class UDPServer extends ExtasysUDPServer {

    private int mPort;
    private int[] mImageData;
    private String mStringData;
    private boolean mListening;
    private int mFrameWidth;
    private int mFrameHeight;
    private Canvas mCanvas;
    private TextArea mConsole;
    private PixelWriter mPixelWriter;
    private WritablePixelFormat<IntBuffer> mPixelFormat;
    private byte[] mData;
    private UDPServerCallback mController;
    private final Semaphore receiving = new Semaphore(1);
    private final int gcInterval = 500;
    private int gcCounter = 0;
    private boolean mHasGraphics;
    private boolean mHasConsole;

    public UDPServer(int port, int frameWidth, int frameHeight, boolean hasGraphics, boolean hasConsole, UDPServerCallback controller) throws UnknownHostException {
        super("", "", 1, 1);
        mController = controller;
        mPort = port;
        mHasGraphics = hasGraphics;
        mHasConsole = hasConsole;
        mFrameWidth = frameWidth;
        mFrameHeight = frameHeight;
        if (mHasGraphics) {
            mCanvas = mController.getDemoCanvas();
            mPixelWriter = mCanvas.getGraphicsContext2D().getPixelWriter();
            mPixelFormat = PixelFormat.getIntArgbInstance();
        }
        if (mHasConsole) {
            mConsole = mController.getDemoConsole();
        }
        mListening = false;
        this.AddListener("", java.net.InetAddress.getByName("0.0.0.0"), mPort, mFrameWidth * 2 + 2, 2000);
    }

    public void startListening() throws SocketException {
        if (mHasGraphics) {
            mImageData = new int[mFrameWidth * mFrameHeight];
            Arrays.fill(mImageData, 0xFFFFFFFF);
        }
        mListening = true;
        this.Start();
    }

    public void stopListening() {
        mListening = false;
        this.Stop();
    }

    public boolean getListening() {
        return mListening;
    }

    @Override
    public void OnDataReceive(UDPListener listener, DatagramPacket packet)
    {
        receiving.acquireUninterruptibly();

        mData = packet.getData();
        int y = ((mData[1] & 0xFF) << 8) | (mData[0] & 0xFF);

        if (mHasConsole && y == 0xFFFF) {
            try {
                mStringData = new String(mData, 2, mData.length - 2, "Cp437");
            }
            catch (UnsupportedEncodingException e) {
                mStringData = new String(mData, 2, mData.length - 2);
            }

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                        mConsole.setText(mStringData);
                }
            });
        }
        else if (mHasGraphics && y < 0xFFFF) {
            int x = 0;
            for (int i = 2; i < mData.length; i+=2) {
                mImageData[x + (y * mFrameWidth)] = 0xFF000000 | ((mData[i+1] & 0xF8) << 16) | (((mData[i+1] & 0x07) << 13) | ((mData[i] & 0xE0) << 5)) | ((mData[i] & 0x1F) << 3);
                x++;
            }

            if (y >= mFrameHeight - 2)
            {
                updateImage();
            }
        }

        receiving.release();
    }

    private void updateImage() {
        try {
            mPixelWriter.setPixels(20, 20, mFrameWidth, mFrameHeight, mPixelFormat, mImageData, 0, mFrameWidth);
        }
        catch (Exception e) {
            System.out.println("Image update error");
            e.printStackTrace();
        }

        if (gcCounter == gcInterval) {
            System.gc();
            gcCounter = 0;
        }
        else {
            gcCounter++;
        }
    }
}
