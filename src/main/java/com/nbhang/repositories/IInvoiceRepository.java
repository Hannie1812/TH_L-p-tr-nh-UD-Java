package com.nbhang.repositories;

import com.nbhang.entities.Invoice;
import com.nbhang.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IInvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByUser(User user);
}