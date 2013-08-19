package screen;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DemoController {

    public ImageView imageView;
    public Label titleLabel;

    public void setupDemo(String title, String imageName) {
        String imageUrl = getClass().getResource(imageName).toString();
        Image image = new Image(imageUrl, imageView.getFitWidth(), imageView.getFitHeight(), true, true, true);
        setupDemo(title, image);
    }

    public void setupDemo(String title, Image image) {
        imageView.setImage(image);
        titleLabel.setText(title);
    }

}
