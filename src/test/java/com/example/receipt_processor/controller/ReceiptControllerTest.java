package com.example.receipt_processor.controller;

import com.example.receipt_processor.service.ReceiptService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptControllerTest {

    @Mock
    private ReceiptService receiptService;

    @InjectMocks
    private ReceiptController receiptController;

    @Test
    void process() {
        UUID receiptId = UUID.randomUUID();
        when(receiptService.saveReceipt(any(ReceiptRequest.class))).thenReturn(receiptId);

        assertThat(receiptController.process(mock(ReceiptRequest.class)).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(receiptController.process(mock(ReceiptRequest.class)).getBody().getId()).isEqualTo(receiptId);

    }

    @Test
    void points() {
        UUID receiptId = UUID.randomUUID();
        when(receiptService.calculatePoints(receiptId)).thenReturn(10);

        assertThat(receiptController.points(receiptId).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(receiptController.points(receiptId).getBody().getPoints()).isEqualTo(10);
    }

    @Test
    void handleEntityNotFound() {
        assertThat(receiptController.handleEntityNotFound().getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(receiptController.handleEntityNotFound().getBody()).isEqualTo("Receipt not found");
    }

    @Test
    void handleValidationExceptions() {
        FieldError e = mock(FieldError.class);
        when(e.getField()).thenReturn("total");
        when(e.getDefaultMessage()).thenReturn("can not be null");

        BindingResult result = mock(BindingResult.class);
        when(result.getFieldErrors()).thenReturn(List.of(e));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(result);

        assertThat(receiptController.handleValidationExceptions(ex).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(receiptController.handleValidationExceptions(ex).getBody()).isEqualTo(Map.of("total", "can not be null"));
    }
}
