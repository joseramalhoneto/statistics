package com.hellofresh.statistics.repository;

import com.hellofresh.statistics.model.Statistic;
import com.hellofresh.statistics.model.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.concurrent.PriorityBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @AfterEach
    void tearDown() {
        transactionRepository.getTransactions().clear();
    }

    @Test
    void save() {
        Transaction transaction = new Transaction(LocalDateTime.now(),0.0442672968,1282509067);
        transactionRepository.save(transaction);

        PriorityBlockingQueue<Transaction> transactions = transactionRepository.getTransactions();
        assertThat(transactions).contains(transaction);
    }

    @Test
    void getStatistics() {
        Transaction transaction1 = new Transaction(LocalDateTime.now(),0.0442672968,1282509067);
        transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction(LocalDateTime.now().minusSeconds(10),0.0442672968,1282509067);
        transactionRepository.save(transaction2);

        PriorityBlockingQueue<Transaction> transactions = transactionRepository.getTransactions();
        DoubleSummaryStatistics summaryStatisticsX = transactionRepository.getSummaryStatisticsX(transactions);
        DoubleSummaryStatistics summaryStatisticsY = transactionRepository.getSummaryStatisticsY(transactions);

        Statistic statistic = new Statistic(summaryStatisticsX.getCount(),
                summaryStatisticsX.getSum(),
                summaryStatisticsX.getAverage(),
                summaryStatisticsY.getSum(),
                summaryStatisticsY.getAverage());

        assertEquals(statistic.getTotal(), Optional.of(2L).get());
        assertEquals(statistic.getAvgX(), Optional.of(0.0442672968).get());
        assertEquals(statistic.getSumX(), Optional.of(0.0885345936).get());
        assertEquals(statistic.getAvgY(), Optional.of(1.282509067E9).get());
        assertEquals(statistic.getSumY(), Optional.of(2.565018134E9).get());
    }

    @Test
    void getTransactions() {
        Transaction transaction1 = new Transaction(LocalDateTime.now(),0.0442672968,1282509067);
        transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction(LocalDateTime.now().minusSeconds(10),0.0442672968,1282509067);
        transactionRepository.save(transaction2);

        PriorityBlockingQueue<Transaction> transactions = transactionRepository.getTransactions();
        assertThat(transactions).contains(transaction1);
        assertThat(transactions).contains(transaction2);
    }

}