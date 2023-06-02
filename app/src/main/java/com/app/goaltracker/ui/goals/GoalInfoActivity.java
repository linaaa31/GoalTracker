package com.app.goaltracker.ui.goals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.goaltracker.AddEventDialog;
import com.app.goaltracker.Constants;
import com.app.goaltracker.databinding.ActivityGoalInfoBinding;
import com.app.goaltracker.db.GoalWithHistory;
import com.app.goaltracker.mvvm.GoalsViewModel;
import com.app.goaltracker.R;
import com.app.goaltracker.db.History;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GoalInfoActivity extends AppCompatActivity {
    private RecyclerView historyRecyclerView;
    private ActivityGoalInfoBinding binding;
    private ImageView hamburgerMenu;
    private ImageView backButton;
    private TextView creationDateTextView;
    private TextView eventStatusTextView;
    private LinearLayout hoursLayout;
    private SeekBar seekBar;
    private int progress;
    private SharedPreferences sharedPreferences;
    private GoalsViewModel goalsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoalInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        seekBar = findViewById(R.id.progress_slider);
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

        goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);
        goalsViewModel.selectGoal(goalId).observe(this, new Observer<GoalWithHistory>() {
            @Override
            public void onChanged(GoalWithHistory goalWithHistory) {
                ((HistoryAdapter) historyRecyclerView.getAdapter()).setList(goalWithHistory.historyList);
            }
        });

        binding.title.setText(getIntent().getStringExtra(Constants.GOAL_NAME));
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEventDialog addEventDialog = new AddEventDialog(goalId);
                addEventDialog.show(getSupportFragmentManager(),"add_event");
            }
        });

        creationDateTextView = findViewById(R.id.creation_date);
        eventStatusTextView= findViewById(R.id.status);
        hoursLayout= findViewById(R.id.hours_layout);
        Intent intent = getIntent();
        if (intent != null) {
            String eventStatusText = intent.getStringExtra("event_status");

            long creationDateMillis = intent.getLongExtra("creation_date", 0);
            Date creationDate = new Date(creationDateMillis);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedCreationDate = dateFormat.format(creationDate);
            creationDateTextView.setText(formattedCreationDate);
            eventStatusTextView.setText(eventStatusText);
            ArrayList<String> reminderHours = intent.getStringArrayListExtra("reminder_hours");
            if (reminderHours != null) {
                hoursLayout.removeAllViews();

                for (String hour : reminderHours) {
                    TextView hourTextView = new TextView(this);
                    hourTextView.setText(hour);
                    hoursLayout.addView(hourTextView);
                }
            }
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        progress = sharedPreferences.getInt("progress", 0);
        seekBar.setProgress(progress);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int newProgress, boolean fromUser) {
                progress = newProgress;
                Integer goalId = getIntent().getIntExtra(Constants.GOAL_ID, 0);
                goalsViewModel.updateGoalProgress(goalId, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("progress", progress);
                editor.apply();
                Toast.makeText(getApplicationContext(), "Final Progress: " + progress, Toast.LENGTH_SHORT).show();
            }
        });
    }
private void deleteGoal() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Confirm Delete")
            .setMessage("Are you sure you want to delete this goal?")
            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Integer goalId = getIntent().getIntExtra(Constants.GOAL_ID, 0);
                    goalsViewModel.deleteGoalById(goalId);
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

    private void archiveGoal() {
        Integer goalId = getIntent().getIntExtra(Constants.GOAL_ID, 0);
        goalsViewModel.archiveGoal(goalId);
        finish();

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
                        archiveGoal();
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
        private Integer goalId;

        public HistoryAdapter(Integer goalId) {
            this.goalId = goalId;
            historyList = new ArrayList<>();
        }

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