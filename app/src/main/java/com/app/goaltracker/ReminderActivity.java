package com.app.goaltracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.goaltracker.db.Goal;

import java.util.List;

public class ReminderActivity extends AppCompatActivity {
    private RecyclerView goalsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        goalsRecyclerView = findViewById(R.id.reminder_rv);
        goalsRecyclerView.setAdapter(new ReminderAdapter(this));

        GoalsViewModel goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);
        goalsViewModel.getGoals().observe(this, new Observer<List<Goal>>() {
            @Override
            public void onChanged(List<Goal> goals) {
                ((ReminderAdapter) goalsRecyclerView.getAdapter()).setList(goals);
            }
        });
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
        private List<Goal> goals;

        private Context context;
        GoalsViewModel goalsViewModel;

        public ReminderAdapter(Context context) {
            this.context = context;
            goalsViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(GoalsViewModel.class);
        }


        public void setList(List<Goal> goals) {
            this.goals = goals;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Goal currentGoal = goals.get(position);
            Goal goal = goals.get(position);
            holder.goalNameTextView.setText(goal.goalName);
            holder.yesButton.setOnClickListener(v -> {
                // Call the method to update the database with the result
                goalsViewModel.addHistory(currentGoal.goalId, true);
            });
            holder.noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goalsViewModel.addHistory(currentGoal.goalId, false);
                }
            });
        }

        @Override
        public int getItemCount() {
            return goals != null ? goals.size() : 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView goalNameTextView;
            TextView goalDescriptionTextView;
            Button yesButton;
            Button noButton;

            public ViewHolder(View itemView) {
                super(itemView);
                goalNameTextView = itemView.findViewById(R.id.goal_name_item);
                goalDescriptionTextView = itemView.findViewById(R.id.goal_desc_item);
                yesButton = itemView.findViewById(R.id.yes_button);
                noButton = itemView.findViewById(R.id.no_button);
            }
        }
    }
}