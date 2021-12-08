package org.elsys.timer;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@RestController
public class Controller {
    final ArrayList<Timer> timers = new ArrayList<>();

    @GetMapping("/timer/{id}")
    public ResponseEntity index(@PathVariable String id,
                                @RequestHeader Map<String, String> headers,
                                @RequestParam(value = "long", required = false) String longFlag) {
        try {
            int idInt = Integer.parseInt(id);
            Timer timer = timers.get(idInt);

            longPolling(timer, longFlag);

            return ResponseEntity
                    .ok()
                    .header("ACTIVE-TIMERS", activeTimers())
                    .body(createResultGet(idInt, timer, headers));
        } catch (Exception e) {
            return ResponseEntity
                    .status(404)
                    .header("ACTIVE-TIMERS", activeTimers())
                    .body(e.getMessage());
        }
    }

    @PostMapping("/timer")
    public ResponseEntity createTimer(@RequestBody String args,
                                      @RequestHeader Map<String, String> headers,
                                      @RequestParam(value = "long", required = false) String longFlag) {
        try {
            JSONObject jsonTimer = new JSONObject(args);
            verifyJSON(jsonTimer);

            Timer timer = new Timer(jsonTimer);
            timer.start();
            timers.add(timer);

            int id = timers.size() - 1;

            longPolling(timer, longFlag);

            return ResponseEntity
                    .status(201)
                    .header("ACTIVE-TIMERS", activeTimers())
                    .body(createResultPost(id, timer, headers));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .header("ACTIVE-TIMERS", activeTimers())
                    .body(e.getMessage());
        }
    }

    private String activeTimers() {
        int activeTimers = 0;
        for (Timer timer : timers) {
            if (Objects.equals(timer.isDone(), "no")) {
                activeTimers++;
            }
        }
        return String.valueOf(activeTimers);
    }

    private void verifyJSON(JSONObject jsonTimer) throws Exception {
        if (!jsonTimer.has("name")) {
            throw new Exception("'name' must not be null!");
        }
        if (!jsonTimer.has("time")) {
            if (!jsonTimer.has("hours") && !jsonTimer.has("minutes") && !jsonTimer.has("seconds")) {
                throw new Exception("You must provide 'time' or at least one of 'hours', 'minutes', 'seconds'!");
            }
        } else {
            if (jsonTimer.has("hours") || jsonTimer.has("minutes") || jsonTimer.has("seconds")) {
                throw new Exception("You must provide only 'time' or as many as you want of 'hours', 'minutes', 'seconds'!");
            }
        }
    }

    private Result createResultPost(int id, Timer timer, Map<String, String> headers) {
        if (headers.containsKey("time-format")) {
            if (Objects.equals(headers.get("time-format"), "seconds")) {
                return new Result(id,
                        timer.getTimerName(),
                        null,
                        null,
                        timer.getSeconds(),
                        null,
                        null,
                        null);
            }
            else if (Objects.equals(headers.get("time-format"), "hours-minutes-seconds")) {
                String[] hms = timer.getHMS();
                return new Result(id,
                        timer.getTimerName(),
                        null,
                        null,
                        null,
                        hms[0],
                        hms[1],
                        hms[2]);
            }
            else {
                return new Result(id,
                        timer.getTimerName(),
                        null,
                        timer.getTime(),
                        null,
                        null,
                        null,
                        null);
            }
        } else {
            return new Result(id,
                    timer.getTimerName(),
                    null,
                    timer.getTime(),
                    null,
                    null,
                    null,
                    null);
        }
    }

    private Result createResultGet(int id, Timer timer, Map<String, String> headers) {
        if (headers.containsKey("time-format")) {
            if (Objects.equals(headers.get("time-format"), "seconds")) {
                return new Result(id,
                        timer.getTimerName(),
                        timer.isDone(),
                        null,
                        timer.getSeconds(),
                        null,
                        null,
                        null);
            }
            else if (Objects.equals(headers.get("time-format"), "hours-minutes-seconds")) {
                String[] hms = timer.getHMS();
                return new Result(id,
                        timer.getTimerName(),
                        timer.isDone(),
                        null,
                        null,
                        hms[0],
                        hms[1],
                        hms[2]);
            }
            else {
                return new Result(id,
                        timer.getTimerName(),
                        timer.isDone(),
                        timer.getTime(),
                        null,
                        null,
                        null,
                        null);
            }
        } else {
            return new Result(id,
                    timer.getTimerName(),
                    timer.isDone(),
                    timer.getTime(),
                    null,
                    null,
                    null,
                    null);
        }
    }

    private void longPolling (Timer timer, String longFlag) throws InterruptedException {
        if (Objects.equals(longFlag, "true")) {
            for (int i = 0; i < 20; i++) {
                Thread.sleep(500);
                if (Objects.equals(timer.isDone(), "yes")) {
                    break;
                }
            }
        }
    }
}
