package com.example.receipt_processor.controller;

import com.example.receipt_processor.service.ReceiptService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("receipts")
class ReceiptController {

  @Autowired
  private ReceiptService receiptService;

  @PostMapping("/process")
  public ResponseEntity<ProcessResponse> process(@Valid @RequestBody ReceiptRequest receiptRequest) {
    return ResponseEntity.ok(new ProcessResponse(receiptService.saveReceipt(receiptRequest)));
  }

  @GetMapping("/{id}/points")
  public ResponseEntity<PointsResponse> points(@PathVariable UUID id) {
    return ResponseEntity.ok(new PointsResponse(receiptService.calculatePoints(id)));
  }

  @ExceptionHandler(EntityNotFoundException.class)
  ResponseEntity<String> handleEntityNotFound() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Receipt not found");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    return ResponseEntity.badRequest().body(errors);
  }
}