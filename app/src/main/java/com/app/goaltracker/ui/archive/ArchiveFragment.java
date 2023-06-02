package com.app.goaltracker.ui.archive;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.goaltracker.R;
import com.app.goaltracker.db.Goal;
import com.app.goaltracker.db.GoalWithHistory;
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
    private SearchView searchView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emptyArchive = view.findViewById(R.id.archive_empty);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archive, container, false);
        searchView = view.findViewById(R.id.search);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                goalsViewModel.setFilterText(newText);
                return true;
            }
        });
        archiveRecyclerView = view.findViewById(R.id.archive_list);
        archiveRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        archiveAdapter = new ArchiveAdapter();
        RecyclerView archiveRecyclerView = view.findViewById(R.id.archive_list);
        archiveRecyclerView.setAdapter(archiveAdapter);
        goalsViewModel = new ViewModelProvider(requireActivity()).get(GoalsViewModel.class);
        goalsViewModel.getArchivedGoals().observe(getViewLifecycleOwner(), archivedGoals -> {
            archiveAdapter.setArchiveList(archivedGoals);
            if (archivedGoals.size() > 0) {
                emptyArchive.setVisibility(View.GONE);
            } else {
                emptyArchive.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ArchiveViewHolder> {

        private List<GoalWithHistory> archivedGoals;

        public void setArchiveList(List<GoalWithHistory> archiveList) {
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
            GoalWithHistory goal = archivedGoals.get(position);
            holder.goalNameTextView.setText(archivedGoals.get(position).goal.goalName);
            holder.goalProgress.setProgress(archivedGoals.get(position).goal.getProgress() != null ? archivedGoals.get(position).goal.getProgress() : 0);

            Integer totalEventCount = goal.historyList.size();
            Integer completedEventCount = (int) goal.historyList.stream()
                    .filter(e -> e.result).count();

            if (completedEventCount != null && totalEventCount != null) {
                String eventStatus = completedEventCount + "/" + totalEventCount;
                holder.eventsStatusTextView.setText(eventStatus);
            } else {
                holder.eventsStatusTextView.setText("0/0");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String creationDate = dateFormat.format(archivedGoals.get(position).goal.creationDate);
            holder.creationDateTextView.setText(creationDate);

            SimpleDateFormat dateFormatArchive = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String archiveDate = dateFormatArchive.format(archivedGoals.get(position).goal.archiveDate);
            holder.archiveDateTextView.setText(archiveDate);
            holder.unarchiveButton.setOnClickListener(v -> {
                int goalId = goal.goal.getGoalId();
                goalsViewModel.unarchiveGoal(goalId);
            });
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










