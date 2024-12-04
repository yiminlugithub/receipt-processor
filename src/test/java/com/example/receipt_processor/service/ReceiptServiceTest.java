package com.example.receipt_processor.service;

import com.example.receipt_processor.controller.ReceiptRequest;
import com.example.receipt_processor.repository.Item;
import com.example.receipt_processor.repository.Receipt;
import com.example.receipt_processor.repository.ReceiptRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {

    @Mock
    private ReceiptRepository receiptRepository;

    @InjectMocks
    private ReceiptService receiptService;

    @Test
    void testSaveReceipt() {
        Receipt receipt = mock(Receipt.class);

        ReceiptRequest receiptRequest = mock(ReceiptRequest.class);

        when(receiptRepository.save(any(Receipt.class))).thenReturn(receipt);
        assertThat(receipt.getId()).isEqualTo(receiptService.saveReceipt(receiptRequest));

        verify(receiptRepository).save(any(Receipt.class));
    }

    @Test
    void testCalculatePointsPrecalculatedOnlyCalculatesOnce() {
        UUID receiptId = UUID.randomUUID();

        Item item = mock(Item.class);
        when(item.getShortDescription()).thenReturn("Mountain Dew 12PK");

        Receipt receipt = mock(Receipt.class);
        when(receipt.getRetailer()).thenReturn("Target");
        when(receipt.getTotal()).thenReturn(35.35);
        when(receipt.getItems()).thenReturn(List.of(item));
        when(receipt.getPurchaseDate()).thenReturn(LocalDate.of(2022, 1, 1));
        when(receipt.getPurchaseTime()).thenReturn(LocalTime.of(13, 1));

        when(receiptRepository.getReferenceById(receiptId)).thenReturn(receipt);

        assertThat(12).isEqualTo(receiptService.calculatePoints(receiptId));

        assertThat(12).isEqualTo(receiptService.calculatePoints(receiptId));

        verify(receiptRepository, times(1)).getReferenceById(receiptId);
    }

    @Test
    void testCalculateRetailerNamePointsOnlyCountsAlphaNumericCharacters() {

        Receipt receipt = mock(Receipt.class);
        when(receipt.getRetailer()).thenReturn("M&M Corner Market");

        assertThat(14).isEqualTo(receiptService.calculateRetailerNamePoints(receipt));
    }

    @Test
    void testCalculateReceiptTotalPointsAdds25IfTotalIsMultipleOf25CentsButNotWholeValue() {
        Receipt receipt = mock(Receipt.class);
        when(receipt.getTotal()).thenReturn(40.25);

        assertThat(25).isEqualTo(receiptService.calculateReceiptTotalPoints(receipt));
    }

    @Test
    void testCalculateReceiptTotalPointsAdds75IfTotalIWholeValue() {
        Receipt receipt = mock(Receipt.class);
        when(receipt.getTotal()).thenReturn(40.0);

        assertThat(75).isEqualTo(receiptService.calculateReceiptTotalPoints(receipt));
    }

    @Test
    void testCalculateReceiptItemsPointsAdds5ForEvery2Items() {
        Item item1 = mock(Item.class);
        when(item1.getShortDescription()).thenReturn("Short Description");

        Item item2 = mock(Item.class);
        when(item2.getShortDescription()).thenReturn("Short Description");

        Item item3 = mock(Item.class);
        when(item3.getShortDescription()).thenReturn("Short Description");

        Item item4 = mock(Item.class);
        when(item4.getShortDescription()).thenReturn("Short Description");

        Item item5 = mock(Item.class);
        when(item5.getShortDescription()).thenReturn("Short Description");

        Receipt receipt = mock(Receipt.class);
        when(receipt.getItems()).thenReturn(List.of(item1, item2, item3, item4, item5));

        assertThat(10).isEqualTo(receiptService.calculateReceiptItemsPoints(receipt));
    }

    @Test
    void testCalculateReceiptItemsPointsCorrectlyCalculatesPointsWhenTrimmedShortDescriptionIsMultipleOf3() {
        Item item = mock(Item.class);
        when(item.getShortDescription()).thenReturn("   Klarbrunn 12-PK 12 FL OZ  ");
        when(item.getPrice()).thenReturn(12.0);

        Receipt receipt = mock(Receipt.class);
        when(receipt.getItems()).thenReturn(List.of(item));

        assertThat(3).isEqualTo(receiptService.calculateReceiptItemsPoints(receipt));
    }

    @Test
    void testCalculateReceiptItemsPointsAdds0WhenTrimmedShortDescriptionIsNotMultipleOf3() {
        Item item = mock(Item.class);
        when(item.getShortDescription()).thenReturn("Short Description");

        Receipt receipt = mock(Receipt.class);
        when(receipt.getItems()).thenReturn(List.of(item));

        assertThat(0).isEqualTo(receiptService.calculateReceiptItemsPoints(receipt));
    }
    @Test
    void testCalculateReceiptPurchaseDatePointsAdds6IfOddDay() {
        Receipt receipt = mock(Receipt.class);
        when(receipt.getPurchaseDate()).thenReturn(LocalDate.of(12,1,1));

        assertThat(6).isEqualTo(receiptService.calculateReceiptPurchaseDatePoints(receipt));
    }

    @Test
    void testCalculateReceiptPurchaseDatePointsAdds0IfEvenDay() {
        Receipt receipt = mock(Receipt.class);
        when(receipt.getPurchaseDate()).thenReturn(LocalDate.of(12,1,2));

        assertThat(0).isEqualTo(receiptService.calculateReceiptPurchaseDatePoints(receipt));
    }

    @Test
    void testCalculateReceiptTimeDatePointsAdds10IfBetween2And4PM() {
        Receipt receipt = mock(Receipt.class);
        when(receipt.getPurchaseTime()).thenReturn(LocalTime.of(14,1));

        assertThat(10).isEqualTo(receiptService.calculateReceiptPurchaseTimePoints(receipt));
    }

    @Test
    void testCalculateReceiptTimeDatePointsAdds0INotfBetween2And4PM() {
        Receipt receipt = mock(Receipt.class);
        when(receipt.getPurchaseTime()).thenReturn(LocalTime.of(16,0));

        assertThat(0).isEqualTo(receiptService.calculateReceiptPurchaseTimePoints(receipt));
    }
}
