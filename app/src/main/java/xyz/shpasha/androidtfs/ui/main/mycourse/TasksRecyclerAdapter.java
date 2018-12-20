package xyz.shpasha.androidtfs.ui.main.mycourse;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import xyz.shpasha.androidtfs.R;
import xyz.shpasha.androidtfs.model.Task;
import xyz.shpasha.androidtfs.ui.test.TestActivity;

import java.util.*;

public class TasksRecyclerAdapter extends RecyclerView.Adapter<TasksRecyclerAdapter.MyViewHolder> {

    private List<Task> taskList;
    private Context context;


    public TasksRecyclerAdapter(Context context, List<Task> taskList) {
        this.taskList = taskList;
        this.context= context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.tasks_recycler_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Task task = taskList.get(i);
        myViewHolder.taskTitleView.setText(task.getTask().getTitle());

        String contestStatus = task.getTask().getContestInfo().getContestStatus().getStatus();
        String taskStatus = task.getStatus();

        myViewHolder.container.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

        if (contestStatus.equals("ongoing")) {
            myViewHolder.statusView.setText(context.getString(R.string.test_going));
            myViewHolder.container.setCardBackgroundColor(context.getResources().getColor(R.color.colorOrange));
        } else
        if (contestStatus.equals("announcement")) {
            myViewHolder.statusView.setText(context.getString(R.string.test_soon_start));
        } else
        if (contestStatus.equals("contest_review") ) {
            if (taskStatus.equals("accepted")) {
                myViewHolder.container.setCardBackgroundColor(context.getResources().getColor(R.color.colorLiteGreen));
                myViewHolder.statusView.setText(context.getString(R.string.test_accepted));
            }
            else
                myViewHolder.statusView.setText(context.getString(R.string.statusOnCheck));
        }
        else {
            myViewHolder.statusView.setText("");
        }
        myViewHolder.markView.setText(task.getMark());
        myViewHolder.maxMarkView.setText(task.getTask().getMaxScore());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public List<Task> getData() {
        return taskList;
    }

    public void setData(List<Task> taskList) {
        this.taskList = taskList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView taskTitleView, statusView, maxMarkView, markView;
        private CardView container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            taskTitleView = itemView.findViewById(R.id.taskTitleView);
            statusView = itemView.findViewById(R.id.statusView);
            maxMarkView = itemView.findViewById(R.id.maxMarkView);
            markView = itemView.findViewById(R.id.markView);
            container.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Task task = taskList.get(getAdapterPosition());
            String status = task.getTask().getContestInfo().getContestStatus().getStatus();

            if (!status.equals("ongoing")) return;
            startTest(task.getTask().getContestInfo().getContestUrl());

            /*
            if (status.equals("ongoing"))
                startTest(task.getTask().getContestInfo().getContestUrl());
            else {
                new AlertDialog.Builder(context)
                        .setTitle("Тест")
                        .setMessage("Запустить тест?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                            startTest(task.getTask().getContestInfo().getContestUrl());
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
            */
        }

        private void startTest(String contestUrl) {
            Intent intent = new Intent(context, TestActivity.class);
            intent.putExtra("CONTEST_URL", contestUrl);
            context.startActivity(intent);
        }
    }
}
