package com.example.receipt_processor.controller;

import java.util.UUID;

class ProcessResponse {

    public ProcessResponse(UUID id) {
        this.id = id;
    }

    UUID id;

    public UUID getId() {
        return id;
    }
}
