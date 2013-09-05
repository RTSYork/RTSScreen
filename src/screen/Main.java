package screen;

import javafx.application.Application;
import javafx.application.Platform;
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
import java.util.*;

import com.google.gson.Gson;

public class Main extends Application {

    ArrayList<Demo> demos = new ArrayList<Demo>();
    ScreenController screenController;
    int activeDemo;
    Timer demoTimer;
    TimerTask demoTimerTask;

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
        Reader reader;
        try {
            reader = new InputStreamReader(new FileInputStream("demos.json"));
        }
        catch (IOException e) {
            reader = new InputStreamReader(getClass().getResourceAsStream("demos.json"));
        }
        List<Map> results = gson.fromJson(reader, List.class);

        for (Map result : results) {
            Demo demo = new Demo(result);
            demos.add(demo);
            screenController.addDemoBox(demo);
        }

        demoTimer = new Timer();

        activeDemo = -1;
        nextDemo();

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.F && keyEvent.isControlDown())
                    primaryStage.setFullScreen(true);
                else if (keyEvent.getCode() == KeyCode.Q && keyEvent.isControlDown())
                    primaryStage.close();
                else if (keyEvent.getCode() == KeyCode.RIGHT)
                    nextDemo();
                else if (keyEvent.getCode() == KeyCode.LEFT)
                    previousDemo();
            }
        });


    }

    public void nextDemo() {
        if (demoTimerTask != null)
            demoTimerTask.cancel();

        activeDemo++;
        if (activeDemo >= demos.size())
            activeDemo = 0;

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        screenController.setupActiveDemo(demos.get(activeDemo));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            try {
                screenController.setupActiveDemo(demos.get(activeDemo));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        demoTimerTask = new TimerTask() {
            @Override
            public void run() {
                nextDemo();
            }
        };

        demoTimer.schedule(demoTimerTask, demos.get(activeDemo).getDuration() * 1000);

        System.gc();
    }

    public void previousDemo() {
        if (activeDemo == 0)
            activeDemo = demos.size() - 2;
        else
            activeDemo -= 2;

        nextDemo();
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
