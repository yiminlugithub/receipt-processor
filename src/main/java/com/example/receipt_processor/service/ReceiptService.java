package com.example.receipt_processor.service;

import com.example.receipt_processor.controller.ReceiptRequest;
import com.example.receipt_processor.repository.Item;
import com.example.receipt_processor.repository.Receipt;
import com.example.receipt_processor.repository.ReceiptRepository;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiptService {

  private static final LocalTime TWO_PM = LocalTime.parse("14:00");
  private static final LocalTime FOUR_PM = LocalTime.parse("16:00");
  private final Map<UUID, Integer> precalculatedTotals = new HashMap<>();

  @Autowired
  private ReceiptRepository receiptRepository;

  public UUID saveReceipt(ReceiptRequest receiptRequest) {
    Receipt receipt = receiptRepository.save(new Receipt(receiptRequest));
    return receipt.getId();
  }

  public Integer calculatePoints(UUID receiptId) {
    if (precalculatedTotalExists(receiptId))
      return precalculatedTotals.get(receiptId);

    Receipt receipt = receiptRepository.getReferenceById(receiptId);

    int total = calculateRetailerNamePoints(receipt)
            + calculateReceiptTotalPoints(receipt)
            + calculateReceiptItemsPoints(receipt)
            + calculateReceiptPurchaseDatePoints(receipt)
            + calculateReceiptPurchaseTimePoints(receipt);

    precalculatedTotals.put(receiptId, total);

    return total;
  }

  int calculateRetailerNamePoints(Receipt receipt) {
    return (int) receipt.getRetailer().chars()
            .filter(c-> Character.isDigit((char) c) || Character.isLetter((char) c))
            .count();
  }

  int calculateReceiptTotalPoints(Receipt receipt) {
    int total = 0;

    double receiptTotal = receipt.getTotal();

    if (receiptTotal % 1 == 0)
      total += 50;

    if (receiptTotal % 0.25 == 0)
      total += 25;

    return total;
  }

  int calculateReceiptItemsPoints(Receipt receipt) {
    int total = 0;
    List<Item> items = receipt.getItems();

    total += (items.size() / 2) * 5;

    for (Item item : items) {
      if (item.getShortDescription().trim().length() % 3 == 0)
        total += (int) Math.ceil(item.getPrice() * 0.2);
    }

    return total;
  }

  int calculateReceiptPurchaseDatePoints(Receipt receipt) {
    if (receipt.getPurchaseDate().getDayOfMonth() % 2 == 1)
      return 6;
    return 0;
  }

  int calculateReceiptPurchaseTimePoints(Receipt receipt) {
    if (receipt.getPurchaseTime().isAfter(TWO_PM) && receipt.getPurchaseTime().isBefore(FOUR_PM))
      return 10;
    return 0;
  }

  private boolean precalculatedTotalExists(UUID receiptId) {
    return precalculatedTotals.containsKey(receiptId);
  }

}
