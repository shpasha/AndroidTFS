package xyz.shpasha.androidtfs.ui.main.mycourse;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import xyz.shpasha.androidtfs.model.Homework;
import xyz.shpasha.androidtfs.model.Resource;
import xyz.shpasha.androidtfs.source.remote.DataRepository;

import java.util.List;

public class MyCourseViewModel extends ViewModel {

    private LiveData<Resource<List<Homework>>> homeworkListResource;
    private MutableLiveData homeworkListRequest = new MutableLiveData();

    private DataRepository dataRepository;

    public MyCourseViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        initLiveData();
    }

    private void initLiveData() {
        homeworkListResource = Transformations.switchMap(homeworkListRequest, (a) -> dataRepository.getHomeworks());
    }

    public void getHomeworks() {
        homeworkListRequest.setValue(null);
    }

    public LiveData<Resource<List<Homework>>> homeworkListResource() {
        return homeworkListResource;
    }
}
