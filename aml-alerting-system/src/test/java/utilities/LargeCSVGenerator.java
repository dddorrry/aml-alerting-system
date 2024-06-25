package utilities;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * This class generates a large CSV file containing simulated banking transactions.
 * The CSV file will have three columns: Time, Amount, and AccountId.
 * The number of transactions per account is defined by {@link #TRANSACTIONS_PER_ACCOUNT},
 * and the total number of accounts is defined by {@link #NUM_ACCOUNTS}.
 * The total number of transactions generated is the product of the number of accounts and transactions per account.
 */
public class LargeCSVGenerator {
    private static final int NUM_ACCOUNTS = 2; // More than 5000 accounts
    private static final int TRANSACTIONS_PER_ACCOUNT = 10001; // Reduced for brevity in output
    private static final int TOTAL_TRANSACTIONS = NUM_ACCOUNTS * TRANSACTIONS_PER_ACCOUNT;
    private static final Random RANDOM = new Random();

    public static Stream<String> generateTransactions() {
        LocalTime startTime = LocalTime.of(9, 0); // Start at 9:00 AM
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return Stream.concat(
                Stream.of("Time,Amount,AccountId"), // Header
                IntStream.range(0, TOTAL_TRANSACTIONS)
                        .mapToObj(i -> {
                            int accountId = i % NUM_ACCOUNTS + 1; // Ensure all accounts are used
                            int amount = RANDOM.nextInt(60000) + 1000; // Random amount between 1000 and 60000
                            LocalTime transactionTime = startTime.plusSeconds(i);

                            return String.format("%s,%d,%d",
                                    transactionTime.format(formatter),
                                    amount,
                                    accountId);
                        })
        );
    }

    public static void main(String[] args) {
        // Print all generated transactions
        generateTransactions().forEach(System.out::println);

        // Print total number of lines (including header) to stderr
        System.err.println("Total lines generated: " + (TOTAL_TRANSACTIONS + 1));
    }
}
