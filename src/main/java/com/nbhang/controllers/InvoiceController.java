package com.nbhang.controllers;

import com.nbhang.entities.Invoice;
import com.nbhang.entities.User;
import com.nbhang.services.InvoiceService;
import com.nbhang.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final UserService userService;

    // User viewing their own orders
    @GetMapping("/orders")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String userOrders(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Invoice> orders = invoiceService.getInvoicesByUser(user);
        model.addAttribute("orders", orders);
        return "invoice/user/list";
    }

    // Admin viewing all orders
    @GetMapping("/admin/orders")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminOrders(Model model) {
        List<Invoice> orders = invoiceService.getAllInvoices();
        model.addAttribute("orders", orders);
        return "invoice/admin/list";
    }

    // Admin updating order status
    @PostMapping("/admin/orders/update-status/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateStatus(@PathVariable Long id, @RequestParam("status") String status,
            RedirectAttributes redirectAttributes) {
        log.info("Attempting to update status for invoice ID: {} to {}", id, status);
        try {
            invoiceService.updateStatus(id, status);
            log.info("Successfully updated status for invoice ID: {}", id);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái thành công!");
        } catch (Exception e) {
            log.error("Failed to update status for invoice ID: {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Cập nhật trạng thái thất bại: " + e.getMessage());
        }
        return "redirect:/admin/orders";
    }
}
