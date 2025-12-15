package com.isro.pdfportal.controller;

import com.isro.pdfportal.config.CustomUserDetails;
import com.isro.pdfportal.entity.Ticket;
import com.isro.pdfportal.model.TicketStatus;
import com.isro.pdfportal.service.TicketService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/tickets")
public class AdminTicketController {

    private final TicketService ticketService;

    public AdminTicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Full ticket list
    @GetMapping
    public String listTickets(Authentication authentication, Model model) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails details)) {
            return "redirect:/login";
        }

        List<Ticket> tickets = ticketService.getAllTickets();
        model.addAttribute("tickets", tickets);
        model.addAttribute("statuses", TicketStatus.values());
        return "admin-tickets";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam("status") TicketStatus status) {
        ticketService.updateStatus(id, status);
        return "redirect:/admin/tickets";
    }

}