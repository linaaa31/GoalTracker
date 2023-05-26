package com.app.goaltracker.ui.goals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.app.goaltracker.AddEventDialog;
import com.app.goaltracker.Constants;
import com.app.goaltracker.MainActivity;
import com.app.goaltracker.databinding.ActivityGoalInfoBinding;
import com.app.goaltracker.db.AppDatabase;
import com.app.goaltracker.db.Goal;
import com.app.goaltracker.mvvm.GoalsViewModel;
import com.app.goaltracker.mvvm.HistoryViewModel;
import com.app.goaltracker.mvvm.HistoryViewModelFactory;
import com.app.goaltracker.R;
import com.app.goaltracker.db.History;
import androidx.fragment.app.FragmentManager;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GoalInfoActivity extends AppCompatActivity {
    private RecyclerView historyRecyclerView;
    private HistoryViewModel historyViewModel;
    private ActivityGoalInfoBinding binding;
    private ImageView hamburgerMenu;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoalInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hamburgerMenu = findViewById(R.id.hamburger_menu);

        hamburgerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        historyRecyclerView = findViewById(R.id.reminder_rv);

        Integer goalId = getIntent().getIntExtra(Constants.GOAL_ID, 0);
        historyRecyclerView.setAdapter(new HistoryAdapter(goalId));

        historyViewModel = new ViewModelProvider(this, new HistoryViewModelFactory(getApplication(), goalId)).get(HistoryViewModel.class);
        historyViewModel.getHistory().observe(this, new Observer<List<History>>() {
            @Override
            public void onChanged(List<History> history) {
                ((HistoryAdapter) historyRecyclerView.getAdapter()).setList(history);
            }
        });

        binding.title.setText(getIntent().getStringExtra(Constants.GOAL_NAME));
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEventDialog addEventDialog = new AddEventDialog(goalId,historyViewModel);
                addEventDialog.show(getSupportFragmentManager(),"add_event");


            }
        });
   }
private void deleteGoal() {
    GoalsViewModel goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Confirm Delete")
            .setMessage("Are you sure you want to delete this goal?")
            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Integer goalId = getIntent().getIntExtra(Constants.GOAL_ID, 0);
                    goalsViewModel.deleteGoalById(goalId);
                    // goalsViewModel.refreshGoalList();
                    finish();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .show();
}
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.main_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.delete_goal:
                    deleteGoal();
                    return true;
                    case R.id.archive_goal:


                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.show();
    }



    private class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

        private List<History> historyList;
        private Context context;
        private Integer goalId;

        public HistoryAdapter(Integer goalId) {
            this.goalId = goalId;
            historyList = new ArrayList<>();
        }


        //  public void setList(List<History> historyList) {
//            this.historyList = historyList;
//            notifyDataSetChanged();
//        }
        public void setList(List<History> historyList) {
            List<History> filteredList = new ArrayList<>();
             for (History history : historyList) {
             if (history.getGoalId() == goalId) {
            filteredList.add(history);
             }
             }
             this.historyList = filteredList;
                notifyDataSetChanged();
        }

        @NonNull
        @Override
        public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_history_item, parent, false);
            return new HistoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
            History history = historyList.get(position);
            holder.bind(history);
        }

        @Override
        public int getItemCount() {
            return historyList.size();
        }

        public class HistoryViewHolder extends RecyclerView.ViewHolder {
            private TextView resultTextView;
            private TextView hourTextView;

            HistoryViewHolder(@NonNull View itemView) {
                super(itemView);
                resultTextView = itemView.findViewById(R.id.event_result);
                hourTextView = itemView.findViewById(R.id.event_timestamp);
            }

            void bind(History history) {
                resultTextView.setText(history.result ? "Yes" : "No");
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String formattedTime = dateFormat.format(history.timestamp);
                hourTextView.setText(formattedTime);
            }

        }
    }

}