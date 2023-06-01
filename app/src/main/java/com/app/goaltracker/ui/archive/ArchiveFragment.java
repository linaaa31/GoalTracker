package com.app.goaltracker.ui.archive;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.goaltracker.R;
import com.app.goaltracker.db.Goal;
import com.app.goaltracker.mvvm.GoalsViewModel;
import com.app.goaltracker.ui.goals.GoalInfoActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArchiveFragment extends Fragment  {

    private GoalsViewModel goalsViewModel;
    private RecyclerView archiveRecyclerView;
    private ArchiveAdapter archiveAdapter;
    private TextView emptyArchive;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emptyArchive = view.findViewById(R.id.archive_empty);
    }
    private void observeGoals() {
        goalsViewModel.getGoals().observe(getViewLifecycleOwner(), new Observer<List<Goal>>() {
            @Override
            public void onChanged(List<Goal> goals) {
                boolean hasArchivedGoals = false;
                if (goals != null) {
                    for (Goal goal : goals) {
                        if (goal.getArchived() == true) {
                            hasArchivedGoals = true;
                            break;
                        }
                    }
                }

                if (hasArchivedGoals) {
                    emptyArchive.setVisibility(View.GONE);
                } else {
                    emptyArchive.setVisibility(View.VISIBLE);

                }
            }


        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archive, container, false);
        archiveRecyclerView = view.findViewById(R.id.archive_list);
        archiveRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        archiveAdapter = new ArchiveAdapter();
        RecyclerView archiveRecyclerView = view.findViewById(R.id.archive_list);
        archiveRecyclerView.setAdapter(archiveAdapter);
      goalsViewModel = new ViewModelProvider(requireActivity()).get(GoalsViewModel.class);
       // goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);
        goalsViewModel.getArchivedGoals().observe(getViewLifecycleOwner(), archivedGoals -> {
            archiveAdapter.setArchiveList(archivedGoals);
        });


        observeGoals();
        return view;
    }



    private class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ArchiveViewHolder> {

        private List<Goal> archivedGoals;

        public void setArchiveList(List<Goal> archiveList) {
            this.archivedGoals = new ArrayList<>(archiveList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ArchiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_archive_item, parent, false);
            return new ArchiveViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull ArchiveViewHolder holder, int position) {
            Goal goal = archivedGoals.get(position);
            if (goal.getArchived() == false) {
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
                holder.goalNameTextView.setText(archivedGoals.get(position).goalName);
                holder.goalProgress.setProgress(archivedGoals.get(position).getProgress() != null ? archivedGoals.get(position).getProgress() : 0);

                String eventStatus = getEventStatus(archivedGoals.get(position));
                holder.eventsStatusTextView.setText(eventStatus);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String creationDate = dateFormat.format(archivedGoals.get(position).creationDate);
                holder.creationDateTextView.setText(creationDate);

                SimpleDateFormat dateFormatArchive = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String archiveDate = dateFormatArchive.format(archivedGoals.get(position).archiveDate);
                holder.archiveDateTextView.setText(archiveDate);
                holder.unarchiveButton.setOnClickListener(v -> {

                    int goalId = goal.getGoalId();
                    goalsViewModel.unarchiveGoal(goalId);
                    goalsViewModel.refreshGoalList();
                });
            }
        }


        private String getEventStatus(Goal goal) {
            Integer completedEventCount = goal.getCompletedEventCount();
            Integer totalEventCount = goal.getEventCount();

            if (completedEventCount != null && totalEventCount != null) {
                return completedEventCount + "/" + totalEventCount;
            } else {
                return "0/0";
            }
        }

      @Override
      public int getItemCount() {
          if (archivedGoals != null) {
              return archivedGoals.size();
          } else {
              return 0;
          }
      }
        class ArchiveViewHolder extends RecyclerView.ViewHolder {
            TextView goalNameTextView;
            ProgressBar goalProgress;
            TextView eventsStatusTextView;
            TextView creationDateTextView;
            TextView archiveDateTextView;
            ImageView unarchiveButton;

            ArchiveViewHolder(@NonNull View itemView) {
                super(itemView);
                goalNameTextView = itemView.findViewById(R.id.name_text);
                goalProgress = itemView.findViewById(R.id.progress_bar);
                eventsStatusTextView = itemView.findViewById(R.id.status_text);
                creationDateTextView = itemView.findViewById(R.id.creation_date);
                archiveDateTextView = itemView.findViewById(R.id.archive_date);
                unarchiveButton = itemView.findViewById(R.id.unarchive_bt);

            }
        }
    }
}










