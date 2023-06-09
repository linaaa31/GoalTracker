package com.app.goaltracker.ui.settings;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.goaltracker.R;
import com.app.goaltracker.databinding.FragmentSettingsBinding;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private Button fromButton;
    private TextView startTimeTextView;
    private Button toButton;
    private TextView endTimeTextView;
    private DoNotDisturbHours doNotDisturbHours;
    private TextView contact;
    private TextView share;
    private ImageView contactArrow;
    private ImageView shareArrow;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        fromButton = view.findViewById(R.id.from_button);
        startTimeTextView = view.findViewById(R.id.start_time_tv);
        toButton = view.findViewById(R.id.to_button);
        endTimeTextView = view.findViewById(R.id.end_time_tv);
        contact= view.findViewById(R.id.contact_us);
        share = view.findViewById(R.id.share);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String startTime = sharedPreferences.getString("startTime", "00:00");
        String endTime = sharedPreferences.getString("endTime", "00:00");

        doNotDisturbHours = new DoNotDisturbHours();
        doNotDisturbHours.setStartTime(startTime);
        doNotDisturbHours.setEndTime(endTime);

        startTimeTextView.setText(startTime);
        endTimeTextView.setText(endTime);
        contactArrow = view.findViewById(R.id.contact_arrow);
        shareArrow = view.findViewById(R.id.share_arrow);

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

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp();
            }
        });
        shareArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp();
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmailClient();
            }
        });
        contactArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmailClient();
            }
        });


        return view;

    }



    private void openEmailClient() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:linabadalyann@gmail.com"));

        startActivity(Intent.createChooser(emailIntent, "Choose an email client"));
    }
    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String appLink = "https://docs.google.com/document/d/1GAN4f8SN3AXmUQNkvLK7lOBO1VZhBYD2vcL2dgiFfhg/edit?usp=drive_link";
        String shareMessage = "Check out this cool app:\n" + appLink;
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

        startActivity(Intent.createChooser(shareIntent, "Share via"));
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