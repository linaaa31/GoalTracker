package com.app.goaltracker.ui.goals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.goaltracker.Constants;
import com.app.goaltracker.db.GoalWithHistory;
import com.app.goaltracker.mvvm.GoalsViewModel;
import com.app.goaltracker.R;

import com.app.goaltracker.db.Goal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class GoalsFragment extends Fragment {
    private GoalAdapter goalAdapter;
    private GoalsViewModel goalsViewModel;
    private TextView noGoalsMessageTextView;
    ActivityResultLauncher<Intent> infoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
               goalsViewModel.refreshGoalList();
            });
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noGoalsMessageTextView = view.findViewById(R.id.no_goals_message);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_goals, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.goal_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        goalAdapter = new GoalAdapter();
        recyclerView.setAdapter(goalAdapter);

        goalsViewModel = new ViewModelProvider(requireActivity()).get(GoalsViewModel.class);
        goalsViewModel.getLiveGoals().observe(getViewLifecycleOwner(), goals -> {
            goalAdapter.setGoalList(goals);
            if (goals.size() > 0) {
                noGoalsMessageTextView.setVisibility(View.GONE);
            } else {
                noGoalsMessageTextView.setVisibility(View.VISIBLE);
                noGoalsMessageTextView.setText("Tap + icon below to create a goal");
            }
        });


        FloatingActionButton fab = root.findViewById(R.id.add_goal);
        fab.setOnClickListener(view -> {
            AddGoalDialog addGoalDialog = new AddGoalDialog();
            addGoalDialog.show(getChildFragmentManager(), "add_goal");
        });
        return root;
    }

    private class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalCardHolder> {
         List<GoalWithHistory> goalList;

        public void setGoalList(List<GoalWithHistory> goalList) {
            this.goalList = goalList;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return goalList != null ? goalList.size() : 0;
        }

        public GoalAdapter.GoalCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            return new GoalAdapter.GoalCardHolder(inflater.inflate(R.layout.view_goal_list_item, parent, false));
        }


        @Override
        public void onBindViewHolder (@NonNull GoalAdapter.GoalCardHolder holder,int position) {
            GoalWithHistory goal = goalList.get(position);
            holder.itemView.setVisibility(View.VISIBLE);

            List<String> hours = goalList.get(position).goal.getHours();

            holder.nameText.setText(goalList.get(position).goal.goalName);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String creationDate = dateFormat.format(goalList.get(position).goal.creationDate);
            holder.creationDateText.setText(creationDate);

            holder.hoursLayout.removeAllViews();
            for (String hour : hours) {
                TextView hourTextView = new TextView(holder.itemView.getContext());
                hourTextView.setText(hour);
            }

            Integer totalEventCount = goalList.get(position).historyList.size();
            Integer completedEventCount = (int) goalList.get(position).historyList.stream()
                    .filter(e -> e.result).count();

            if (completedEventCount != null && totalEventCount != null) {
                String eventStatus = completedEventCount + "/" + totalEventCount;
                holder.statusText.setText(eventStatus);
            } else {
                holder.statusText.setText("0/0");
            }
            holder.progressBar.setProgress(goalList.get(position).goal.getProgress() != null ? goalList.get(position).goal.getProgress() : 0);
        }

        public class GoalCardHolder extends RecyclerView.ViewHolder {
            TextView nameText;
            LinearLayout hoursLayout;
            TextView creationDateText;
            TextView statusText;
            ProgressBar progressBar;


            public GoalCardHolder(View view) {
                super(view);
                nameText = view.findViewById(R.id.name_tv);
                hoursLayout = view.findViewById(R.id.hours_layout);
                creationDateText = view.findViewById(R.id.creation_date_tv);
                statusText= view.findViewById(R.id.status_tv);
                progressBar = itemView.findViewById(R.id.progress_bar);
                view.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Goal goal = goalList.get(position).goal;
                        Context context = v.getContext();
                        Intent intent = new Intent(context, GoalInfoActivity.class);
                        intent.putExtra(Constants.GOAL_ID, goal.goalId);
                        intent.putExtra(Constants.GOAL_NAME, goal.goalName);
                        infoLauncher.launch(intent);
                    }
                });
            }
        }
    }
}
