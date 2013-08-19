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

import java.net.URL;
import java.util.ArrayList;

public class Main extends Application {

    ArrayList<Demo> demos = new ArrayList<Demo>();

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

        ScreenController controller = (ScreenController)fxmlLoader.getController();
        controller.setupScreen();

        demos.add(new Demo("Guitar Hero on FPGA",
                "The Autocaster is an embedded system based around a Digilent Atlys FPGA board, developed by Russell Joyce as a final year project for the MEng Computer Science with Embedded Systems Engineering course.\n\n" +
                "The project's original title was 'Guitar Hero on FPGA', which evolved into a system that can autonomously play both Rock Band and Guitar Hero games running on a PlayStation 3.\n\n" +
                "The demo above shows the real-time controller output of the system while monitoring the HDMI graphics from a games console.",
                "atlys.jpg"));

        demos.add(new Demo("Ultrasonic Array for Maze Robot",
                "The ultrasonic maze robot was developed by Phil Greenland for a final year project in Computer Science with Embedded Systems Engineering, with the aim of creating a robot to navigate a maze using ultrasonic sdensors.\n\n" +
                "The system developed consists of four main components: an FPGA development board which can be seen sat on top of the platform; a mobile platform on which everything is mounted; sensor boards which perform" +
                "the ultrasound generation and capture as part of the ultrasonic imaging system; and a mainboard which provides an interface between the FPGA, mobile platform and sensor boards while containing additional hardware required by the ultrasonic imaging system.",
                "usrobot.jpg"));

        controller.addDemoBox(demos.get(0));
        controller.addDemoBox(demos.get(1));
        controller.setupActiveDemo(demos.get(0));

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        controller.appendConsoleLine("Testing, 123");

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE)
                    primaryStage.setFullScreen(true);
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
