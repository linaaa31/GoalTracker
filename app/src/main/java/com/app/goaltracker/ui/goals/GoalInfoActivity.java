package com.app.goaltracker.ui.goals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.goaltracker.Constants;
import com.app.goaltracker.databinding.ActivityGoalInfoBinding;
import com.app.goaltracker.mvvm.GoalsViewModel;
import com.app.goaltracker.mvvm.HistoryViewModel;
import com.app.goaltracker.mvvm.HistoryViewModelFactory;
import com.app.goaltracker.R;
import com.app.goaltracker.db.History;

import java.util.List;

public class GoalInfoActivity extends AppCompatActivity {
    private RecyclerView historyRecyclerView;
    private HistoryViewModel historyViewModel;
    private ActivityGoalInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoalInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        historyRecyclerView = findViewById(R.id.reminder_rv);
        historyRecyclerView.setAdapter(new ReminderAdapter());

        Integer goalId = getIntent().getIntExtra(Constants.GOAL_ID, 0);

        historyViewModel = new ViewModelProvider(this, new HistoryViewModelFactory(getApplication(), goalId)).get(HistoryViewModel.class);
        historyViewModel.getHistory().observe(this, new Observer<List<History>>() {
            @Override
            public void onChanged(List<History> history) {
                ((ReminderAdapter) historyRecyclerView.getAdapter()).setList(history);
            }
        });

        binding.title.setText(getIntent().getStringExtra(Constants.GOAL_NAME));
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyViewModel.addHistory(true);
            }
        });
    }

    private class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
        private List<History> historyList;


        public void setList(List<History> historyList) {
            this.historyList = historyList;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_history_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.eventTimestamp.setText(historyList.get(position).timestamp.toString());
            holder.eventResult.setText("Success + colored progress indicator");
        }

        @Override
        public int getItemCount() {
            return historyList != null ? historyList.size() : 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView eventTimestamp;
            TextView eventResult;

            public ViewHolder(View itemView) {
                super(itemView);
                eventTimestamp = itemView.findViewById(R.id.event_timestamp);
                eventResult = itemView.findViewById(R.id.event_result);
            }
        }
    }
}