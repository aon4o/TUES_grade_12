package org.elsys.timer;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Result(int id,
                     String name,
                     String done,
                     String time,
                     String totalSeconds,
                     String hours,
                     String minutes,
                     String seconds) {
}
