package com.jinternals.support.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SupportTicketService {

    private static final Map<String, List<Ticket>> tickets = new ConcurrentHashMap<>();

    public List<Ticket> getTickets(String userId) {
        log.debug("Fetching tickets for userId: {}", userId);
        return tickets.computeIfAbsent(userId, k -> new ArrayList<>());
    }

    public Ticket getTicket(String userId, String ticketId) {
        log.debug("Fetching ticket '{}' for userId: {}", ticketId, userId);
        return tickets.getOrDefault(userId, List.of()).stream()
                .filter(ticket -> ticket.id().equals(ticketId))
                .findFirst()
                .orElse(null);
    }

    public Ticket createTicket(String userId, Ticket ticket) {
        log.info("Creating ticket for userId: {}, ticketId: '{}', title: '{}', description: '{}'",
                userId, ticket.id(), ticket.title(), ticket.description());
        tickets.computeIfAbsent(userId, k -> new ArrayList<>()).add(ticket);
        return ticket;
    }
}
