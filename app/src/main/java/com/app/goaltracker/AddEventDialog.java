package com.app.goaltracker;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.goaltracker.databinding.DialogAddEventBinding;
import com.app.goaltracker.databinding.DialogAddGoalBinding;
import com.app.goaltracker.db.History;
import com.app.goaltracker.mvvm.GoalsViewModel;
import com.app.goaltracker.mvvm.HistoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddEventDialog extends DialogFragment {
       private int goalId;
        private ViewDataBinding binding;
        private HistoryViewModel historyViewModel;

    public AddEventDialog(int goalId, HistoryViewModel historyViewModel) {
        this.goalId = goalId;
        this.historyViewModel = historyViewModel;
    }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_event, container, false);
            return binding.getRoot();
        }

        @Override
        public void onResume() {
            super.onResume();
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            getDialog().getWindow().setLayout((95 * width) / 100, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            Button yesButton = view.findViewById(R.id.yes_button);
            Button noButton = view.findViewById(R.id.no_button);

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createEvent(true);
                    dismiss();
                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createEvent(false);
                    dismiss();
                }
            });
        }
    private void createEvent(boolean result) {
        History history = new History(goalId, result);
        historyViewModel.addHistory(history);
    }
}



