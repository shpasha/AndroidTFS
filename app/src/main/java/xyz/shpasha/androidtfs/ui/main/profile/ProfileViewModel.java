package xyz.shpasha.androidtfs.ui.main.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import xyz.shpasha.androidtfs.model.Resource;
import xyz.shpasha.androidtfs.model.User;
import xyz.shpasha.androidtfs.source.remote.DataRepository;

public class ProfileViewModel extends ViewModel {

    private LiveData<Resource<User>> userResource;
    private MutableLiveData userRequest = new MutableLiveData();

    private LiveData<Resource<String>> editResultResource;
    private MutableLiveData<User> editRequest = new MutableLiveData<>();

    private DataRepository dataRepository;

    public ProfileViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        initLiveData();
    }

    private void initLiveData() {
        userResource = Transformations.switchMap(userRequest, a -> dataRepository.getUser());
        editResultResource = Transformations.switchMap(editRequest, user -> dataRepository.edit(user));
    }

    void getUser() {
        userRequest.setValue(null);
    }

    LiveData<Resource<User>> userResource() {
        return userResource;
    }
    LiveData<Resource<String>> editResultResource() {
        return editResultResource;
    }

    void edit(User user) {
        editRequest.setValue(user);
    }
}
