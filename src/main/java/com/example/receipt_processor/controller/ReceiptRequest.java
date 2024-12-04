package com.example.receipt_processor.controller;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReceiptRequest {

  @NotNull
  @Pattern(regexp = "^[\\w\\s\\-&]+$")
  String retailer;
  @NotNull
  LocalDate purchaseDate;
  @NotNull
  LocalTime purchaseTime;
  @NotEmpty
  List<ItemRequest> items;
  @NotNull
  @Positive
  @Pattern(regexp = "^\\d+\\.\\d{2}$")
  String total;

  public String getRetailer() {
    return retailer;
  }

  public LocalDate getPurchaseDate() {
    return purchaseDate;
  }

  public LocalTime getPurchaseTime() {
    return purchaseTime;
  }

  public List<ItemRequest> getItems() {
    return items;
  }

  public String getTotal() {
    return total;
  }
}