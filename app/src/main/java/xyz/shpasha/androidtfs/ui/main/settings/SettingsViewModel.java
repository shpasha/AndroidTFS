package xyz.shpasha.androidtfs.ui.main.settings;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import xyz.shpasha.androidtfs.model.Resource;
import xyz.shpasha.androidtfs.source.remote.DataRepository;

public class SettingsViewModel extends ViewModel {
    private LiveData<Resource<String>> signoutResource;
    private MutableLiveData signoutRequest = new MutableLiveData();

    private DataRepository dataRepository;

    public SettingsViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        initLiveData();
    }

    private void initLiveData() {
        signoutResource = Transformations.switchMap(signoutRequest, (a) -> dataRepository.signout());
    }

    public void signout() {
        signoutRequest.setValue(null);
    }

    public LiveData<Resource<String>> signoutResource() {
        return signoutResource;
    }
}
