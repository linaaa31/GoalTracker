package com.app.goaltracker.ui.archive;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.goaltracker.R;
import com.app.goaltracker.db.Goal;
import com.app.goaltracker.mvvm.GoalsViewModel;
import com.app.goaltracker.ui.goals.GoalInfoActivity;

import java.util.List;

public class ArchiveFragment extends Fragment {
    private RecyclerView recyclerView;
   // private ArchivedGoalAdapter archivedGoalAdapter;
    private GoalsViewModel goalsViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archive, container, false);
        recyclerView = view.findViewById(R.id.archive_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //  archivedGoalAdapter = new ArchivedGoalAdapter();
        // recyclerView.setAdapter(archivedGoalAdapter);
        goalsViewModel = new ViewModelProvider(requireActivity()).get(GoalsViewModel.class);


        return view;
    }}
   // }

//    public void onResume() {
//        super.onResume();
//        loadArchivedGoals();
//    }

//    private void loadArchivedGoals() {
//        // Observe the archived goals LiveData from the ViewModel
//        goalsViewModel.getArchivedGoals().observe(getViewLifecycleOwner(), new Observer<List<Goal>>() {
//            @Override
//            public void onChanged(List<Goal> archivedGoals) {
//                archivedGoalAdapter.setGoals(archivedGoals);
//            }
//        });
//    }

//    private class ArchivedGoalAdapter extends RecyclerView.Adapter<ArchivedGoalAdapter.ViewHolder> {
//        private List<Goal> goals;
//
//        public void setGoals(List<Goal> goals) {
//            this.goals = goals;
//            notifyDataSetChanged();
//        }
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_archive_item, parent, false);
//            return new ViewHolder(view);
//        }
//
////        @Override
////        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
////            Goal goal = goals.get(position);
////            holder.nameTextView.setText(goal.getName());
////            holder.statusTextView.setText(goal.getStatus());
////            holder.creationDateTextView.setText(goal.getCreationDate());
////            holder.archiveDateTextView.setText(goal.getArchiveDate());
////        }
//
//        @Override
//        public int getItemCount() {
//            return goals != null ? goals.size() : 0;
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            public TextView nameTextView;
//            public TextView statusTextView;
//            public TextView creationDateTextView;
//            public TextView archiveDateTextView;
//
//            public ViewHolder(View itemView) {
//                super(itemView);
//                nameTextView = itemView.findViewById(R.id.name_text);
//                statusTextView = itemView.findViewById(R.id.status_text);
//                creationDateTextView = itemView.findViewById(R.id.creation_date);
//                archiveDateTextView = itemView.findViewById(R.id.archive_date);
//            }
//        }
//    }
//}
//
//
