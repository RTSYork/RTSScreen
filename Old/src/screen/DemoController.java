package screen;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class DemoController {

    public ImageView imageView;
    public Label titleLabel;
    public BorderPane demoBox;

    public void setupDemo(String title, String imageName) {
        String imageUrl = getClass().getResource(imageName).toString();
        Image image = new Image(imageUrl, imageView.getFitWidth(), imageView.getFitHeight(), true, true, true);
        setupDemo(title, image);
    }

    public void setupDemo(String title, Image image) {
        imageView.setImage(image);
        titleLabel.setText(title);
    }

    public void setHighlighted(boolean highlighted) {
        if (highlighted)
            demoBox.setStyle("-fx-background-color: #ddd");
        else
            demoBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0)");
    }

}
