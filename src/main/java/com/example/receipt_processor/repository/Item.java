package com.example.receipt_processor.repository;

import com.example.receipt_processor.controller.ItemRequest;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Item {
    public Item() {}

    public Item(ItemRequest item) {
        this.shortDescription = item.getShortDescription();
        this.price = Double.parseDouble(item.getPrice());
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    String shortDescription;
    @Column(nullable = false)
    Double price;

    public String getShortDescription() {
        return shortDescription;
    }

    public Double getPrice() {
        return price;
    }

}

