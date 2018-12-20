package xyz.shpasha.androidtfs.ui.test;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import xyz.shpasha.androidtfs.R;
import xyz.shpasha.androidtfs.model.Problem;
import xyz.shpasha.androidtfs.model.ProblemResponseOnAnswer;
import xyz.shpasha.androidtfs.model.Resource;
import xyz.shpasha.androidtfs.model.TestStatus;
import xyz.shpasha.androidtfs.source.remote.DataRepository;
import xyz.shpasha.androidtfs.utils.ItemOffsetDecorator;
import xyz.shpasha.androidtfs.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestActivity extends AppCompatActivity implements QuestionSelectedListener {

    private ViewPager viewPager;
    private TestViewPagerAdapter testViewPagerAdapter;
    private TestViewModel testViewModel;
    private ProblemsRecyclerAdapter problemsRecyclerAdapter;
    private TextView questionNumView, currentQuestionNumView, lectureTitleView;
    private List<QustionState> questionStateList;
    private List<Problem> problems;
    private MutableLiveData<Resource<ProblemResponseOnAnswer>> answerStatusData;
    private ProgressBar progressBar;
    private String contestUrl;
    private int currentQuestion = 0;
    private StateFragment stateFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setSupportActionBar(findViewById(R.id.testToolbar));
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.test);

        contestUrl = getIntent().getStringExtra("CONTEST_URL");
        problems = new ArrayList<>();
        questionStateList = new ArrayList<>();
        answerStatusData = new MutableLiveData<>();

        initViews();
        initViewModel();
        initStateFragment();

        testViewModel.startContest();
    }

    private void initStateFragment() {
        stateFragment = StateFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, stateFragment)
                .commit();
    }

    private void initViews() {
        questionNumView = findViewById(R.id.questionNumView);
        currentQuestionNumView = findViewById(R.id.currentQuestionNumView);
        lectureTitleView = findViewById(R.id.lectureTitleView);
        progressBar = findViewById(R.id.progressBar);
        viewPager = findViewById(R.id.testViewPager);
        RecyclerView problemsRecyclerView = findViewById(R.id.problemsRecyclerView);

        testViewPagerAdapter = new TestViewPagerAdapter(getSupportFragmentManager(), problems);
        viewPager.setAdapter(testViewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                onQuestionSelected(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        problemsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        problemsRecyclerAdapter = new ProblemsRecyclerAdapter(this, questionStateList);
        problemsRecyclerView.setAdapter(problemsRecyclerAdapter);
        problemsRecyclerView.addItemDecoration(new ItemOffsetDecorator(10));
    }

    private void initViewModel() {
        testViewModel = ViewModelProviders.of(this, new ViewModelFactory(DataRepository.getInstance())).get(TestViewModel.class);

        testViewModel.setContestUrl(contestUrl);

        testViewModel.startContestResource().observe(this, startContestResource -> {
            if (startContestResource == null) {
                onError("Unknown error");
                return;
            }
            switch (startContestResource.status) {
                case SUCCESS: {
                    onStartContestSuccess();
                    break;
                }
                case LOADING: {
                    onLoading();
                    break;
                }
                case ERROR: {
                    onError(startContestResource.message);
                    break;
                }
            }
        });
        testViewModel.testStatusResource().observe(this, testStatusResource -> {
            if (testStatusResource == null) {
                onError("Unknown error");
                return;
            }
            switch (testStatusResource.status) {
                case SUCCESS: {
                    onTestStatusSuccess(testStatusResource.data);
                    break;
                }
                case LOADING: {
                    stateFragment.setLoading(true);
                    break;
                }
                case ERROR: {
                    stateFragment.setError(testStatusResource.message);
                    break;
                }
            }
        });
        testViewModel.problemsResource().observe(this, problemsResource -> {
            if (problemsResource == null) {
                onError("Unknown error");
                return;
            }
            switch (problemsResource.status) {
                case SUCCESS: {
                    onProblemSuccess(problemsResource.data);
                    break;
                }
                case LOADING: {
                    break;
                }
                case ERROR: {
                    onError(problemsResource.message);
                    break;
                }
            }
        });
        testViewModel.sendAnswerResultResource().observe(this, sendAnswerResultResource -> {
            if (sendAnswerResultResource == null) {
                onError("Unknown error");
                return;
            }
            switch (sendAnswerResultResource.status) {
                case SUCCESS: {
                    onAnswerSendSuccess(sendAnswerResultResource);
                    break;
                }
                case LOADING: {
                    break;
                }
                case ERROR: {
                    if (sendAnswerResultResource.data == null)
                        Toast.makeText(this, sendAnswerResultResource.message, Toast.LENGTH_SHORT).show();
                    else
                        answerStatusData.setValue(sendAnswerResultResource);
                    break;
                }
            }
        });

    }

    private void onAnswerSendSuccess(Resource<ProblemResponseOnAnswer> response) {
        if (response.data == null || problems == null || problems.size() == 0) return;
        String status = response.data.getStatus();
        answerStatusData.setValue(response);
        int i = response.data.getProblemId() - 1;
        problems.get(i).setStatus(status);
        questionStateList.get(i).setSent(true);
        problemsRecyclerAdapter.notifyItemChanged(i);
    }


    private void hideStatFragment() {
        getSupportFragmentManager().beginTransaction()
                .hide(stateFragment)
                .commit();
    }

    private void onProblemSuccess(List<Problem> problem) {
        hideStatFragment();

        problems.clear();
        problems.addAll(problem);
        testViewPagerAdapter.notifyDataSetChanged();
        questionStateList.clear();

        for (Problem p : problem) {
            QustionState qustionState = new QustionState();
            qustionState.setQuestionNum(p.getPosition());
            qustionState.setSent(p.getStatus() != null && p.getStatus().equals("AC"));
            questionStateList.add(qustionState);
        }
        questionStateList.get(viewPager.getCurrentItem()).setCurrent(true);
        problemsRecyclerAdapter.notifyDataSetChanged();

        currentQuestionNumView.setText("1");
        questionNumView.setText(String.valueOf(problems.size()));
        stateFragment.setLoading(false);
    }

    private void onTestStatusSuccess(TestStatus testStatus) {
        if (testStatus == null) return;
        testViewModel.getProblems();
        lectureTitleView.setText(testStatus.getContest().getTitle());
        startTimer(testStatus.getContest().getTimeLeft() * 1000, testStatus.getContest().getDuration() * 1000);
    }

    private void startTimer(long timeLeftMs, long durationMs) {
        if (timeLeftMs < 0 || durationMs < 0) return;
        CountDownTimer countDownTimer = new CountDownTimer(durationMs - timeLeftMs, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int startPercent = (int) (100 * timeLeftMs / durationMs);
                int curPercent = (int) ((100 - startPercent) * (durationMs - timeLeftMs - millisUntilFinished) / (durationMs - timeLeftMs));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progressBar.setProgress(startPercent + curPercent, true);
                } else {
                    progressBar.setProgress(startPercent + curPercent);
                }
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(100);
            }
        };
        countDownTimer.start();
    }

    private void onStartContestSuccess() {
        testViewModel.getTestStatus();
    }

    private void onError(String message) {
        stateFragment.setError(message);
    }

    private void onLoading() {
        stateFragment.setLoading(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onQuestionSelected(int i) {
        if (i >= testViewPagerAdapter.getCount() || i < 0 || i == currentQuestion) return;
        viewPager.setCurrentItem(i);
        questionStateList.get(currentQuestion).setCurrent(false);
        questionStateList.get(i).setCurrent(true);
        problemsRecyclerAdapter.notifyItemChanged(currentQuestion);
        problemsRecyclerAdapter.notifyItemChanged(i);
        currentQuestionNumView.setText(String.valueOf(i + 1));
        currentQuestion = i;
    }

    @Override
    public void onBackPressed() {
        int curIndex = viewPager.getCurrentItem();
        if (curIndex > 0) {
            onQuestionSelected(curIndex - 1);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onCompleteClicked() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.leave_test)
                .setMessage(R.string.confirm_test_quit)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> finish())
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public LiveData<Resource<ProblemResponseOnAnswer>> onAnswerClicked(int problemId, String answers) {
        testViewModel.sendAnswer(problemId, answers);
        return answerStatusData;
    }
}
