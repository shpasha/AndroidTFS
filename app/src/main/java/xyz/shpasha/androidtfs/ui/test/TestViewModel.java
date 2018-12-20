package xyz.shpasha.androidtfs.ui.test;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import xyz.shpasha.androidtfs.model.*;
import xyz.shpasha.androidtfs.source.remote.DataRepository;
import xyz.shpasha.androidtfs.utils.AbsentLiveData;

import java.util.List;

public class TestViewModel extends ViewModel {

    private LiveData<Resource<List<Problem>>> problemsResource;
    private MutableLiveData problemsRequest = new MutableLiveData<>();

    private LiveData<Resource<TestStatus>> testStatusResource;
    private MutableLiveData testStatusRequest = new MutableLiveData<>();

    private LiveData<Resource<ProblemResponseOnAnswer>> sendAnswerResultResource;
    private MutableLiveData<Answer> sendAnswerRequest = new MutableLiveData<>();

    private LiveData<Resource<Object>> startContestResource = new MutableLiveData<>();
    private MutableLiveData startContestRequest = new MutableLiveData();


    private DataRepository dataRepository;

    private String contestUrl;

    public TestViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        initLiveData();
    }

    void setContestUrl(String contestUrl) {
        this.contestUrl = contestUrl;
    }

    private void initLiveData() {
        problemsResource = Transformations.switchMap(problemsRequest, (a) -> dataRepository.getTestProblems(contestUrl));
        testStatusResource = Transformations.switchMap(testStatusRequest, (a) -> dataRepository.getTestStatus(contestUrl));
        sendAnswerResultResource = Transformations.switchMap(sendAnswerRequest, answer -> {
            if (answer == null) return AbsentLiveData.create();
            return dataRepository.sendAnswer(contestUrl, answer);
        });
        startContestResource = Transformations.switchMap(startContestRequest, (a) -> dataRepository.startContest(contestUrl));
    }

    void getProblems() {
        problemsRequest.setValue(null);
    }
    LiveData<Resource<List<Problem>>> problemsResource() {
        return problemsResource;
    }

    void getTestStatus() {testStatusRequest.setValue(null);}
    LiveData<Resource<TestStatus>> testStatusResource() {
        return testStatusResource;
    }

    void sendAnswer(int problemId, String answers) {sendAnswerRequest.setValue(new Answer(problemId, answers));}
    LiveData<Resource<ProblemResponseOnAnswer>> sendAnswerResultResource() {
        return sendAnswerResultResource;
    }

    void startContest() {startContestRequest.setValue(null);}
    LiveData<Resource<Object>> startContestResource() {
        return startContestResource;
    }

}
