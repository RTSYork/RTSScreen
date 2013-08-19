package screen;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;

public class ScreenController {

    public ImageView imageView;
    public VBox demosBox;
    public TextArea console;
    public Label title;
    public Label description;

    public void setupScreen() throws IOException {

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
