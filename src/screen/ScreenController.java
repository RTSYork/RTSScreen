package screen;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.CacheHint;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import java.io.IOException;

public class ScreenController implements UDPServerCallback {

    public ImageView imageView;
    public VBox demosBox;
    public Label title;
    public Label description;
    public BorderPane demoContent;
    private TextArea demoConsole;
    private Canvas demoCanvas;
    private WebView demoWebView;
    private UDPServer mUdpServer;


    public void setupScreen() {

    }

    public void closeScreen() {
        if (mUdpServer != null)
            mUdpServer.stopListening();
    }

    @Override
    public Canvas getDemoCanvas() {
        return demoCanvas;
    }

    @Override
    public TextArea getDemoConsole() {
        return demoConsole;
    }

    public void resetDemoScreen() {
        if (mUdpServer != null) {
            mUdpServer.stopListening();
            mUdpServer = null;
        }

        if (demoCanvas != null)
            demoCanvas = null;

        if (demoConsole != null)
            demoConsole = null;

        if (demoWebView != null)
            demoWebView = null;

        demoContent.setCenter(null);
        demoContent.setLeft(null);
        demoContent.setRight(null);
        demoContent.setTop(null);
        demoContent.setBottom(null);
    }

    public void setupActiveDemo(Demo demo) throws IOException {
        resetDemoScreen();

        imageView.setImage(demo.getImage());
        title.setText(demo.getTitle());
        description.setText(demo.getDescription());

        boolean hasConsole = false;
        boolean hasGraphics = false;
        int graphicsWidth = 0;
        int graphicsHeight = 0;

        if (demo.hasType(Demo.ConsoleType)) {
            hasConsole = true;

            demoConsole = new TextArea();
            demoConsole.setEditable(false);
            demoConsole.setFocusTraversable(false);
            demoConsole.setWrapText(true);
            demoConsole.getStyleClass().add("console");
            demoContent.setCenter(demoConsole);
        }

        if (demo.hasType(Demo.GraphicsType)) {
            hasGraphics = true;
            graphicsWidth = ((Double)demo.getOptionValue(Demo.GraphicsWidthOption)).intValue();
            graphicsHeight = ((Double)demo.getOptionValue(Demo.GraphicsHeightOption)).intValue();

            demoCanvas = new Canvas();
            demoCanvas.setCache(true);
            demoCanvas.setCacheHint(CacheHint.SPEED);
            demoCanvas.setWidth(graphicsWidth + 20);
            demoCanvas.setHeight(graphicsHeight + 20);
            demoContent.setRight(demoCanvas);
        }

        if (demo.hasType(Demo.VideoType)) {
            demoWebView = new WebView();
            demoWebView.getEngine().getLoadWorker().stateProperty().addListener(
                    new ChangeListener<Worker.State>() {
                        public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                            if (newState == Worker.State.SUCCEEDED && demoWebView != null) {
                                demoWebView.getEngine().executeScript("function onChange() {player1.setPlaybackQuality('hd1080');player1.setPlaybackQuality('hd1080');player1.setPlaybackQuality('hd1080');player1.setPlaybackQuality('hd1080');}setTimeout(function(){player1.addEventListener('onStateChange', 'onChange');onChange();}, 1000);");
                            }
                        }
                    });
            String videoId = (String)demo.getOptionValue(Demo.VideoIdOption);
            String videoUrl = String.format("http://www.youtube.com/embed/%s?playlist=%s&rel=0&controls=0&disablekb=1&hd=1&showinfo=0&modestbranding=1&loop=1&fs=0&enablejsapi=1&playerapiid=player1&autoplay=1", videoId, videoId);
            demoWebView.getEngine().load(videoUrl);

            demoContent.setCenter(demoWebView);
        }

        if (hasGraphics || hasConsole) {
            mUdpServer = new UDPServer(demo.getPort(), graphicsWidth, graphicsHeight, hasGraphics, hasConsole, this);
            mUdpServer.startListening();
        }
    }

    public void addDemoBox(Demo demo) throws IOException {
        demosBox.getChildren().add(demo.getDemoBox());
    }

    public void clearDemoBoxes() {
        demosBox.getChildren().clear();
    }

    public void appendConsoleText(String text) {
        if (demoConsole != null)
            demoConsole.appendText(text);
    }

    public void appendConsoleLine(String text) {
        if (demoConsole != null)
            demoConsole.appendText(text + "\n");
    }

    public void setConsoleText(String text) {
        if (demoConsole != null)
            demoConsole.setText(text);
    }

    public void clearConsole() {
        if (demoConsole != null)
            demoConsole.clear();
    }
}
