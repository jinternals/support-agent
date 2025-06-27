package com.jinternals.support.services;

import com.jinternals.support.tools.Meeting;
import com.jinternals.support.tools.MeetingStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MeetingService {
    private final Map<String, List<Meeting>> scheduledMeetings = new ConcurrentHashMap<>();

    public Meeting schedule(String userId, Meeting meeting) {
        if (userId == null || meeting == null) {
            log.warn("⚠️ Invalid schedule attempt: userId or meeting is null");
            throw new IllegalArgumentException("userId and meeting must not be null");
        }
        scheduledMeetings.computeIfAbsent(userId, k -> new ArrayList<>()).add(meeting);
        log.info("✅ Scheduled meeting for user [{}]: {}", userId, meeting);
        return meeting;
    }

    public List<Meeting> getMeetings(String userId) {
        if (userId == null) {
            log.warn("⚠️ Attempted to retrieve meetings for null userId");
            return Collections.emptyList();
        }
        List<Meeting> meetings = scheduledMeetings.getOrDefault(userId, Collections.emptyList());
        log.info("📥 Retrieved {} meeting(s) for user [{}]", meetings.size(), userId);
        return meetings;
    }

    public List<Meeting> findMeetings(String userId, String title, String participants, Instant datetime) {
        if (userId == null) {
            log.warn("⚠️ Attempted to find meetings for null userId");
            return Collections.emptyList();
        }

        List<Meeting> meetings = scheduledMeetings.getOrDefault(userId, Collections.emptyList());
        if (title == null && participants == null && datetime == null) {
            log.info("📥 No search criteria provided, returning all meetings for user [{}]", userId);
            return meetings;
        }

        List<Meeting> filteredMeetings = meetings.stream()
                .filter(meeting -> meeting.getStatus() == MeetingStatus.SCHEDULED)
                .filter(meeting -> {
                    boolean matches = true;
                    if (title != null && !title.isBlank()) {
                        matches = matches && meeting.getTitle().toLowerCase().contains(title.toLowerCase());
                    }
                    if (participants != null && !participants.isBlank()) {
                        matches = matches && meeting.getParticipants() != null &&
                                Arrays.stream(participants.split(","))
                                        .map(String::trim)
                                        .allMatch(p -> meeting.getParticipants().toLowerCase().contains(p.toLowerCase()));
                    }
                    if (datetime != null) {
                        matches = matches && meeting.getDatetime().equals(datetime);
                    }
                    return matches;
                })
                .collect(Collectors.toList());

        log.info("🔍 Found {} meeting(s) for user [{}] with criteria title=[{}], participants=[{}], datetime=[{}]",
                filteredMeetings.size(), userId, title, participants, datetime);
        return filteredMeetings;
    }

    public String cancelMeetingForUser(String userId, String meetingId) {
        if (userId == null || meetingId == null) {
            log.warn("⚠️ Invalid cancellation attempt: userId or meetingId is null");
            return "Missing required parameters: userId or meetingId.";
        }

        List<Meeting> meetings = scheduledMeetings.get(userId);
        if (meetings == null || meetings.isEmpty()) {
            log.warn("❌ No meetings found for user [{}]", userId);
            return "No meetings found for user " + userId + ".";
        }

        log.info("🛠️ Attempting to cancel meetingId={} for userId={}", meetingId, userId);

        Optional<Meeting> meeting = meetings.stream().filter(m -> m.getId().equals(meetingId)).findFirst();
        if (meeting.isPresent()) {
            meeting.get().setStatus(MeetingStatus.CANCELLED);
            log.info("🗑️ Cancelled meeting [{}] for user [{}]", meetingId, userId);
            List<Meeting> updatedMeetings = getMeetings(userId);
            String meetingList = updatedMeetings.isEmpty() ? "No meetings scheduled." :
                    updatedMeetings.stream()
                            .map(m -> "- " + m.getTitle() + " on " + m.getDatetime() +
                                    (m.getParticipants() != null ? " with " + m.getParticipants() : "") +
                                    (m.getDuration() != null ? " (" + m.getDuration() + " minutes)" : ""))
                            .collect(Collectors.joining("\n"));
            return "✅ Meeting with ID " + meetingId + " has been cancelled.\nUpdated meetings:\n" + meetingList;
        } else {
            log.warn("⚠️ No matching meeting [{}] found for cancellation for user [{}]", meetingId, userId);
            return "No meeting found with ID " + meetingId + " for user " + userId + ".";
        }
    }
}