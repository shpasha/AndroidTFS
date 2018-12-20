package xyz.shpasha.androidtfs.ui.test;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import xyz.shpasha.androidtfs.R;
import xyz.shpasha.androidtfs.model.Problem;
import xyz.shpasha.androidtfs.model.ProblemResponseOnAnswer;
import xyz.shpasha.androidtfs.model.Resource;

import java.util.ArrayList;
import java.util.List;

public class TestPageFragment extends Fragment {

    private static String PAGE_NUM = "page_num";
    private static String PROBLEM = "problem";

    private final String POSITION = "POSITION";
    private final String STATUS = "STATUS";
    private final String LASTSUBMISSION_FILE = "LASTSUBMISSION_FILE";
    private final String ANSWER_CHOICES = "ANSWER_CHOICES";
    private final String PROBLEM_TYPE = "PROBLEM_TYPE";
    private final String PROBLEM_TEXT = "PROBLEM_TEXT";

    private TextView answerWrittenView;

    private Integer position;
    private String lastSubmissions;
    private List<String> answerChoices;

    private List<Checkable> answerViews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static TestPageFragment newInstance(int pageNumber, Problem problem) {
        TestPageFragment pageFragment = new TestPageFragment();
        pageFragment.setMyArguments(problem);
        return pageFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.testpage_fragment, container, false);
        //problem = (Problem) getArguments().getSerializable(PROBLEM); // Подозрение на ошибку при сворачивании

        Bundle args = getArguments();
        position = args.getInt(POSITION);
        lastSubmissions = args.getString(LASTSUBMISSION_FILE);
        answerChoices = args.getStringArrayList(ANSWER_CHOICES);
        String problemType = args.getString(PROBLEM_TYPE);
        String text = args.getString(PROBLEM_TEXT);

        answerWrittenView = view.findViewById(R.id.answerWrittenView);
        TextView titleView = view.findViewById(R.id.titleView);
        Button sendAnswerButton = view.findViewById(R.id.sendAnswerButton);
        Button completeButton = view.findViewById(R.id.completeTestButton);
        LinearLayout answersViewLayout = view.findViewById(R.id.answersView);

        titleView.setText(Html.fromHtml(text));


        completeButton.setOnClickListener(v -> ((QuestionSelectedListener) getActivity()).onCompleteClicked());
        sendAnswerButton.setOnClickListener(v -> {
            StringBuilder answers = new StringBuilder();
            for (Checkable answerView : answerViews) {
                answers.append(answerView.isChecked() ? "1" : "0");
            }
            ((QuestionSelectedListener) getActivity()).onAnswerClicked(position, answers.toString()).observe(this, this::updateProblem);
        });

        String currentStates = null;
        if (lastSubmissions != null)
            currentStates = lastSubmissions;

        if (problemType.equals("SELECT_MULTIPLE")) {
            answerViews = placeCheckBoxesAndGetThem(answerChoices, currentStates, answersViewLayout);
        }
        if (problemType.equals("SELECT_ONE")) {
            answerViews = placeRadioButtonsAndGetThem(answerChoices, currentStates, answersViewLayout);
        }
        return view;
    }

    public void updateProblem(Resource<ProblemResponseOnAnswer> problemResponseOnAnswerResource) {
        switch (problemResponseOnAnswerResource.status) {
            case SUCCESS: {
                String status = problemResponseOnAnswerResource.data.getStatus();
                if (status != null && status.equals("AC") && position.equals(problemResponseOnAnswerResource.data.getProblemId())) {
                    lastSubmissions = problemResponseOnAnswerResource.data.getFile();
                    answerWrittenView.setTextColor(getContext().getResources().getColor(R.color.colorLiteGreen));
                    answerWrittenView.setVisibility(View.VISIBLE);
                    answerWrittenView.setText(getString(R.string.answer_wriiten));
                    //setMyArguments(problem);
                }
                break;
            }
            case ERROR: {
                if (position.equals(problemResponseOnAnswerResource.data.getProblemId())) {
                    answerWrittenView.setVisibility(View.VISIBLE);
                    answerWrittenView.setTextColor(getContext().getResources().getColor(R.color.red));
                    answerWrittenView.setText(problemResponseOnAnswerResource.message);
                }
                //answerWrittenView.setVisibility(View.GONE);
                break;
            }
        }
    }

    private void setMyArguments(Problem problem) {
        Bundle args = new Bundle();
        args.putInt(POSITION, problem.getPosition());
        args.putString(STATUS, problem.getStatus());
        if (problem.getLastSubmission() != null)
            args.putString(LASTSUBMISSION_FILE, problem.getLastSubmission().getFile());
        args.putStringArrayList(ANSWER_CHOICES, problem.getProblem().getAnswerChoices());
        args.putString(PROBLEM_TYPE, problem.getProblem().getProblemType());
        args.putString(PROBLEM_TEXT, problem.getProblem().getCmsPage().getUnstyledStatement());
        setArguments(args);
    }

    private List<Checkable> placeRadioButtonsAndGetThem(List<String> answerChoices, String currentStates, LinearLayout answersView) {
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RadioGroup radioGroup = new RadioGroup(getContext());
        List<Checkable> views = new ArrayList<>();
        for (int i = 0; i < answerChoices.size(); i++) {
            String answer = answerChoices.get(i);
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            radioButton.setLayoutParams(linearLayoutParams);
            float scale = getResources().getDisplayMetrics().density;
            int dp = (int) (15 * scale + 0.5f);
            radioButton.setPadding(dp, dp, dp, dp);
            radioButton.setTextColor(getContext().getResources().getColor(R.color.colorDarkGray));
            radioGroup.addView(radioButton);
            setColor(radioButton, R.color.colorPrimary);
            if (currentStates != null)
                radioButton.setChecked(currentStates.charAt(i) == '1');

            radioButton.setText(answer);
            views.add(radioButton);
        }

        answersView.addView(radioGroup);
        return views;
    }

    private List<Checkable> placeCheckBoxesAndGetThem(List<String> answerChoices, String currentStates, ViewGroup answersView) {
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        List<Checkable> views = new ArrayList<>();
        for (int i = 0; i < answerChoices.size(); i++) {
            String answer = answerChoices.get(i);
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            checkBox.setLayoutParams(linearLayoutParams);
            float scale = getResources().getDisplayMetrics().density;
            int dp = (int) (15 * scale + 0.5f);
            checkBox.setPadding(dp, dp, dp, dp);

            checkBox.setTextColor(getContext().getResources().getColor(R.color.colorDarkGray));

            setColor(checkBox, R.color.colorPrimary);
            if (currentStates != null)
                checkBox.setChecked(currentStates.charAt(i) == '1');


            checkBox.setText(answer);
            answersView.addView(checkBox);
            views.add(checkBox);
        }
        return views;
    }


    private void setColor(CompoundButton button, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            button.setButtonTintList(getContext().getColorStateList(colorId));
        }

    }

}
