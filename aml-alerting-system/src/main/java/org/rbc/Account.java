package org.rbc;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.rbc.AMLAlertingConfiguration.ALERT_WINDOW_SECONDS;
import static org.rbc.AMLAlertingConfiguration.THRESHOLD_AMOUNT;


class Account {
    private final int id;
    private final LinkedList<Transaction> transactions;
    private int runningTotal;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public Account(int id) {
        this.id = id;
        this.transactions = new LinkedList<>();
        this.runningTotal = 0;
    }

    public int getId() {
        return id;
    }

    /**
 * Adds a new transaction to the account and checks if the running total exceeds the alert threshold.
 *
 * @param transaction The transaction to be added.
 * @return True if the running total exceeds the alert threshold (meaning an alert must be raised), false otherwise.
 * @throws NullPointerException If the transaction is null.
 */
public boolean addTransaction(Transaction transaction) {
    lock.writeLock().lock();
    try {
        // Calculate the timestamp 60 seconds ago
        LocalTime cutOffTime = transaction.timestamp().minusSeconds(ALERT_WINDOW_SECONDS);

        // Remove transactions that occurred before the 60-second window
        while (!transactions.isEmpty() && transactions.getFirst().timestamp().isBefore(cutOffTime)) {
            runningTotal -= transactions.removeFirst().amount();
        }

        // Add the new transaction to the account
        transactions.addLast(transaction);
        runningTotal += transaction.amount();

        // Check if the running total exceeds the alert threshold
        return runningTotal > THRESHOLD_AMOUNT;
    } finally {
        lock.writeLock().unlock();
    }
}
}