package xyz.shpasha.androidtfs.ui.main.mycourse;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import xyz.shpasha.androidtfs.R;
import xyz.shpasha.androidtfs.model.Homework;
import xyz.shpasha.androidtfs.model.Task;
import xyz.shpasha.androidtfs.source.remote.DataRepository;
import xyz.shpasha.androidtfs.ui.main.callbacks.ErrorListener;
import xyz.shpasha.androidtfs.utils.ItemOffsetDecorator;
import xyz.shpasha.androidtfs.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class MyCourseFragment extends Fragment {

    private MyCourseViewModel myCourseViewModel;
    private RecyclerView tasksRecyclerView;
    private TasksRecyclerAdapter tasksRecyclerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ErrorListener errorListener;

    private List<Task> taskList = new ArrayList<>();

    public static MyCourseFragment newInstance() {
        return new MyCourseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_course_fragment, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        tasksRecyclerView = view.findViewById(R.id.tasksRecyclerView);
        tasksRecyclerAdapter = new TasksRecyclerAdapter(getContext(), taskList);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        tasksRecyclerView.setAdapter(tasksRecyclerAdapter);
        tasksRecyclerView.addItemDecoration(new ItemOffsetDecorator(5));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myCourseViewModel = ViewModelProviders.of(this, new ViewModelFactory(DataRepository.getInstance())).get(MyCourseViewModel.class);
        myCourseViewModel.homeworkListResource().observe(this, homeworksResource -> {
            if (homeworksResource == null) {onError("Unknown error"); return; }
            switch (homeworksResource.status) {
                case SUCCESS: {
                    onSuccess(homeworksResource.data);
                    break;
                }
                case LOADING: {
                    swipeRefreshLayout.setRefreshing(true);
                    break;
                }
                case ERROR: {
                    onError(homeworksResource.message);
                    break;
                }
            }
        });

        if (taskList == null || taskList.size() == 0) {
            myCourseViewModel.getHomeworks();
        }

        swipeRefreshLayout.setOnRefreshListener(() -> myCourseViewModel.getHomeworks());

    }

    private void onSuccess(List<Homework> homeworkList) {
        swipeRefreshLayout.setRefreshing(false);
        if (homeworkList == null) return;

        List<Task> taskList = getTasksFromHomeworks(homeworkList);
        sortTasks(taskList);
        updateRecycler(taskList);
    }

    private void updateRecycler(List<Task> newTaskList) {
        HomeworkListDiffUtilCallback productDiffUtilCallback = new HomeworkListDiffUtilCallback (tasksRecyclerAdapter.getData(), newTaskList);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
        this.taskList = newTaskList;
        tasksRecyclerAdapter.setData(taskList);
        productDiffResult.dispatchUpdatesTo(tasksRecyclerAdapter);
        tasksRecyclerView.scrollToPosition(findScrollPosition());
    }

    private List<Task> getTasksFromHomeworks(List<Homework> homeworkList) {
        List<Task> taskList = new ArrayList<>();
        for (Homework homework : homeworkList) {
            for (Task task : homework.getTasks()) {
                if (task.getTask().getTaskType().equals("test_during_lecture"))
                    taskList.add(task);
            }
        }
        return taskList;
    }

    private int findScrollPosition() {
        int scrollTo;
        for (scrollTo = 0; scrollTo < taskList.size(); scrollTo++) {
            if (taskList.get(scrollTo).getTask().getContestInfo().getContestStatus().getStatus().equals("ongoing")) {
                break;
            }
        }
        return scrollTo < this.taskList.size() ? scrollTo : 0;
    }

    private void onError(String message) {
        swipeRefreshLayout.setRefreshing(false);
        errorListener.onError(message);
    }

    private void sortTasks(List<Task> tasks) {
        List<Task> sortedTasks = new ArrayList<>();
        for (Task t : tasks) {
            String st = t.getTask().getContestInfo().getContestStatus().getStatus();
            if (st.equals("announcement"))
                sortedTasks.add(t);
        }
        for (Task t : tasks) {
            String st = t.getTask().getContestInfo().getContestStatus().getStatus();
            if (st.equals("ongoing"))
                sortedTasks.add(t);
        }
        for (Task t : tasks) {
            String st = t.getTask().getContestInfo().getContestStatus().getStatus();
            if (st.equals("contest_review"))
                sortedTasks.add(t);
        }
        tasks.clear();
        tasks.addAll(sortedTasks);
        /*Collections.sort(taskList, (o1, o2) -> {
            String st1 = o1.getTask().getContestInfo().getContestStatus().getStatus();
            String st2 = o2.getTask().getContestInfo().getContestStatus().getStatus();
            if (st1.equals("announcement")) return -1;
            if (st2.equals("announcement")) return 1;
            if (st1.equals("ongoing")) return -1;
            if (st2.equals("ongoing")) return 1;
            return 0;
        });*/
    }

    public void provideErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }
}
