package com.isro.pdfportal.repository;

import com.isro.pdfportal.entity.User;
import com.isro.pdfportal.model.TicketStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByStaffNo(String staffNo);

    Optional<User> findByEmailOrStaffNo(String email, String staffNo);

    boolean existsByEmail(String email);

    boolean existsByStaffNo(String staffNo);
    
   // long countByStatus(TicketStatus status);


	long countByActive(boolean active);
	 List<User> findAllByOrderByFullNameAsc();
}
