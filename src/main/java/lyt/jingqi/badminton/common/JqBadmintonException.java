package lyt.jingqi.badminton.common;

public class JqBadmintonException extends RuntimeException {

    public JqBadmintonException(){
    }

    public JqBadmintonException(String message) {
        super(message);
    }

    /**
     * 抛出一个异常
     *
     * @param message
     */
    public static void fail(String message) {
        throw new JqBadmintonException(message);
    }
}
