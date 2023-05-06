package com.app.goaltracker.ui.goals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.goaltracker.R;

import com.app.goaltracker.db.Goal;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GoalsFragment extends Fragment {
    private GoalAdapter goalAdapter;
    private GoalsViewModel goalsViewModel;
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_goals, container, false);


            RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
            recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            goalAdapter = new GoalAdapter(requireContext());
            recyclerView.setAdapter(goalAdapter);
            recyclerView.scrollToPosition(0);

            goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);
            goalsViewModel.getGoals().observe(getViewLifecycleOwner(), goals -> {
                goalAdapter.setGoalList(goals);
            });

            FloatingActionButton fab = root.findViewById(R.id.addNewGoal);
            fab.setOnClickListener(view -> {
                showAddGoalDialog();
            });

            return root;
        }


    private void showAddGoalDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_goal, null);
        EditText goalNameEditText = dialogView.findViewById(R.id.name_edit_text);
        EditText goalDescriptionEditText = dialogView.findViewById(R.id.desc_edit_text);
        EditText periodEditText = dialogView.findViewById(R.id.period_edit_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.add_goal_title)
                .setView(dialogView)
                .setPositiveButton(R.string.submit_button, (dialog, which) -> {
                    String goalName = goalNameEditText.getText().toString().trim();
                    String goalDescription = goalDescriptionEditText.getText().toString().trim();
                    String period = periodEditText.getText().toString().trim();
                    if (!TextUtils.isEmpty(goalName)) {
                        goalsViewModel.addGoal(goalName, goalDescription, period);
                    } else {
                        Toast.makeText(requireContext(), R.string.empty_goal_error, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel_button, (dialog, which) -> {
                    dialog.cancel();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalCardHolder> {
        List<Goal> goalList;
        private Context context;

        public void setGoalList(List<Goal> goalList) {
           // this.goalList = goalList;
            this.goalList = new ArrayList<>(goalList);
            notifyDataSetChanged();
        }
        public GoalAdapter(Context context) {
            this.context = context;
        }

        public GoalAdapter.GoalCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            return new GoalAdapter.GoalCardHolder(inflater.inflate(R.layout.item_recycler_view, parent, false));
        }

        @Override
        public void onBindViewHolder (@NonNull GoalAdapter.GoalCardHolder holder,int position) {
        holder.nameText.setText(goalList.get(position).getGoalName());
        holder.descriptionText.setText(goalList.get(position).getGoalDescription());
        holder.periodText.setText(goalList.get(position).getPeriod());

        }


        @Override
        public int getItemCount() {
            return goalList != null ? goalList.size() : 0;
        }
        public class GoalCardHolder extends RecyclerView.ViewHolder {
            TextView nameText;
            TextView descriptionText;
            TextView periodText;
            ImageView delete;



            public GoalCardHolder(View view) {
                super(view);
                nameText = view.findViewById(R.id.name_tv);
                descriptionText = view.findViewById(R.id.description_tv);
                periodText = view.findViewById(R.id.period_tv);
                delete = view.findViewById(R.id.goal_delete);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goalsViewModel.deleteGoal(goalList.get(getAdapterPosition()));
                    }
                });


            }
        }
    }
}

//    private FragmentGoalsBinding binding;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
//
//        binding =FragmentGoalsBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//}