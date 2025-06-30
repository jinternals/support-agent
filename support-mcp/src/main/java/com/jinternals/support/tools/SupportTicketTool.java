package com.jinternals.support.tools;

import com.jinternals.support.services.Priority;
import com.jinternals.support.services.SupportTicketService;
import com.jinternals.support.services.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Validated
public class SupportTicketTool {

    private final SupportTicketService supportTicketService;

    @Tool(
            name = "createSupportTicket",
            description = "Use this to create a support ticket when the user reports an issue that cannot be resolved using available knowledge base articles."
    )
    public String createSupportTicket(
            @ToolParam(description = "A clear and concise title of the issue.")
            String title,
            @ToolParam(description = "A brief summary providing more details about the issue mentioned in the title.")
            String summary,
            @ToolParam(description = "Priority of the issue. Must be one of: LOW, MEDIUM, HIGH.")
            String priority,
            @ToolParam(description = "The unique identifier of the user requesting support.")
            String userId
    ) {
        Priority parsedPriority;
        try {
            parsedPriority = priority == null || priority.isBlank()
                    ? Priority.MEDIUM
                    : Priority.valueOf(priority.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid priority: must be one of LOW, MEDIUM, HIGH");
        }

        Ticket ticket = new Ticket(UUID.randomUUID().toString(), title, parsedPriority, summary);
        supportTicketService.createTicket(userId, ticket);

        return String.format(
                "Ticket created: ID [%s], Title [%s], Priority [%s], User [%s]",
                ticket.id(), ticket.title(), ticket.priority(), userId
        );
    }

    @Tool(
            name = "getSupportTicket",
            description = "Fetch a specific support ticket by ticket ID and user ID."
    )
    public Ticket getSupportTicket(
            @ToolParam(description = "The unique identifier of the user who created the ticket.")
            String userId,
            @ToolParam(description = "The unique identifier of the support ticket.")
            String ticketId
    ) {
        Ticket ticket = supportTicketService.getTicket(userId, ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("No ticket found for given user and ticket ID.");
        }
        return ticket;
    }

    @Tool(
            name = "listSupportTickets",
            description = "List all support tickets for a given user."
    )
    public List<Ticket> listSupportTickets(
            @ToolParam(description = "The unique identifier of the user.")
            String userId
    ) {
        return supportTicketService.getTickets(userId);
    }
}
