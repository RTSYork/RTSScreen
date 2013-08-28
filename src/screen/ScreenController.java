package screen;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ScreenController implements UDPServerCallback {

    public ImageView imageView;
    public VBox demosBox;
    public TextArea console;
    public Label title;
    public Label description;
    public BorderPane demoContent;
    private Canvas demoCanvas;
    private UDPServer mUdpTest;


    public void setupScreen() throws IOException {
        demoCanvas = new Canvas();
        demoContent.setCenter(demoCanvas);

        mUdpTest = new UDPServer(0xF00D, this);
        mUdpTest.startListening();
    }

    public void closeScreen() {
        if (mUdpTest != null)
            mUdpTest.stopListening();
    }

    @Override
    public Canvas getDemoCanvas() {
        return demoCanvas;
    }

    @Override
    public TextArea getDemoConsole() {
        return console;
    }

    public void setupActiveDemo(Demo demo) {
        imageView.setImage(demo.getImage());
        title.setText(demo.getTitle());
        description.setText(demo.getDescription());
        clearConsole();
    }

    public void addDemoBox(Demo demo) throws IOException {
        demosBox.getChildren().add(demo.getDemoBox());
    }

    public void appendConsoleText(String text) {
        console.appendText(text);
    }

    public void appendConsoleLine(String text) {
        console.appendText("\n" + text);
    }

    public void setConsoleText(String text) {
        console.setText(text);
    }

    public void clearConsole() {
        console.clear();
    }
}
