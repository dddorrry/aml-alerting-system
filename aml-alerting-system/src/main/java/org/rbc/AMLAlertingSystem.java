package org.rbc;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class AMLAlertingSystem {
    public static final Logger logger = Logger.getLogger(AMLAlertingSystem.class.getName());


    /**
     * The main entry point of the AML Alerting System.
     *For simplicity purposes, we are printing to the standard output. In Production, we would write to an output file.
     * We are using a single thread executor to be able to queue up tasks for execution (decoupling file reading from
     * transaction processing) while still ensuring that the input order is maintained in the output
     * @param args The command-line arguments. The first argument should be the path to the CSV file containing
     *             transaction data.
     * @throws IOException If an error occurs while reading the CSV file.
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: java AMLAlertingSystem <csv_file_path>");
        }

        String inputFilePath = args[0];
        List<Transaction> transactions = AMLCSVReader.readTransactions(inputFilePath);

        AMLTransactionProcessor alertingSystem = new AMLTransactionProcessor();
        var  executorService = Executors.newSingleThreadExecutor();
        for (Transaction transaction : transactions) {
            executorService.submit(() -> {
                boolean alert = alertingSystem.processTransaction(transaction);
                String output = String.format("%s %d %d %s",
                        transaction.timestamp(),
                        transaction.amount(),
                        transaction.accountId(),
                        alert ? "Y" : "N");
                logger.info(output);
                System.out.println(output);  //For
            });
        }

        executorService.shutdown();
    }
}