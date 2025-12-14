package com.isro.pdfportal.service;

import com.isro.pdfportal.entity.Ticket;
import com.isro.pdfportal.entity.User;
import com.isro.pdfportal.model.TicketStatus;
import com.isro.pdfportal.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /* -------------------- CREATE -------------------- */

    public Ticket createTicket(User user, String title, String description, String priority) {
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setTitle(title.trim());
        ticket.setDescription(description.trim());
        ticket.setPriority(
                (priority != null && !priority.isBlank()) ? priority.toUpperCase() : "MEDIUM"
        );
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    /* -------------------- READ -------------------- */

    @Transactional(readOnly = true)
    public List<Ticket> getUserTickets(User user) {
        return ticketRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Ticket> getLatestTickets(int limit) {
        // repository currently returns top 10
        return ticketRepository.findTop10ByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public long countByStatus(TicketStatus status) {
        return ticketRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public Ticket getById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + id));
    }

    /* -------------------- UPDATE -------------------- */

    public Ticket updateStatus(Long id, TicketStatus status) {
        Ticket ticket = getById(id);
        ticket.setStatus(status);
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }
}
