package com.hellofresh.statistics.service;

import com.hellofresh.statistics.model.Transaction;
import com.hellofresh.statistics.repository.TransactionRepository;
import com.hellofresh.statistics.utility.TransactionUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class TransactionServiceTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService service;

    @Test
    void saveTransaction() {
        Transaction transaction1 = new Transaction(LocalDateTime.now(),0.0442672968,1282509067);
        transactionRepository.save(transaction1);

        boolean contains = transactionRepository.getTransactions().contains(transaction1);
        assertEquals(true, contains);
    }

    @Test
    void isNullJson() {
        String strTransaction = null;
        boolean invalidJson = service.isInvalidJson(strTransaction);
        assertEquals(true, invalidJson);
    }

    @Test
    void isBlankJson() {
        String strTransaction = "";
        boolean invalidJson = service.isInvalidJson(strTransaction);
        assertEquals(true, invalidJson);
    }

    @Test
    void isIncompleteJson() {
        String strTransaction = "1607341341814";
        boolean invalidJson = service.isInvalidJson(strTransaction);
        assertEquals(true, invalidJson);

        strTransaction = LocalDateTime.now() + ",0.0442672968";
        invalidJson = service.isInvalidJson(strTransaction);
        assertEquals(true, invalidJson);
    }

    @Test
    void isFutureTransaction() {
        LocalDateTime timestamp = LocalDateTime.now().plusHours(10);
        Double x = 0.0442672968;
        Integer y = 1282509067;

        String strTransaction = TransactionUtility.localDateTimeToLong(timestamp) + "," + x + "," + y;
        boolean futureTransaction = service.isFutureTransaction(strTransaction);
        assertEquals(true, futureTransaction);
    }

    @Test //𝑥: A real number always in 0..1
    void isInvalidX() {
        LocalDateTime timestamp = LocalDateTime.now();
        Double x = -1.0;    //negative invalid
        Integer y = 1282509067;

        String strTransaction = TransactionUtility.localDateTimeToLong(timestamp) + "," + x + "," + y;
        boolean invalidJson = service.isInvalidJson(strTransaction);
        assertEquals(true, invalidJson);

        x = 9.0;            //positive invalid
        strTransaction = TransactionUtility.localDateTimeToLong(timestamp) + "," + x + "," + y;
        invalidJson = service.isInvalidJson(strTransaction);
        assertEquals(true, invalidJson);
    }

    @Test //𝑦: An integer in 1073741823..2147483647
    void isValidY() {
        LocalDateTime timestamp = LocalDateTime.now();
        Double x = 0.0442672968;
        Integer y = 1073741823;

        String strTransaction = TransactionUtility.localDateTimeToLong(timestamp) + "," + x + "," + y;
        boolean invalidJson = service.isInvalidJson(strTransaction);
        assertEquals(false, invalidJson);
    }

}