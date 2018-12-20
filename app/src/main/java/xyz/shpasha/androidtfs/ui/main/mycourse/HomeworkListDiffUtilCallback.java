package xyz.shpasha.androidtfs.ui.main.mycourse;

import android.support.v7.util.DiffUtil;
import xyz.shpasha.androidtfs.model.Task;

import java.util.List;

class HomeworkListDiffUtilCallback extends DiffUtil.Callback {

    private List<Task> taskListOld, taskListNew;

    public HomeworkListDiffUtilCallback(List<Task> oldList, List<Task> newList) {
        this.taskListOld = oldList;
        this.taskListNew = newList;
    }

    @Override
    public int getOldListSize() {
        return taskListOld.size();
    }

    @Override
    public int getNewListSize() {
        return taskListNew.size();
    }

    @Override
    public boolean areItemsTheSame(int i, int i1) {
        return taskListNew.get(i).getId() == taskListOld.get(i1).getId();
    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {
        Task oldTask = taskListOld.get(i);
        Task newTask = taskListNew.get(i1);
        return oldTask.getTask().getTitle().equals(newTask.getTask().getTitle()) &&
                oldTask.getTask().getContestInfo().getContestStatus().getStatus().equals(newTask.getTask().getContestInfo().getContestStatus().getStatus()) &&
                oldTask.getTask().getContestInfo().getContestUrl().equals(newTask.getTask().getContestInfo().getContestUrl()) &&
                oldTask.getMark().equals(newTask.getMark());
    }
}
