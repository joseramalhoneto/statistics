package com.hellofresh.statistics.service;

import com.hellofresh.statistics.model.Statistic;
import com.hellofresh.statistics.model.Transaction;
import com.hellofresh.statistics.repository.TransactionRepository;
import com.hellofresh.statistics.utility.TransactionUtility;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Synchronized
    public void saveTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }

    public boolean isInvalidJson(String strTransaction) {
        if(strTransaction == null ||
                strTransaction.equals("") ||
                strTransaction.split(",").length != 3)
            return true;

        try {
            Transaction transaction = TransactionUtility.strToTransaction(strTransaction);

            if(transaction.getTimestamp() == null ||
                    transaction.getTimestamp().equals(""))
                return true;

            if(transaction.getX() == null ||
                    transaction.getX() < TransactionUtility.MIN_X ||
                    transaction.getX() > TransactionUtility.MAX_X)
                return true;

            if(transaction.getY() == null ||
                    transaction.getY() < TransactionUtility.MIN_Y ||
                    transaction.getY() > TransactionUtility.MAX_Y)
                return true;

        } catch (RuntimeException e) {
            logger.info("RuntimeException - HttpStatus_BAD_REQUEST");
            return true;
        }

        return false;
    }

    public boolean isFutureTransaction(String strTransaction) {
        String[] split = strTransaction.split(",");
        LocalDateTime timestamp = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(Long.parseLong(split[0])),
                ZoneId.systemDefault()
        );

        if(TransactionUtility.getLongInstance() -
                TransactionUtility.localDateTimeToLong(timestamp) < 0)
            return true;

        return false;
    }

    @Synchronized
    public Statistic getStatistics(){
        return transactionRepository.getStatistics();
    }
}
