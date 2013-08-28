package screen;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class Main extends Application {

    ArrayList<Demo> demos = new ArrayList<Demo>();
    ScreenController screenController;
    int activeDemo;

    @Override
    public void start(final Stage primaryStage) throws Exception {
        URL location = getClass().getResource("screen.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

        Parent root = (Parent) fxmlLoader.load(location.openStream());

        root.getStylesheets().add("screen/screen.css");
        primaryStage.setTitle("RTS Screen");

        Rectangle2D r = Screen.getPrimary().getBounds();
        Scene scene = new Scene(root, r.getWidth(), r.getHeight());

        screenController = fxmlLoader.getController();
        screenController.setupScreen();

        // Parse JSON file of demos
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(getClass().getResourceAsStream("demos.json"));
        List<Map> results = gson.fromJson(reader, List.class);

        for (Map result : results) {
            Demo demo = new Demo(result);
            demos.add(demo);
            screenController.addDemoBox(demo);
        }

        activeDemo = -1;
        nextDemo();

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        screenController.appendConsoleLine("Testing, 123");

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.F && keyEvent.isControlDown())
                    primaryStage.setFullScreen(true);
                else if (keyEvent.getCode() == KeyCode.Q && keyEvent.isControlDown())
                    primaryStage.close();
            }
        });
    }

    public void nextDemo() throws IOException {
        activeDemo++;
        screenController.setupActiveDemo(demos.get(activeDemo));
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        screenController.closeScreen();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
