package com.app.goaltracker.db;



import androidx.room.TypeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListStringConverter {
    @TypeConverter
    public static String fromList(List<String> hours) {
        if (hours == null || hours.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String hour : hours) {
            sb.append(hour).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @TypeConverter
    public static List<String> toList(String hoursString) {
        if (hoursString == null || hoursString.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(hoursString.split(","));
    }
}

