package xyz.shpasha.androidtfs.source;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import xyz.shpasha.androidtfs.source.remote.ApiResponse;
import xyz.shpasha.androidtfs.utils.AppExecutors;
import xyz.shpasha.androidtfs.model.Resource;
import xyz.shpasha.androidtfs.model.UserDetail;
import xyz.shpasha.androidtfs.utils.Objects;

public abstract class NetworkBoundResource<ResultType, RequestType> {
    private final AppExecutors appExecutors = AppExecutors.getInstanse();

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    public NetworkBoundResource() {
        result.setValue(Resource.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource, newData -> setValue(Resource.success(newData)));
            }
        });
    }

    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        if (!Objects.equals(result.getValue(), newValue)) {
            result.setValue(newValue);
        }
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        LiveData<ApiResponse<RequestType>> apiResponse = createCall();
        result.addSource(dbSource, newData -> setValue(Resource.loading(newData)));
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);
            if (response.isSuccessful() && isUserDetailAndCorrect(response.body)) {
                appExecutors.diskIO().execute(() -> {
                    saveCallResult(processResponse(response));
                    appExecutors.mainThread().execute(() ->
                            result.addSource(loadFromDb(),
                                    newData -> setValue(Resource.success(newData)))
                    );
                });
            } else {
                onFetchFailed();

                // ЭТО КОСТЫЛЬ. СЕРВЕР ВОЗВРАЩАЕТ STATUS 200 НА ЗАПРОС ПОЛЬЗОВАТЕЛЯ, ЕСЛИ ОН НЕ АВТОРИЗОВАН. ТОЛЬКО ТАК ПОЛУЧИЛОСЬ РЕШИТЬ.
                if (isUserDetail(response.body))
                    response.errorMessage = ((UserDetail)response.body).getMessage();


                result.addSource(dbSource, newData -> setValue(Resource.error(response.errorMessage, newData)));
            }
        });
    }

    private boolean isUserDetail(RequestType body) {
        return body != null && body.getClass() == UserDetail.class;
    }

    private boolean isUserDetailAndCorrect(RequestType body) {
        return isUserDetail(body) && ((UserDetail)body).getUser() != null;
    }


    protected void onFetchFailed() {

    }

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

    @WorkerThread
    protected RequestType processResponse(ApiResponse<RequestType> response) {
        return response.body;
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();
}
