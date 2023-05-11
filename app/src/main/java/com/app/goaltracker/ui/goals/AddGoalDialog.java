package com.app.goaltracker.ui.goals;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.goaltracker.mvvm.GoalsViewModel;
import com.app.goaltracker.R;
import com.app.goaltracker.databinding.DialogAddGoalBinding;

public class AddGoalDialog extends DialogFragment {
    private DialogAddGoalBinding binding;
    private GoalsViewModel goalsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_goal, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        getDialog().getWindow().setLayout((95 * width)/100, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.goalReminderPeriod.setMinValue(1);
        binding.goalReminderPeriod.setMaxValue(24);
        binding.goalReminderPeriod.setValue(4);

        goalsViewModel = new ViewModelProvider(requireActivity()).get(GoalsViewModel.class);

        binding.goalAddButton.setOnClickListener(v -> {
                goalsViewModel.addGoal(binding.nameEditText.getText().toString(), binding.goalReminderPeriod.getValue());
                dismiss();
            }
        );
    }
}
