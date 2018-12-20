package xyz.shpasha.androidtfs.source.remote;

import java.util.List;
import java.util.Map;

import android.arch.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.http.*;
import xyz.shpasha.androidtfs.model.*;

public interface ApiService {
    String API_URL = "https://fintech.tinkoff.ru/";

    @POST("/api/signin")
    Call<Detail> signIn(@HeaderMap Map<String, String> headers, @Body AuthBody user);

    @POST("/api/signout")
    Call<Void> signout(@HeaderMap Map<String,String> headers, @Body String payload);

    @GET("/api/user")
    LiveData<ApiResponse<UserDetail>> getUser();

    @PUT("/api/register_user")
    Call<String> editUser(@HeaderMap Map<String, String> headers, @Body User user);

    @GET("/api/course/android_fall2018/homeworks")
    Call<Homeworks> getHomeworks();

    @GET("/api/contest/{contest_url}/problems")
    Call<List<Problem>> getTestProblems(@Path("contest_url") String contestUrl);

    @GET("/api/contest/{contest_url}/status")
    Call<TestStatus> getTestStatus(@Path("contest_url") String contestUrl);

    @POST("/api/contest/{contest_url}/problem/{id}")
    Call<ProblemResponseOnAnswer> sendProblemAnswer(@HeaderMap Map<String, String> headers, @Path("contest_url") String contestUrl, @Path("id") int id, @Body AnswerBody answerBody);

    @POST("/api/contest/{contest_url}/start_contest")
    Call<Object> startContest(@HeaderMap Map<String, String> headers, @Path("contest_url") String contestUrl, @Body String payload);

}
