import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class Command {

    public String time(ArrayList<String> args) {
        HashMap<String, Integer> UTC_offsets = new HashMap<>();
        UTC_offsets.put("+14:00", (14 * 60));
        UTC_offsets.put("+13:00", (13 * 60));
        UTC_offsets.put("+12:45", (12 * 60 + 45));
        UTC_offsets.put("+12:00", (12 * 60));
        UTC_offsets.put("+11:00", (11 * 60));
        UTC_offsets.put("+10:30", (10 * 60 + 30));
        UTC_offsets.put("+10:00", (10 * 60));
        UTC_offsets.put("+09:30", (9 * 60 + 30));
        UTC_offsets.put("+09:00", (9 * 60));
        UTC_offsets.put("+08:45", (8 * 60 + 45));
        UTC_offsets.put("+08:00", (8 * 60));
        UTC_offsets.put("+07:00", (7 * 60));
        UTC_offsets.put("+06:30", (6 * 60 + 30));
        UTC_offsets.put("+06:00", (6 * 60));
        UTC_offsets.put("+05:45", (5 * 60 + 45));
        UTC_offsets.put("+05:30", (5 * 60 + 30));
        UTC_offsets.put("+05:00", (5 * 60));
        UTC_offsets.put("+04:30", (4 * 60 + 30));
        UTC_offsets.put("+04:00", (4 * 60));
        UTC_offsets.put("+03:30", (3 * 60 + 30));
        UTC_offsets.put("+03:00", (3 * 60));
        UTC_offsets.put("+02:00", (2 * 60));
        UTC_offsets.put("+01:00", (60));
        UTC_offsets.put("+00:00", 0);
        UTC_offsets.put("-01:00", (-1 * 60));
        UTC_offsets.put("-02:00", (-2 * 60));
        UTC_offsets.put("-03:00", (-3 * 60));
        UTC_offsets.put("-03:30", (-3 * 60 + 30));
        UTC_offsets.put("-04:00", (-4 * 60));
        UTC_offsets.put("-05:00", (-5 * 60));
        UTC_offsets.put("-06:00", (-6 * 60));
        UTC_offsets.put("-07:00", (-7 * 60));
        UTC_offsets.put("-08:00", (-8 * 60));
        UTC_offsets.put("-09:00", (-9 * 60));
        UTC_offsets.put("-09:30", (-9 * 60 + 30));
        UTC_offsets.put("-10:00", (-10 * 60));
        UTC_offsets.put("-11:00", (-11 * 60));
        UTC_offsets.put("-12:00", (-12 * 60));

        int current_offset = TimeZone.getDefault().getOffset(new Date().getTime()) / 1000 / 60;
        LocalTime time = LocalTime.now().minusMinutes(current_offset);

        if (args.size() == 1) {
            time = time.plusMinutes(Integer.parseInt(args.get(0)) / 1000 / 60);
            return DateTimeFormatter.ofPattern("HH:mm").format(time);
        }

        String time_zone = args.get(0);

        if (time_zone.length() != 6 || (!time_zone.contains("+") && !time_zone.contains("-"))
                || !time_zone.contains(":")) {
            return "invalid input";
        }

        if (!UTC_offsets.containsKey(args.get(0))) {
            return "invalid time zone";
        }

        time = time.plusMinutes(UTC_offsets.get(args.get(0)));

        return DateTimeFormatter.ofPattern("HH:mm").format(time);
    }
}
