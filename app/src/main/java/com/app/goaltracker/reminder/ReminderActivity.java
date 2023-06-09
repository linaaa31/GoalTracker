package com.app.goaltracker.reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.goaltracker.R;
import com.app.goaltracker.db.Goal;
import com.app.goaltracker.db.GoalWithHistory;
import com.app.goaltracker.db.History;
import com.app.goaltracker.mvvm.GoalsViewModel;
import com.app.goaltracker.ui.goals.GoalsFragment;

import java.util.ArrayList;
import java.util.List;

public class ReminderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;
    private GoalsViewModel goalsViewModel;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        recyclerView = findViewById(R.id.recycler_view);
        questionAdapter = new QuestionAdapter();
        recyclerView.setAdapter(questionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);

        goalsViewModel.getCurrentGoals().observe(this, new Observer<List<GoalWithHistory>>() {
            @Override
            public void onChanged(List<GoalWithHistory> currentGoals) {
                questionAdapter.setCurrentGoals(currentGoals);
            }
        });
        back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }


    private class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

        private List<GoalWithHistory> currentGoals;

        public QuestionAdapter() {
            currentGoals = new ArrayList<>();
        }

        public void setCurrentGoals(List<GoalWithHistory> currentGoals) {
            this.currentGoals = currentGoals;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
            return new QuestionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
            GoalWithHistory goalWithHistory = currentGoals.get(position);
            holder.bind(goalWithHistory);
        }

        @Override
        public int getItemCount() {
            return currentGoals.size();
        }

        public class QuestionViewHolder extends RecyclerView.ViewHolder {
            private TextView goalNameTextView;
            private Button yesButton;
            private Button noButton;

            QuestionViewHolder(@NonNull View itemView) {
                super(itemView);
                goalNameTextView = itemView.findViewById(R.id.goalNameTextView);
                yesButton = itemView.findViewById(R.id.yes_button);
                noButton = itemView.findViewById(R.id.no_button);
            }

            void bind(GoalWithHistory goalWithHistory) {
                Goal goal = goalWithHistory.goal;
                goalNameTextView.setText(goal.getGoalName());

                yesButton.setOnClickListener(v -> {

                    createEvent(goal, true);
                    hideButtons();
                });

                noButton.setOnClickListener(v -> {
                    createEvent(goal, false);
                    hideButtons();
                });
            }
            private void hideButtons() {
                yesButton.setVisibility(View.GONE);
                noButton.setVisibility(View.GONE);
                goalNameTextView.setVisibility(View.GONE);
            }

            private void createEvent(Goal goal, boolean result) {
                History history = new History(goal.getGoalId(), result);
                goalsViewModel.addHistory(history);
            }
        }
    }}

