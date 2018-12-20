package xyz.shpasha.androidtfs.source.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.shpasha.androidtfs.model.*;
import xyz.shpasha.androidtfs.source.NetworkBoundResource;
import xyz.shpasha.androidtfs.source.local.AppDatabase;
import xyz.shpasha.androidtfs.model.Answer;
import xyz.shpasha.androidtfs.utils.LiveDataCallAdapterFactory;

public class DataRepository {
    private static DataRepository dataRepository;
    private ApiService apiService;
    private MyCookieJar myCookieJar;

    private DataRepository() {
        initRetrofit();
    }

    public static DataRepository getInstance() {
        if (dataRepository == null) {
            dataRepository = new DataRepository();
        }
        return dataRepository;
    }

    private Map<String, String> getNeededHeaders() {
        Map<String, String> headers = new HashMap<>();
        String token = myCookieJar.getCsrfToken();

        if (token != null) {
            headers.put("X-CSRFToken", token);
            headers.put("Referer", "https://fintech.tinkoff.ru/");
        }

        return headers;
    }

    public LiveData<Resource<Set<String>>> signIn(AuthBody authBody) {

        final  MutableLiveData<Resource<Set<String>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        apiService.signIn(getNeededHeaders(), authBody).enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(@NonNull Call<Detail> call, @NonNull Response<Detail> response) {
                if (response.isSuccessful()) {
                    data.setValue(Resource.success(myCookieJar.getCookieStringSet()));
                } else {
                    data.setValue(Resource.error(getDetails(response.errorBody()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Detail> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<String>> signout() {

        final  MutableLiveData<Resource<String>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        apiService.signout(getNeededHeaders(), "{}").enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    data.setValue(Resource.success("ok"));
                } else {
                    data.setValue(Resource.error(getDetails(response.errorBody()), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    private AppDatabase appDatabase;

    public void setDb(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    public LiveData<Resource<User>> getUser() {
        return new NetworkBoundResource<User, UserDetail>() {
            @Override
            protected void saveCallResult(@NonNull UserDetail item) {
                if (item.getUser() != null)
                    appDatabase.userDao().insert(item.getUser());
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return true;
            }


            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return appDatabase.userDao().findByEmail("pash-96@mail.ru");
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<UserDetail>> createCall() {
                return apiService.getUser();
            }
        }.asLiveData();
    }

/*
    public LiveData<Resource<User>> getUser() {
        final  MutableLiveData<Resource<User>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        apiService.getUser().enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(@NonNull Call<UserDetail> call, @NonNull Response<UserDetail> response) {
                UserDetail userDetail = response.body();
                if (userDetail.getStatus().equals("Ok"))
                    data.setValue(Resource.success(response.body().getUser()));
                if (userDetail.getStatus().equals("Error")) {
                    data.setValue(Resource.error(userDetail.getMessage(), null));
                }
            }
            @Override
            public void onFailure(@NonNull Call<UserDetail> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }
*/
    public LiveData<Resource<String>> edit(User user) {
        final  MutableLiveData<Resource<String>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        apiService.editUser(getNeededHeaders(), user).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful())
                    data.setValue(Resource.success(response.body()));
                else {
                    data.setValue(Resource.error(getDetails(response.errorBody()), null));
                }

            }
            @Override
            public void onFailure(@NonNull Call<String> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<List<Homework>>> getHomeworks() {
        final  MutableLiveData<Resource<List<Homework>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        apiService.getHomeworks().enqueue(new Callback<Homeworks>() {
            @Override
            public void onResponse(@NonNull Call<Homeworks> call, @NonNull Response<Homeworks> response) {
                if (response.isSuccessful()) {
                    data.setValue(Resource.success(response.body().getHomeworks()));
                } else {
                    data.setValue(Resource.error("error", null));
                }
            }
            @Override
            public void onFailure(@NonNull Call<Homeworks> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<List<Problem>>> getTestProblems(String contestUrl) {
        final  MutableLiveData<Resource<List<Problem>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        apiService.getTestProblems(contestUrl).enqueue(new Callback<List<Problem>>() {
            @Override
            public void onResponse(@NonNull Call<List<Problem>> call, @NonNull Response<List<Problem>> response) {
                if (response.isSuccessful()) {
                    data.setValue(Resource.success(response.body()));
                } else {
                    data.setValue(Resource.error(getDetails(response.errorBody()), null));
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Problem>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<TestStatus>> getTestStatus(String contestUrl) {
        final  MutableLiveData<Resource<TestStatus>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        apiService.getTestStatus(contestUrl).enqueue(new Callback<TestStatus>() {
            @Override
            public void onResponse(@NonNull Call<TestStatus> call, @NonNull Response<TestStatus> response) {
                if (response.isSuccessful()) {
                    data.setValue(Resource.success(response.body()));
                } else {
                    data.setValue(Resource.error(getDetails(response.errorBody()), null));
                }
            }
            @Override
            public void onFailure(@NonNull Call<TestStatus> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<ProblemResponseOnAnswer>> sendAnswer(String contestUrl, Answer answer) {
        final  MutableLiveData<Resource<ProblemResponseOnAnswer>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        apiService.sendProblemAnswer(getNeededHeaders(), contestUrl, answer.getProblemId(), new AnswerBody(answer.getAnswers())).enqueue(new Callback<ProblemResponseOnAnswer>() {
            @Override
            public void onResponse(@NonNull Call<ProblemResponseOnAnswer> call, @NonNull Response<ProblemResponseOnAnswer> response) {
                if (response.isSuccessful()) {
                    data.setValue(Resource.success(response.body()));
                } else {
                    ProblemResponseOnAnswer responseOnAnswer = new ProblemResponseOnAnswer();
                    responseOnAnswer.setProblemId(answer.getProblemId());
                    data.setValue(Resource.error(getDetails(response.errorBody()), responseOnAnswer));
                }
            }
            @Override
            public void onFailure(@NonNull Call<ProblemResponseOnAnswer> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<Object>> startContest(String contestUrl) {
        final  MutableLiveData<Resource<Object>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        apiService.startContest(getNeededHeaders(), contestUrl, "{}").enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (response.isSuccessful()) {
                    data.setValue(Resource.success(response.body()));
                } else {
                    data.setValue(Resource.error(getDetails(response.errorBody()), null));
                }
            }
            @Override
            public void onFailure(@NonNull Call<Object> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    private String getDetails(ResponseBody responseBody) {
        if (responseBody == null) return "Unknown error";
        String json;
        try {
            json = responseBody.string();
            Gson gson = new Gson();
            Detail detail = gson.fromJson(json, Detail.class);
            return detail == null ? null : detail.getDetail();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown error";
    }

    public void setCookies(Set<String> cookieSet) {
        myCookieJar.setCookies(cookieSet);
    }

    private void initRetrofit() {

        myCookieJar = new MyCookieJar();

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(myCookieJar)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        apiService = new Retrofit.Builder()
                .baseUrl(ApiService.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(ApiService.class);
    }

}
