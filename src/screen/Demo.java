package screen;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class Demo {
    private String mTitle;
    private String mDescription;
    private String mImageName;
    private String mType;
    private int mPort;

    private Image mImage;
    private Node mDemoBox;
    private boolean mActive;

    public Demo(String title, String description, String image, String type, int port) {
        mTitle = title;
        mDescription = description;
        mImageName = image;
        mType = type;
        mPort = port;
        mActive = false;
    }

    public Demo(Map params) {
        mTitle = (String)params.get("title");
        mDescription = (String)params.get("description");
        mImageName = (String)params.get("image");
        mType =(String)params.get("type");
        mPort = ((Double)params.get("port")).intValue();
        mActive = false;
    }

    public void setActive(boolean active) {
        mActive = active;
    }

    public boolean getActive() {
        return mActive;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getImageName() {
        return mImageName;
    }

    public Image getImage() {
        if (mImage == null) {
            String imageUrl = getClass().getResource(mImageName).toString();
            mImage = new Image(imageUrl, true);
        }
        return mImage;
    }

    public Node getDemoBox() throws IOException {
        if (mDemoBox == null) {
            URL location = getClass().getResource("demo.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            mDemoBox = (Node)fxmlLoader.load(location.openStream());
            DemoController c = (DemoController)fxmlLoader.getController();
            c.setupDemo(mTitle, getImage());
        }
        return mDemoBox;
    }
}
