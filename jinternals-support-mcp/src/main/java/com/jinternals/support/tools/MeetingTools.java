package com.jinternals.support.tools;

import com.jinternals.support.services.MeetingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class MeetingTools {

    private final MeetingService meetingService;

    @Tool(name = "scheduleMeetingForUser", description = "Creates and schedules a new meeting for the given user ID. All required details like title and datetime must be provided.")
    public String scheduleMeetingForUser(@ToolParam(description = "The unique ID of the user creating the meeting", required = true) String userId, @ToolParam(description = "Title or subject of the meeting (e.g., 'Project Sync')", required = true) String title, @ToolParam(description = "Meeting date and time in ISO-8601 UTC format (e.g., 2025-06-26T05:19:41Z)", required = true) String datetime, @ToolParam(description = "Comma-separated list of participants (e.g., 'Alice,Bob,Charlie')", required = false) String participants, @ToolParam(description = "Duration of the meeting in minutes (e.g., 60)", required = false) Integer duration) {
        if (userId == null || title == null || datetime == null) {
            return "Missing required parameters: userId, title, or datetime.";
        }

        try {
            Instant instant = Instant.parse(datetime); // ISO-8601 UTC format
            Meeting meeting = new Meeting(UUID.randomUUID().toString(), title, instant, participants, MeetingStatus.SCHEDULED, duration != null ? duration : 60); // Default to 60 minutes if not provided
            meetingService.schedule(userId, meeting);
            return "✅ Meeting with ID " + meeting.getId() + " titled '" + title + "' scheduled for " + datetime + ".";
        } catch (DateTimeParseException e) {
            return "Failed to parse datetime. Please use ISO-8601 UTC format (e.g., 2025-06-26T05:19:41Z).";
        }
    }

    @Tool(name = "getUserMeetings", description = "Retrieves a list of all meetings scheduled by the specified user ID.")
    public List<Meeting> getUserMeetings(@ToolParam(description = "The user ID whose meetings should be retrieved", required = true) String userId) {
        if (userId == null) {
            return List.of(); // Return empty list for invalid userId
        }
        return meetingService.getMeetings(userId);
    }

    @Tool(name = "findMeetingsByCriteria", description = "Finds meetings for a user by matching title, participants, or datetime.")
    public List<Meeting> findMeetingsByCriteria(
            @ToolParam(description = "The user ID whose meetings should be searched", required = true) String userId,
            @ToolParam(description = "Title or subject of the meeting (partial match, case-insensitive)", required = false) String title,
            @ToolParam(description = "Comma-separated list of participants (e.g., 'Alice,Bob')", required = false) String participants,
            @ToolParam(description = "Meeting date and time in ISO-8601 UTC format (e.g., 2025-06-26T05:19:41Z)", required = false) String datetime) {
        if (userId == null) {
            return List.of(); // Return empty list for invalid userId
        }
        try {
            Instant parsedDatetime = datetime != null &&  !datetime.isBlank() ? Instant.parse(datetime) : null;
            return meetingService.findMeetings(userId, title, participants, parsedDatetime);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return List.of(); // Return empty list for invalid datetime
        }
    }

    @Tool(name = "cancelMeetingForUser", description = "Cancels a meeting for a given user by matching the provided user ID and meeting ID.")
    public String cancelMeetingForUser(@ToolParam(description = "ID of the user requesting the cancellation", required = true) String userId, @ToolParam(description = "ID of the meeting to cancel", required = true) String meetingId) {
        if (userId == null || meetingId == null) {
            return "Missing required parameters: userId or meetingId.";
        }

        String result = meetingService.cancelMeetingForUser(userId, meetingId);
        if (result.startsWith("✅")) {
            List<Meeting> updatedMeetings = meetingService.getMeetings(userId);
            String meetingList = updatedMeetings.isEmpty() ? "No meetings scheduled." : updatedMeetings.stream().map(m -> "- " + m.getTitle() + " on " + m.getDatetime() + (m.getParticipants() != null ? " with " + m.getParticipants() : "")).collect(Collectors.joining("\n"));
            return result + "\nUpdated meetings:\n" + meetingList;
        }
        return result;
    }

    @Tool(name = "summarizeMeetings", description = "Summarizes a user's scheduled meetings in a human-readable format.")
    public String summarizeMeetings(@ToolParam(description = "The user ID whose meetings should be summarized", required = true) String userId) {
        if (userId == null) {
            return "User ID must not be empty.";
        }

        List<Meeting> meetings = meetingService.getMeetings(userId);
        if (meetings.isEmpty()) {
            return "📋 No scheduled meetings found.";
        }

        String summary = meetings.stream().filter(m -> m.getStatus() == MeetingStatus.SCHEDULED).map(m -> String.format("- %s on %s%s%s", m.getTitle(), m.getDatetime(), m.getParticipants() != null ? " with " + m.getParticipants() : "", m.getDuration() != null ? " (" + m.getDuration() + " minutes)" : "")).collect(Collectors.joining("\n"));
        return "📋 Your scheduled meetings:\n" + summary;
    }

}