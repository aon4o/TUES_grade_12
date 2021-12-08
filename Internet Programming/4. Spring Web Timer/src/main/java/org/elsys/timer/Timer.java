package org.elsys.timer;

import org.json.JSONObject;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Timer extends Thread {
    private final String name;
    private final LocalTime endTime;
    private final LocalTime zeroTime = LocalTime.of(0, 0, 0, 0);
    private boolean done = false;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Timer(JSONObject object) throws Exception {
        name = object.getString("name");
        LocalTime startTime = LocalTime.now();

        if (object.has("time"))
        {
            String[] times = object.getString("time").split(":");
            if (times.length != 3) {
                throw new Exception("'time' must be in the format 'HH:mm:ss'");
            }
            endTime = startTime
                    .plusHours(Integer.parseInt(times[0]))
                    .plusMinutes(Integer.parseInt(times[1]))
                    .plusSeconds(Integer.parseInt(times[2]));
        }
        else
        {
            int hours = 0, minutes = 0, seconds = 0;
            if (object.has("hours")) {
                hours = object.getInt("hours");
            }
            if (object.has("minutes")) {
                minutes = object.getInt("minutes");
            }
            if (object.has("seconds")) {
                seconds = object.getInt("seconds");
            }
            endTime = startTime
                    .plusHours(hours)
                    .plusMinutes(minutes)
                    .plusSeconds(seconds);
        }
    }

    public void run() {
        while (true) {
            if (Objects.equals(this.getTime(), "00:00:00")) {
                done = true;
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTimerName() {
        return name;
    }

    public String getTime() {
        if (done) {
            return formatter.format(zeroTime);
        }
        LocalTime timeNow = LocalTime.now();
        LocalTime remainingTime = endTime
                .minusHours(timeNow.getHour())
                .minusMinutes(timeNow.getMinute())
                .minusSeconds(timeNow.getSecond())
                .minusNanos(timeNow.getNano());
        return formatter.format(remainingTime);
    }

    public String getSeconds() {
        if (done) {
            return "0";
        }
        LocalTime timeNow = LocalTime.now();
        LocalTime remainingTime = endTime
                .minusHours(timeNow.getHour())
                .minusMinutes(timeNow.getMinute())
                .minusSeconds(timeNow.getSecond())
                .minusNanos(timeNow.getNano());
        int seconds = remainingTime.getHour() * 60 * 60 +
                remainingTime.getMinute() * 60 +
                remainingTime.getSecond();
        return String.valueOf(seconds);
    }

    public String[] getHMS() {
        String[] time = {"0", "0", "0"};
        if (done) {
            return time;
        }
        LocalTime timeNow = LocalTime.now();
        LocalTime remainingTime = endTime
                .minusHours(timeNow.getHour())
                .minusMinutes(timeNow.getMinute())
                .minusSeconds(timeNow.getSecond())
                .minusNanos(timeNow.getNano());
        time[0] = String.valueOf(remainingTime.getHour());
        time[1] = String.valueOf(remainingTime.getMinute());
        time[2] = String.valueOf(remainingTime.getSecond());
        return time;
    }

    public String isDone() {
        if (done) {
            return "yes";
        }
        return "no";
    }
}