package com.isro.pdfportal.controller;

import com.isro.pdfportal.config.CustomUserDetails;
import com.isro.pdfportal.entity.Ticket;
import com.isro.pdfportal.entity.User;
import com.isro.pdfportal.model.TicketStatus;
import com.isro.pdfportal.service.TicketService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Show create ticket form
    @GetMapping("/new")
    public String newTicket(Authentication authentication, Model model) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails details)) {
            return "redirect:/login";
        }

        User user = details.getUser();
        model.addAttribute("fullName", user.getFullName());
        return "ticket-new";
    }

    // Handle form submit
    @PostMapping
    public String createTicket(@RequestParam String title,
                               @RequestParam String description,
                               @RequestParam(defaultValue = "MEDIUM") String priority,
                               Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails details)) {
            return "redirect:/login";
        }

        User user = details.getUser();
        ticketService.createTicket(user, title, description, priority);

        // After creating ticket, go to "My tickets"
        return "redirect:/tickets/my";
    }

    // User's tickets + resolved notification
    @GetMapping("/my")
    public String myTickets(Authentication authentication, Model model) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails details)) {
            return "redirect:/login";
        }

        User user = details.getUser();
        List<Ticket> tickets = ticketService.getUserTickets(user);

        // ✅ Tickets that are resolved/closed – for notification banner
        List<Ticket> resolvedTickets = tickets.stream()
                .filter(t -> t.getStatus() == TicketStatus.RESOLVED
                          || t.getStatus() == TicketStatus.CLOSED)
                .collect(Collectors.toList());

        model.addAttribute("tickets", tickets);
        model.addAttribute("resolvedTickets", resolvedTickets);
        model.addAttribute("fullName", user.getFullName());

        return "ticket-my";
    }
}
