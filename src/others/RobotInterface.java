package others;


import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

/**
 * Created by Galib on 2/26/2017.
 */
public interface RobotInterface {

    void destroy();

    void keyPress(KeyCode key);

    void keyRelease(KeyCode key);

    Point2D getMouseLocation();

    void mouseMove(Point2D location);

    void mousePress(MouseButton button);

    void mouseRelease(MouseButton button);

    void mouseWheel(int wheelAmount);

    Color getCapturePixelColor(Point2D location);

    Image getCaptureRegion(Rectangle2D region);
}
