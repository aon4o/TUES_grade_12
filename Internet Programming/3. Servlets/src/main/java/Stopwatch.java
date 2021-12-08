import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Stopwatch {
    private final LocalTime baseTime;
    private LocalTime lastLap;
    private final ArrayList<LocalTime> laps = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Stopwatch() {
        baseTime = LocalTime.now();
        laps.add(LocalTime.of(0, 0, 0, 0));
        lastLap = LocalTime.of(0, 0, 0, 0);
    }

    public String get() {
        return formatter.format(LocalTime.now()
                .minusHours(baseTime.getHour())
                .minusMinutes(baseTime.getMinute())
                .minusSeconds(baseTime.getSecond())
                .minusNanos(baseTime.getNano()));
    }

    public String lap() {
        LocalTime lap = LocalTime.now()
                .minusHours(baseTime.getHour() + lastLap.getHour())
                .minusMinutes(baseTime.getMinute() + lastLap.getMinute())
                .minusSeconds(baseTime.getSecond() + lastLap.getSecond())
                .minusNanos(baseTime.getNano() + lastLap.getNano());

        laps.add(lap);
        lastLap = lastLap
                .plusHours(lap.getHour())
                .plusMinutes(lap.getMinute())
                .plusSeconds(lap.getSecond())
                .plusNanos(lap.getNano());

        return formatter.format(lap);
    }

    public String stop() {
        this.lap();
        StringBuilder result = new StringBuilder();
        LocalTime currentTime = laps.remove(0);

        for (LocalTime lap : laps) {
            currentTime = currentTime
                    .plusHours(lap.getHour())
                    .plusMinutes(lap.getMinute())
                    .plusSeconds(lap.getSecond())
                    .plusNanos(lap.getNano());


            if (laps.indexOf(lap) < 9) {
                result.append("0");
            }
            result.append(laps.indexOf(lap) + 1).append(" ")
                    .append(formatter.format(lap)).append(" / ")
                    .append(formatter.format(currentTime)).append("\n");
        }

        return result.toString();
    }
}
