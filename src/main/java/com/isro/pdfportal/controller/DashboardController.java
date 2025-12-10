//package com.isro.pdfportal.controller;
//
//import com.isro.pdfportal.config.CustomUserDetails;
//import com.isro.pdfportal.entity.User;
//import com.isro.pdfportal.model.FormType;
//import com.isro.pdfportal.service.PdfRecordService;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.Arrays;
//
//@Controller
//public class DashboardController {
//
//    private final PdfRecordService pdfRecordService;
//
//    public DashboardController(PdfRecordService pdfRecordService) {
//        this.pdfRecordService = pdfRecordService;
//    }
//
//    @GetMapping("/dashboard")
//    public String userDashboard(Authentication authentication, Model model) {
//
//        if (authentication == null ||
//                !(authentication.getPrincipal() instanceof CustomUserDetails details)) {
//            return "redirect:/login";
//        }
//
//        User user = details.getUser();
//
//        // User details
//        model.addAttribute("fullName", user.getFullName());
//        model.addAttribute("staffNo", user.getStaffNo());
//        model.addAttribute("department", user.getDepartment());
//        model.addAttribute("designation", user.getDesignation());
//        model.addAttribute("role", user.getRole());
//
//        // Form types dropdown
//        model.addAttribute("formTypes", Arrays.asList(FormType.values()));
//
//        // Recent PDFs (last 3 days)
//        model.addAttribute(
//                "recentPdfs",
//                pdfRecordService.getRecentRecords(user, 3)
//        );
//
//        return "dashboard";
//    }
//
//    @GetMapping("/admin/dashboard")
//    public String adminDashboard(Authentication authentication, Model model) {
//
//        if (authentication == null ||
//                !(authentication.getPrincipal() instanceof CustomUserDetails details)) {
//            return "redirect:/login";
//        }
//
//        User user = details.getUser();
//
//        model.addAttribute("fullName", user.getFullName());
//        model.addAttribute("staffNo", user.getStaffNo());
//        model.addAttribute("department", user.getDepartment());
//
//        return "admin-dashboard";
//    }
//}

package com.isro.pdfportal.controller;

import com.isro.pdfportal.config.CustomUserDetails;
import com.isro.pdfportal.entity.Ticket;
import com.isro.pdfportal.entity.User;
import com.isro.pdfportal.model.FormType;
import com.isro.pdfportal.model.TicketStatus;
import com.isro.pdfportal.repository.UserRepository;
import com.isro.pdfportal.service.PdfRecordService;
import com.isro.pdfportal.service.TicketService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class DashboardController {

    private final PdfRecordService pdfRecordService;
    private final TicketService ticketService;
    private final UserRepository userRepository;
    

    public DashboardController(PdfRecordService pdfRecordService,
                               TicketService ticketService,
                               UserRepository userRepository) {
        this.pdfRecordService = pdfRecordService;
        this.ticketService = ticketService;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String userDashboard(Authentication authentication, Model model) {

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof CustomUserDetails details)) {
            return "redirect:/login";
        }

        User user = details.getUser();

        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("staffNo", user.getStaffNo());
        model.addAttribute("department", user.getDepartment());
        model.addAttribute("designation", user.getDesignation());
        model.addAttribute("role", user.getRole());

        model.addAttribute("formTypes", Arrays.asList(FormType.values()));
        model.addAttribute("recentPdfs", pdfRecordService.getRecentRecords(user, 3));
        model.addAttribute("myTickets", ticketService.getUserTickets(user)); 

        return "dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Authentication authentication, Model model) {

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof CustomUserDetails details)) {
            return "redirect:/login";
        }

        User admin = details.getUser();

        model.addAttribute("fullName", admin.getFullName());
        model.addAttribute("staffNo", admin.getStaffNo());
        model.addAttribute("department", admin.getDepartment());

        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByActive(true);
        long openTickets = ticketService.countByStatus(TicketStatus.OPEN);
        long resolvedTickets =
                ticketService.countByStatus(TicketStatus.RESOLVED)
              + ticketService.countByStatus(TicketStatus.CLOSED);

        List<Ticket> latestTickets = ticketService.getLatestTickets(10);

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("openTickets", openTickets);
        model.addAttribute("resolvedTickets", resolvedTickets);
        model.addAttribute("latestTickets", latestTickets);

        return "admin-dashboard";
    }
}
