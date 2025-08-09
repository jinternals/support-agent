package com.jinternals.support.tools;

import com.jinternals.support.services.Priority;
import com.jinternals.support.services.SupportTicketService;
import com.jinternals.support.services.Ticket;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
            description = """
                    Create a new support ticket ONLY when the user reports a product/app issue that cannot be solved from known KB answers.
                    Preconditions:
                    - You MUST have a userId (UUID).
                    - If the user’s description is unclear or missing key details, ask a clarifying question BEFORE calling.
                    Arguments:
                    - title: short, specific summary (<=120 chars)
                    - summary: concrete details, steps to reproduce, expected vs actual (<=1000 chars)
                    - priority: one of LOW | MEDIUM | HIGH (default MEDIUM if omitted)
                    Output:
                    - Returns the created Ticket object { id, title, priority, summary }.
                    """
    )
    public Ticket createSupportTicket(
            @ToolParam(description = "Short, specific problem title (<=120 chars). Avoid vague titles like 'help'.")
            @NotBlank @Size(max = 120)
            String title,

            @ToolParam(description = "Concrete details: steps to reproduce, expected vs actual, error codes (<=1000 chars).")
            @NotBlank @Size(max = 1000)
            String summary,

            @ToolParam(description = "Urgency level. One of: LOW, MEDIUM, HIGH. Defaults to MEDIUM if not provided.")
            Priority priority, // allow null => default below

            @ToolParam(description = "Requester’s UUID. Do NOT invent. Use the given user id; if unknown, ask for it.")
            @NotBlank
            @Pattern(regexp = "^[0-9a-fA-F-]{36}$", message = "userId must be a UUID")
            String userId
    ) {
        Priority effectivePriority = (priority == null) ? Priority.MEDIUM : priority;

        Ticket ticket = new Ticket(UUID.randomUUID().toString(), title, effectivePriority, summary);
        supportTicketService.createTicket(userId, ticket);
        return ticket;
    }

    @Tool(
            name = "getSupportTicket",
            description = """
                    Fetch a specific support ticket by ticketId for a user. 
                    Use this when the user asks about status/details of a known ticket.
                    """
    )
    public Ticket getSupportTicket(
            @ToolParam(description = "Requester’s UUID that owns the ticket.")
            @NotBlank
            @Pattern(regexp = "^[0-9a-fA-F-]{36}$", message = "userId must be a UUID")
            String userId,

            @ToolParam(description = "Support ticket ID (UUID).")
            @NotBlank
            @Pattern(regexp = "^[0-9a-fA-F-]{36}$", message = "ticketId must be a UUID")
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
            description = """
                    List all support tickets for a user. Use when the user asks to review their open/recent tickets.
                    """
    )
    public List<Ticket> listSupportTickets(
            @ToolParam(description = "Requester’s UUID.")
            @NotBlank
            @Pattern(regexp = "^[0-9a-fA-F-]{36}$", message = "userId must be a UUID")
            String userId
    ) {
        return supportTicketService.getTickets(userId);
    }
}
