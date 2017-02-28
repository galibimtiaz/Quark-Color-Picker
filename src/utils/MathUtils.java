package utils;

/**
 * Created by Galib on 2/27/2017.
 */
public class MathUtils {

    public static int getRoundValue(double doubleValue) {
        return (int) (doubleValue * 255);
    }
    public static double toHalfRouned(double value){
        return Math.round(value * 100.0) / 100.0;
    }

}
