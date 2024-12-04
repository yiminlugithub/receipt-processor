package com.example.receipt_processor.repository;

import com.example.receipt_processor.controller.ReceiptRequest;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
public class Receipt {

  public Receipt() {}
  public Receipt(ReceiptRequest request) {
    this.retailer = request.getRetailer();
    this.purchaseDate = request.getPurchaseDate();
    this.purchaseTime = request.getPurchaseTime();
    this.items = request.getItems() != null ? request.getItems().stream().map(Item::new).collect(Collectors.toList()) : List.of();
    this.total =  request.getTotal() != null ? Double.parseDouble(request.getTotal()) : 0.0;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private UUID id;
  @Column(nullable = false)
  String retailer;
  @Column(nullable = false)
  LocalDate purchaseDate;
  @Column(nullable = false)
  LocalTime purchaseTime;
  @OneToMany(cascade=CascadeType.ALL)
  @Column(nullable = false)
  List<Item> items;
  @Column(nullable = false)
  Double total;

  public UUID getId() {
    return id;
  }

  public String getRetailer() {
    return retailer;
  }

  public LocalDate getPurchaseDate() {
    return purchaseDate;
  }

  public LocalTime getPurchaseTime() {
    return purchaseTime;
  }

  public Double getTotal() {
    return total;
  }

  public List<Item> getItems() {
    return items;
  }

}