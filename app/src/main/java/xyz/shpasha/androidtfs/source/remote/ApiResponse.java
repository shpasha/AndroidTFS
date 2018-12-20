package xyz.shpasha.androidtfs.source.remote;

import android.support.annotation.Nullable;
import android.util.Log;
import retrofit2.Response;

import java.io.IOException;

/**
 * Common class used by API responses.
 * @param <T>
 */
public class ApiResponse<T> {
    public final int code;
    @Nullable
    public final T body;
    @Nullable
    public String errorMessage;


    public ApiResponse(Throwable error) {
        code = 500;
        body = null;
        errorMessage = error.getMessage();
    }

    public ApiResponse(Response<T> response) {
        code = response.code();
        if(response.isSuccessful()) {
            body = response.body();
            errorMessage = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string();
                } catch (IOException e) {
                    Log.e("parse error", e.getMessage());
                }
            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            errorMessage = message;
            body = null;
        }

    }

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

}
