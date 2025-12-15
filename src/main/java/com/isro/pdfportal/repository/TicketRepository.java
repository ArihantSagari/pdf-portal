package com.isro.pdfportal.repository;

import com.isro.pdfportal.entity.Ticket;
import com.isro.pdfportal.entity.User;
import com.isro.pdfportal.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUserOrderByCreatedAtDesc(User user);

    List<Ticket> findAllByOrderByCreatedAtDesc();

    List<Ticket> findTop10ByOrderByCreatedAtDesc();

    long countByStatus(TicketStatus status);
}
