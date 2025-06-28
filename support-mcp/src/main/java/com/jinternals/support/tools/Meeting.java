package com.jinternals.support.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meeting {
    private String id;
    private String title;
    private Instant datetime;
    private String participants;
    private MeetingStatus status;
    private Integer duration; // Duration in minutes
}
