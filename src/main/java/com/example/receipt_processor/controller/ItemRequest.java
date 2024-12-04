package com.example.receipt_processor.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class ItemRequest {
  @NotNull
  @Pattern(regexp="^[\\w\\s\\-]+$")
  String shortDescription;
  @NotNull
  @Positive
  @Pattern(regexp="^\\d+\\.\\d{2}$")
  String price;

  public String getShortDescription() {
    return shortDescription;
  }

  public String getPrice() {
    return price;
  }
}