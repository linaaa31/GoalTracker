package com.app.goaltracker.ui.goals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.goaltracker.Constants;
import com.app.goaltracker.mvvm.GoalsViewModel;
import com.app.goaltracker.R;

import com.app.goaltracker.db.Goal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class GoalsFragment extends Fragment {
    private GoalAdapter goalAdapter;
    private GoalsViewModel goalsViewModel;
    private SearchView searchView;
    ActivityResultLauncher<Intent> infoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                //goalsViewModel.refresh();
            });

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
            holder.nameText.setText(goalList.get(position).goalName);
            holder.nextReminder.setText(getString(R.string.label_next_reminder, goalList.get(position).periodHours));
        }


        @Override
        public int getItemCount() {
            return goalList != null ? goalList.size() : 0;
        }

        public class GoalCardHolder extends RecyclerView.ViewHolder {
            TextView nameText;
            TextView nextReminder;
            ImageView delete;


            public GoalCardHolder(View view) {
                super(view);
                nameText = view.findViewById(R.id.name_tv);
                nextReminder = view.findViewById(R.id.next_reminder_tv);
                delete = view.findViewById(R.id.goal_delete);

                delete.setOnClickListener(v -> goalsViewModel.deleteGoal(goalList.get(getAdapterPosition())));

                view.setOnClickListener(v -> {
                    Intent i = new Intent(getActivity(), GoalInfoActivity.class);
                    i.putExtra(Constants.GOAL_ID, goalList.get(getAdapterPosition()).goalId);
                    i.putExtra(Constants.GOAL_NAME, goalList.get(getAdapterPosition()).goalName);
                    infoLauncher.launch(i);
                });
            }
        }
    }
}
