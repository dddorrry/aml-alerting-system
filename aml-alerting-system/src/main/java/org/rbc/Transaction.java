package org.rbc;

import java.time.LocalTime;


/**
 * Represents a financial transaction.
 *
 * @param timestamp The time when the transaction occurred.
 * @param amount    The amount of money involved in the transaction.
 * @param accountId The unique identifier of the account involved in the transaction.
 */
record Transaction(LocalTime timestamp, int amount, int accountId) {
}