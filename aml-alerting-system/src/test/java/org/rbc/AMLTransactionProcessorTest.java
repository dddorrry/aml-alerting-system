package org.rbc;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AMLTransactionProcessorTest {


    private AMLTransactionProcessor MakeAMLTransactionProcessor() {
        return new AMLTransactionProcessor();
    }

    @Test
    public void ProcessTransaction_AddsTransactionToAccount() {
        AMLTransactionProcessor processor = MakeAMLTransactionProcessor();
        Transaction transaction = new Transaction(LocalTime.now(), 500, 1);

        boolean result = processor.processTransaction(transaction);

        assertFalse(result);
    }

    @Test
    public void ProcessTransaction_ExceedsThreshold_ReturnsTrue() {
        AMLTransactionProcessor processor = MakeAMLTransactionProcessor();
        Transaction transaction1 = new Transaction(LocalTime.now().minusSeconds(30), 60000, 1);
        Transaction transaction2 = new Transaction(LocalTime.now(), 50000, 1);

        processor.processTransaction(transaction1);
        boolean result = processor.processTransaction(transaction2);

        assertTrue(result);
    }

    @Test
    public void ProcessTransaction_DoesNotExceedThreshold_ReturnsFalse() {
        AMLTransactionProcessor processor = MakeAMLTransactionProcessor();
        Transaction transaction = new Transaction(LocalTime.now(), 500, 1);

        boolean result = processor.processTransaction(transaction);

        assertFalse(result);
    }
}