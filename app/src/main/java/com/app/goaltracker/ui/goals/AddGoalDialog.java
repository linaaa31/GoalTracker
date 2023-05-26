package com.app.goaltracker.ui.goals;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.goaltracker.db.Goal;
import com.app.goaltracker.mvvm.GoalsViewModel;
import com.app.goaltracker.R;
import com.app.goaltracker.databinding.DialogAddGoalBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class AddGoalDialog extends DialogFragment {
    private DialogAddGoalBinding binding;
    private GoalsViewModel goalsViewModel;
    private List<String> selectedHours;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_goal, container, false);
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_corners);
        }
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
        binding.addHour.setOnClickListener(v -> {
            showTimePickerDialog();
        });

      binding.addImage.setOnClickListener(v ->{
          showTimePickerDialog();
      });
        binding.goalAddButton.setOnClickListener(v -> {
            String goalName = binding.nameEditText.getText().toString();

            if (selectedHours.isEmpty()) {
                Toast.makeText(requireContext(), "Please select at least one hour", Toast.LENGTH_SHORT).show();
                return;
            }

            addGoal(goalName, selectedHours);
            dismiss();
        });
        goalsViewModel = new ViewModelProvider(requireActivity()).get(GoalsViewModel.class);
        selectedHours = new ArrayList<>();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    String formattedHour = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    selectedHours.add(formattedHour);
                    addHourView(formattedHour);
                },
                0,
                0,
                true
        );
        timePickerDialog.show();
    }

    private void addHourView(String hour) {
        TextView hourTextView = new TextView(requireContext());
        hourTextView.setText(hour);
        binding.hourContainer.addView(hourTextView);
    }

    private void addGoal(String goalName, List<String> hours) {
        goalsViewModel.addGoal(goalName, hours);
    }
}
