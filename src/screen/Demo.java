package screen;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.Map;

public class Demo {
    private String mTitle;
    private String mDescription;
    private String mImageName;
    private ArrayList<String> mTypes;
    private Map<String, Object> mOptions;
    private int mDuration;
    private int mPort;

    private Image mImage;
    private Node mDemoBox;
    private boolean mActive;

    public static final String ConsoleType = "console";
    public static final String GraphicsType = "graphics";
    public static final String VideoType = "video";

    public static final String GraphicsWidthOption = "graphicsWidth";
    public static final String GraphicsHeightOption = "graphicsHeight";
    public static final String VideoIdOption = "videoId";

    public Demo(String title, String description, String image, ArrayList<String> types, Map<String, Object> options, int duration, int port) {
        mTitle = title;
        mDescription = description;
        mImageName = image;
        mTypes = types;
        mOptions = options;
        mDuration = duration;
        mPort = port;
        mActive = false;
    }

    public Demo(Map params) {
        mTitle = (String)params.get("title");
        mDescription = (String)params.get("description");
        mImageName = (String)params.get("image");
        mTypes =(ArrayList<String>)params.get("types");
        mOptions =(Map<String, Object>)params.get("options");
        mDuration = ((Double)params.get("duration")).intValue();;
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

    public int getDuration() {
        return mDuration;
    }

    public int getPort() {
        return mPort;
    }

    public ArrayList<String> getTypes() {
        return mTypes;
    }

    public boolean hasType(String type) {
        return mTypes.contains(type);
    }

    public Map<String, Object> getOptions() {
        return mOptions;
    }

    public boolean hasOption(String option) {
        return mOptions.containsKey(option);
    }

    public Object getOptionValue(String option) {
        return mOptions.get(option);
    }

    public Image getImage() {
        if (mImage == null) {

            if (new File(mImageName).isFile()) {
                mImage = new Image(mImageName, true);
            }
            else {
                try {
                    String imageUrl = getClass().getResource(mImageName).toString();
                    mImage = new Image(imageUrl, true);
                }
                catch (Exception e) {
                    mImage = null;
                }
            }
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
