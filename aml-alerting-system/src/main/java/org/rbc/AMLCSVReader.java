package org.rbc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.rbc.AMLAlertingConfiguration.MAX_ACCOUNTS;
import static org.rbc.AMLAlertingConfiguration.MAX_TRANSACTIONS_PER_ACCOUNT;

/**
 * This class is responsible for reading and processing AML transactions from a CSV file.
 */
class AMLCSVReader {

    /**
     * Logger for logging errors and information.
     */
    public static final Logger logger = Logger.getLogger(AMLCSVReader.class.getName());

    /**
     * Reads transactions from a CSV file and returns a list of Transaction objects.
     *
     * @param filePath The path to the CSV file containing transaction data.
     * @return A list of Transaction objects representing the transactions read from the CSV file.
     * @throws IOException If an error occurs while reading the CSV file.
     */
    public static List<Transaction> readTransactions(String filePath) throws IOException {
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Consume the first line as it is a header line

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 3) {
                    LocalTime timestamp = LocalTime.parse(values[0]);
                    int amount = Integer.parseInt(values[1]);
                    int accountId = Integer.parseInt(values[2]);
                    transactions.add(new Transaction(timestamp, amount, accountId));
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading CSV file: " + e);
            throw (e);
        }

        // Validate the number of accounts and transactions per account
        Map<Integer, Long> transactionMap = transactions.stream().collect(groupingBy(Transaction::accountId, counting()));

        if (transactionMap.entrySet().size() > MAX_ACCOUNTS)
            throw new IllegalArgumentException("The maximum number of accounts allowed " +
                    "in the input file is: " + MAX_ACCOUNTS);

        if(transactionMap.values().stream().max(Comparator.naturalOrder()).orElse(Long.valueOf(0)) > MAX_TRANSACTIONS_PER_ACCOUNT)
            throw new IllegalArgumentException("The maximum number of transactions allowed" +
                    " per account is: " +MAX_TRANSACTIONS_PER_ACCOUNT);

        return transactions;
    }
}