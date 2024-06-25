package org.rbc;

import java.util.concurrent.ConcurrentHashMap;

import static org.rbc.AMLAlertingConfiguration.MAX_ACCOUNTS;

/**
 * A class responsible for processing AML transactions.
 * It maintains a map of accounts and processes transactions by adding them to the corresponding accounts.
 * If the account does not exist, it will be created.
 */
class AMLTransactionProcessor {

    /**
     * A concurrent hash map to store accounts.
     * The key is the account ID and the value is the corresponding Account object
     * which contains its list of transactions
     */
    private final ConcurrentHashMap<Integer, Account> accountCache;

    public AMLTransactionProcessor() {
        this.accountCache = new ConcurrentHashMap<>(MAX_ACCOUNTS);
    }

    /**
 * Processes a given transaction by adding it to the corresponding account.
 * If the account does not exist, it will be created.
 *
 * @param transaction The transaction to be processed.
 * @return {@code true} if the transaction raised an AML violation alert,
 *         {@code false} if the transaction did not raise a violation alert.
 */
public boolean processTransaction(Transaction transaction) {
    return accountCache.computeIfAbsent(transaction.accountId(), Account::new)
            .addTransaction(transaction);
}
}