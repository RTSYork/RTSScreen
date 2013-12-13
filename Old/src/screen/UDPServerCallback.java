package screen;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;

public interface UDPServerCallback {
    public Canvas getDemoCanvas();
    public TextArea getDemoConsole();
}
