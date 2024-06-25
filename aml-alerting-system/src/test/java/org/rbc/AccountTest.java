package org.rbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.rbc.AMLAlertingConfiguration.ALERT_WINDOW_SECONDS;
import static org.rbc.AMLAlertingConfiguration.THRESHOLD_AMOUNT;

@ExtendWith(MockitoExtension.class)
public class AccountTest {

    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account(1);
    }

    @Test
    public void testSingleTransaction_ExceedsThresholdAmount() {
        Transaction transaction1 = new Transaction(LocalTime.now(), THRESHOLD_AMOUNT + 1, 1);

        boolean alert = account.addTransaction(transaction1);

        assertTrue(alert);
    }

    @Test
    public void testSingleTransaction_BelowThresholdAmount() {
        Transaction transaction1 = new Transaction(LocalTime.now(), THRESHOLD_AMOUNT, 1);

        boolean alert = account.addTransaction(transaction1);

        assertFalse(alert);
    }

    @Test
    public void testTimeWindowSubtraction_withinWindow_BelowThresholdAmount() {
        Transaction transaction1 = new Transaction(LocalTime.now(), 10000, 1);
        Transaction transaction2 = new Transaction(LocalTime.now().plusSeconds(30), 40000, 1);

        account.addTransaction(transaction1);
        boolean alert = account.addTransaction(transaction2);

        assertFalse(alert);
    }

    @Test
    public void testTimeWindowSubtraction_withinWindow_JustAboveThresholdAmount() {
        Transaction transaction1 = new Transaction(LocalTime.now(), 10000, 1);
        Transaction transaction2 = new Transaction(LocalTime.now().plusSeconds(30), 40001, 1);

        account.addTransaction(transaction1);
        boolean alert = account.addTransaction(transaction2);

        assertTrue(alert);
    }

    @Test
    public void testTimeWindowSubtraction_outsideWindow_BelowThresholdAmount() {
        Transaction transaction1 = new Transaction(LocalTime.now(), 10000, 1);
        Transaction transaction2 = new Transaction(LocalTime.now().plusSeconds(ALERT_WINDOW_SECONDS + 1),
                40000, 1);

        account.addTransaction(transaction1);
        boolean alert = account.addTransaction(transaction2);

        assertFalse(alert);
    }

    @Test
    public void testTimeWindowSubtraction_outsideWindow_JustAboveThresholdAmount() {
        Transaction transaction1 = new Transaction(LocalTime.now(), 10000, 1);
        Transaction transaction2 = new Transaction(LocalTime.now().plusSeconds(ALERT_WINDOW_SECONDS + 1),
                40001, 1);

        account.addTransaction(transaction1);
        boolean alert = account.addTransaction(transaction2);

        assertFalse(alert);
    }

    @Test
    public void testTimeWindowSubtraction_multipleTransactionsWithinWindow_BelowThresholdAmount() {
        Transaction transaction1 = new Transaction(LocalTime.now(), 10000, 1);
        Transaction transaction2 = new Transaction(LocalTime.now().plusSeconds(10), 20000, 1);
        Transaction transaction3 = new Transaction(LocalTime.now().plusSeconds(20), 2000, 1);

        account.addTransaction(transaction1);
        account.addTransaction(transaction2);
        boolean alert = account.addTransaction(transaction3);

        assertFalse(alert);
    }

    @Test
    public void testTimeWindowSubtraction_multipleTransactionsWithinWindow_JustAboveThresholdAmount() {
        Transaction transaction1 = new Transaction(LocalTime.now(), 10000, 1);
        Transaction transaction2 = new Transaction(LocalTime.now().plusSeconds(10), 20000, 1);
        Transaction transaction3 = new Transaction(LocalTime.now().plusSeconds(20), 20001, 1);

        account.addTransaction(transaction1);
        account.addTransaction(transaction2);
        boolean alert = account.addTransaction(transaction3);

        assertTrue(alert);
    }

    @Test
    public void testTimeWindowSubtraction_multipleTransactionsOutsideWindow_BelowThresholdAmount() {
        Transaction transaction1 = new Transaction(LocalTime.now(), 10000, 1);
        Transaction transaction2 = new Transaction(LocalTime.now().plusSeconds(ALERT_WINDOW_SECONDS + 1),
                20000, 1);
        Transaction transaction3 = new Transaction(LocalTime.now().plusSeconds(ALERT_WINDOW_SECONDS * 2 + 2),
                20000, 1);

        account.addTransaction(transaction1);
        account.addTransaction(transaction2);
        boolean alert = account.addTransaction(transaction3);

        assertFalse(alert);
    }

    @Test
    public void testTimeWindowSubtraction_multipleTransactionsOutsideWindow_JustAboveThresholdAmount() {
        Transaction transaction1 = new Transaction(LocalTime.now(), 10000, 1);
        Transaction transaction2 = new Transaction(LocalTime.now().plusSeconds(ALERT_WINDOW_SECONDS + 1),
                20000, 1);
        Transaction transaction3 = new Transaction(LocalTime.now().plusSeconds(ALERT_WINDOW_SECONDS * 2 + 2),
                20001, 1);

        account.addTransaction(transaction1);
        account.addTransaction(transaction2);
        boolean alert = account.addTransaction(transaction3);

        assertFalse(alert);
    }

    @Test
    public void testJustAtEndOfAlertWindow_exceedsThresholdAmount_ShouldRaiseAlert() {
        Transaction transaction1 = new Transaction(LocalTime.now(), THRESHOLD_AMOUNT - 100, 1);
        Transaction transaction2 = new Transaction(LocalTime.now().plusSeconds(ALERT_WINDOW_SECONDS),
                101, 1);

        account.addTransaction(transaction1);
        boolean alert = account.addTransaction(transaction2);

        assertTrue(alert);
    }

    @Test
    public void testJustAfterAlertWindow_exceedsThresholdAmount_ShouldNotRaiseAlert() {
        Transaction transaction1 = new Transaction(LocalTime.now(), THRESHOLD_AMOUNT - 100, 1);
        Transaction transaction2 = new Transaction(LocalTime.now().plusSeconds(ALERT_WINDOW_SECONDS + 1),
                101, 1);

        account.addTransaction(transaction1);
        boolean alert = account.addTransaction(transaction2);

        assertFalse(alert);
    }
}