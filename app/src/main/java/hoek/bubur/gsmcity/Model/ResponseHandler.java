package hoek.bubur.gsmcity.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dalbo on 5/23/2017.
 */

public class ResponseHandler {
    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;

    public ResponseHandler() {
    }

    public ResponseHandler(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
