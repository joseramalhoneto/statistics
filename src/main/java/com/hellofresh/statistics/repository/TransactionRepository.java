package com.hellofresh.statistics.repository;

import com.hellofresh.statistics.comparator.TransactionTimestampComparator;
import com.hellofresh.statistics.model.Statistic;
import com.hellofresh.statistics.model.Transaction;
import com.hellofresh.statistics.utility.TransactionUtility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.DoubleSummaryStatistics;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Component
public class TransactionRepository {

    private static final int QUEUE_INITIAL_CAPACITY = 1000;

    private PriorityBlockingQueue<Transaction> transactions;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TransactionRepository() {
        this.transactions = new PriorityBlockingQueue<>(QUEUE_INITIAL_CAPACITY, new TransactionTimestampComparator());
    }

    @Synchronized
    public void save(Transaction transaction) {
        logger.info("TransactionRepository - Save transaction data");
        transactions.add(transaction);
        logger.info(transactions.toString());
    }

    @Synchronized
    public Statistic getStatistics() {
        logger.info("TransactionRepository - Get statistics");
        DoubleSummaryStatistics summaryStatisticsX = getSummaryStatisticsX(transactions);
        DoubleSummaryStatistics summaryStatisticsY = getSummaryStatisticsY(transactions);

        Statistic statistic = new Statistic(summaryStatisticsX.getCount(),
                summaryStatisticsX.getSum(),
                summaryStatisticsX.getAverage(),
                summaryStatisticsY.getSum(),
                summaryStatisticsY.getAverage());

        return statistic;
    }

    public DoubleSummaryStatistics getSummaryStatisticsX(PriorityBlockingQueue<Transaction> transactions) {
        return transactions
                .stream()
                .filter(transaction -> TransactionUtility.getLongInstance() - transaction.localDateTimeToLong() <= TransactionUtility.TIME_LIMIT)
                .collect(Collectors.summarizingDouble(transaction -> transaction.getX()));
    }

    public DoubleSummaryStatistics getSummaryStatisticsY(PriorityBlockingQueue<Transaction> transactions) {
        return transactions
                .stream()
                .filter(transaction -> TransactionUtility.getLongInstance() - transaction.localDateTimeToLong() <= TransactionUtility.TIME_LIMIT)
                .collect(Collectors.summarizingDouble(transaction -> transaction.getY()));
    }
}
