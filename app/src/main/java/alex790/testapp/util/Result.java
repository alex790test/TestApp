package alex790.testapp.util;

/**
 * Created by alex790 on 09.06.2018.
 */

public class Result {

    public static int NO_ERROR = 0;


    private int errorCode;
    private String value;


    public Result(int errorCode, String value) {
        this.errorCode = errorCode;
        this.value = value;
    }


    public int getErrorCode() {
        return errorCode;
    }


    public String getValue() {
        return value;
    }
}
