package xyz.shpasha.androidtfs.ui.auth;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import xyz.shpasha.androidtfs.utils.AbsentLiveData;
import xyz.shpasha.androidtfs.model.AuthBody;
import xyz.shpasha.androidtfs.model.Resource;
import xyz.shpasha.androidtfs.source.remote.DataRepository;

import java.util.Set;

public class AuthViewModel extends ViewModel {

    private LiveData<Resource<Set<String>>> loginDetails;
    private MutableLiveData<AuthBody> authRequest = new MutableLiveData<>();


    private DataRepository dataRepository;

    public AuthViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        initLiveData();
    }

    private void initLiveData() {
        loginDetails = Transformations.switchMap(authRequest, authBody -> {
            if (authBody == null) return AbsentLiveData.create();
            return dataRepository.signIn(authBody);
        });
   }

    public void signIn(String email, String password) {
        authRequest.setValue(new AuthBody(email, password));
    }
    public LiveData<Resource<Set<String>>> authDetails() {
        return loginDetails;
    }
}
