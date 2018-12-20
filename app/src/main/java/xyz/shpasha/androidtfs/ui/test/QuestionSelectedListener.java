package xyz.shpasha.androidtfs.ui.test;

import android.arch.lifecycle.LiveData;
import xyz.shpasha.androidtfs.model.ProblemResponseOnAnswer;
import xyz.shpasha.androidtfs.model.Resource;

interface QuestionSelectedListener {
    void onQuestionSelected(int i);
    void onCompleteClicked();
    LiveData<Resource<ProblemResponseOnAnswer>> onAnswerClicked(int problemId, String answers);
}