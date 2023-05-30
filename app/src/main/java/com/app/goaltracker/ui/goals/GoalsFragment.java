package com.app.goaltracker.ui.goals;

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
import com.app.goaltracker.mvvm.GoalsViewModel;
import com.app.goaltracker.R;

import com.app.goaltracker.db.Goal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class GoalsFragment extends Fragment {
    private GoalAdapter goalAdapter;
    private GoalsViewModel goalsViewModel;
    private SearchView searchView;
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
    private void observeGoals() {
        goalsViewModel.getGoals().observe(getViewLifecycleOwner(), new Observer<List<Goal>>() {
            @Override
            public void onChanged(List<Goal> goals) {
                if (goals != null && !goals.isEmpty()) {
                    noGoalsMessageTextView.setVisibility(View.GONE);
                } else {
                    noGoalsMessageTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_goals, container, false);
        searchView = root.findViewById(R.id.search_view);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        RecyclerView recyclerView = root.findViewById(R.id.goal_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        goalAdapter = new GoalAdapter();
        recyclerView.setAdapter(goalAdapter);

        goalsViewModel = new ViewModelProvider(requireActivity()).get(GoalsViewModel.class);
        goalsViewModel.getGoals().observe(getViewLifecycleOwner(), goals -> {
            goalAdapter.setGoalList(goals);
        });


        FloatingActionButton fab = root.findViewById(R.id.add_goal);
        fab.setOnClickListener(view -> {
            AddGoalDialog addGoalDialog = new AddGoalDialog();
            addGoalDialog.show(getChildFragmentManager(), "add_goal");
        });
        observeGoals();
        return root;
    }

    private void filterList(String text) {
        List<Goal> filteredList = new ArrayList<>();
        List<Goal> goalList = goalsViewModel.getGoals().getValue();
        //if(goalList!=null) {
            for (Goal goal : goalList) {
                if (goal.goalName.toLowerCase().startsWith(text.toLowerCase())) {
                    filteredList.add(goal);
                }
            }
       // }
        if(filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No result found", Toast.LENGTH_SHORT).show();
        }else{
            goalAdapter.setFilteredList(filteredList);
        }
    }

    private class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalCardHolder> {
         List<Goal> goalList;

        public void setGoalList(List<Goal> goalList) {
           // this.goalList = goalList;
            this.goalList = new ArrayList<>(goalList);
            notifyDataSetChanged();
        }
        public void updateProgressBar(int position, int progress) {
            Goal goal = goalList.get(position);
            if (goal != null) {
                goal.setProgress(progress);
                notifyItemChanged(position);
            }
        }

        public void setFilteredList(List<Goal> filteredList){
            this.goalList = filteredList;
            notifyDataSetChanged();
        }
        public GoalAdapter.GoalCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            return new GoalAdapter.GoalCardHolder(inflater.inflate(R.layout.view_goal_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder (@NonNull GoalAdapter.GoalCardHolder holder,int position) {
            List<String> hours = goalList.get(position).getHours();

            holder.nameText.setText(goalList.get(position).goalName);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String creationDate = dateFormat.format(goalList.get(position).creationDate);
            holder.creationDateText.setText(creationDate);

            holder.hoursLayout.removeAllViews();
            for (String hour : hours) {
                TextView hourTextView = new TextView(holder.itemView.getContext());
                hourTextView.setText(hour);
               // holder.hoursLayout.addView(hourTextView);
            }

            Integer completedEventCount = goalList.get(position).getCompletedEventCount();
            Integer totalEventCount = goalList.get(position).getEventCount();

            if (completedEventCount != null && totalEventCount != null) {
                String eventStatus = completedEventCount + "/" + totalEventCount;
                holder.statusText.setText(eventStatus);
            } else {
                holder.statusText.setText("0/0");
            }
              holder.progressBar.setProgress(goalList.get(position).getProgress()!=null? goalList.get(position).getProgress():0);

//            holder.nextReminder.setText(getString(R.string.label_next_reminder, goalList.get(position).periodHours));
        }


        @Override
        public int getItemCount() {
            return goalList != null ? goalList.size() : 0;
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
                        Goal goal = goalList.get(position);
                        Context context = v.getContext();
                        Intent intent = new Intent(context, GoalInfoActivity.class);
                        intent.putExtra("goal_id", goal.goalId);
                        intent.putExtra("goal_name", goal.goalName);
                        long creationDateMillis = goalList.get(position).creationDate.getTime();
                        intent.putExtra("creation_date", creationDateMillis);
                        Integer completedEventCount = goal.getCompletedEventCount();
                        Integer eventCount = goal.getEventCount();
                        String eventStatus;
                        if(completedEventCount == null && eventCount ==null){
                            eventStatus="0/0";
                        }else {
                            eventStatus = completedEventCount + "/" + eventCount;
                        }
                        intent.putExtra("event_status", eventStatus);

                        intent.putStringArrayListExtra("reminder_hours", new ArrayList<>(goal.getHours()));
                      //  context.startActivity(intent);
                        infoLauncher.launch(intent);
                    }
                });



            }
        }
    }
}

//                view.setOnClickListener(v -> {
//                    Intent i = new Intent(getActivity(), GoalInfoActivity.class);
//                    i.putExtra(Constants.GOAL_ID, goalList.get(getAdapterPosition()).goalId);
//                    i.putExtra(Constants.GOAL_NAME, goalList.get(getAdapterPosition()).goalName);
//                    infoLauncher.launch(i);
//                });