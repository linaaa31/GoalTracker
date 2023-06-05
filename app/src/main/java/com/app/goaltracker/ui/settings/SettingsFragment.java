package com.app.goaltracker.ui.settings;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.goaltracker.R;
import com.app.goaltracker.databinding.FragmentSettingsBinding;

import java.util.Calendar;
import java.util.Locale;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private Button fromButton;
    private TextView startTimeTextView;
    private Button toButton;
    private TextView endTimeTextView;
    private DoNotDisturbHours doNotDisturbHours;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

       // binding =  FragmentSettingsBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        fromButton = view.findViewById(R.id.from_button);
        startTimeTextView = view.findViewById(R.id.start_time_tv);
        toButton = view.findViewById(R.id.to_button);
        endTimeTextView = view.findViewById(R.id.end_time_tv);


        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String startTime = sharedPreferences.getString("startTime", "00:00");
        String endTime = sharedPreferences.getString("endTime", "00:00");

        doNotDisturbHours = new DoNotDisturbHours();
        doNotDisturbHours.setStartTime(startTime);
        doNotDisturbHours.setEndTime(endTime);

        startTimeTextView.setText(startTime);
        endTimeTextView.setText(endTime);

        fromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(startTimeTextView);
            }
        });

        toButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(endTimeTextView);
            }
        });



//        View root = binding.getRoot();
//        return root;
        return view;

    }
    private void showTimePickerDialog(final TextView textView) {

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        textView.setText(selectedTime);
                        if (textView == startTimeTextView) {
                            doNotDisturbHours.setStartTime(selectedTime);
                        } else if (textView == endTimeTextView) {
                            doNotDisturbHours.setEndTime(selectedTime);
                        }
                    }
                },
                hour,
                minute,
                DateFormat.is24HourFormat(requireContext())
        );

        timePickerDialog.show();
    }


    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("startTime", doNotDisturbHours.getStartTime());
        editor.putString("endTime", doNotDisturbHours.getEndTime());
        editor.apply();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}