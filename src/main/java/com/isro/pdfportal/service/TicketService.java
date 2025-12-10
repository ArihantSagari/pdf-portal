package com.isro.pdfportal.service;

import com.isro.pdfportal.entity.Ticket;
import com.isro.pdfportal.entity.User;
import com.isro.pdfportal.model.TicketStatus;
import com.isro.pdfportal.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(User user, String title, String description, String priority) {
        Ticket t = new Ticket();
        t.setUser(user);
        t.setTitle(title);
        t.setDescription(description);
        t.setPriority(priority != null && !priority.isBlank() ? priority : "MEDIUM");
        t.setStatus(TicketStatus.OPEN);
        t.setCreatedAt(LocalDateTime.now());
        t.setUpdatedAt(LocalDateTime.now());
        return ticketRepository.save(t);
    }
    
    

    public List<Ticket> getUserTickets(User user) {
        return ticketRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Ticket> getLatestTickets(int limit) {
        // using repo that returns top 10, you can ignore limit or adjust query later
        return ticketRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public long countByStatus(TicketStatus status) {
        return ticketRepository.countByStatus(status);
    }

    public Ticket getById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public Ticket updateStatus(Long id, TicketStatus status) {
        Ticket t = getById(id);
        t.setStatus(status);
        t.setUpdatedAt(LocalDateTime.now());
        return ticketRepository.save(t);
    }
}
