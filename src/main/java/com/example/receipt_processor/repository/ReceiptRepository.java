package com.example.receipt_processor.repository;

import java.util.UUID;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {}
