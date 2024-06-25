package org.rbc;

/**
 * This class contains configuration parameters for AML Alerting system.
 * These parameters are used to define thresholds, windows, and limits for the alerting system.
 */
public class AMLAlertingConfiguration {

    /**
     * The threshold amount for triggering an alert.
     * If the total amount of transactions exceeds this value within the alert window, an alert will be triggered.
     */
    public static final int THRESHOLD_AMOUNT = 50000;

    /**
     * The alert window duration in seconds.
     * This defines the time frame within which transactions are considered for alerting.
     */
    public static final int ALERT_WINDOW_SECONDS = 60;

    /**
     * The maximum number of accounts that can be monitored simultaneously.
     * This limit helps to prevent excessive resource usage.
     */
    public static final int MAX_ACCOUNTS = 5000;

    /**
     * The maximum number of transactions that can be processed per account within the alert window.
     * This limit helps to prevent excessive resource usage.
     */
    public static final int MAX_TRANSACTIONS_PER_ACCOUNT = 10000;
}
