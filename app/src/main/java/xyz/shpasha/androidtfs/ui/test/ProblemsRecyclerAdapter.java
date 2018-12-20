package xyz.shpasha.androidtfs.ui.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import xyz.shpasha.androidtfs.R;

import java.util.List;

class ProblemsRecyclerAdapter extends RecyclerView.Adapter<ProblemsRecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<QustionState> qustionStateList;

    ProblemsRecyclerAdapter(Context context, List<QustionState> qustionStateList) {
        this.context = context;
        this.qustionStateList = qustionStateList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.problems_recycler_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        QustionState qustionState = qustionStateList.get(i);
        myViewHolder.questionNumView.setText(String.valueOf(i + 1));

        if (qustionState.isCurrent()) {
            myViewHolder.container.setCardBackgroundColor(context.getResources().getColor(R.color.colorOrange));
            return;
        }
        if (qustionState.isSent())
            myViewHolder.container.setCardBackgroundColor(context.getResources().getColor(R.color.colorLiteGreen));
        else
            myViewHolder.container.setCardBackgroundColor(context.getResources().getColor(R.color.colorItemDark));

    }

    @Override
    public int getItemCount() {
        return qustionStateList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView questionNumView;
        private CardView container;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            questionNumView = itemView.findViewById(R.id.questionNum);
            container = itemView.findViewById(R.id.container);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ((QuestionSelectedListener)context).onQuestionSelected(getAdapterPosition());
        }
    }
}
