package tankrotationexample.game;

public class Util {
    public static <T extends Comparable<T>> T clamp(T val, T min, T max) {
        T result = val;
        if (result.compareTo(min) < 0) {
            result = min;
        }
        if (result.compareTo(max) > 0) {
            result = max;
        }
        return result;
    }
}
