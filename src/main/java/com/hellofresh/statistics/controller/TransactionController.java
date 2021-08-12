package com.hellofresh.statistics.controller;

import com.hellofresh.statistics.model.Statistic;
import com.hellofresh.statistics.model.Transaction;
import com.hellofresh.statistics.service.TransactionService;
import com.hellofresh.statistics.utility.TransactionUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping(
            value = "/event",
            method = RequestMethod.POST,
            consumes = "text/plain")
    public ResponseEntity saveTransaction(@RequestBody String strTransaction){
        logger.info("PostMapping/transactions");

        try {
            if(transactionService.isInvalidJson(strTransaction)){
                logger.error("The JSON file is invalid - HttpStatus_BAD_REQUEST");
                return new ResponseEntity(HttpStatus.BAD_REQUEST);           //Error 400 – if the JSON is invalid
            }

            if (transactionService.isFutureTransaction(strTransaction)){
                logger.error("The transaction date is in the future - HttpStatus_UNPROCESSABLE_ENTITY");
                return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);  //Error 422 – if the transaction date is in the future
            }

            Transaction transaction = TransactionUtility.strToTransaction(strTransaction);
            transactionService.saveTransaction(transaction);
            return new ResponseEntity(HttpStatus.ACCEPTED);                   //Return 202 – in case of success

        } catch (RuntimeException e) {
            logger.info("RuntimeException - HttpStatus_BAD_REQUEST");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);                //Error 400 – Bad Request
        }catch (Exception e){
            logger.info("Exception - HttpStatus_INTERNAL_SERVER_ERROR");
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);      //Error 500 – Internal Server Error
        }

    }

    @GetMapping("/stats")
    public ResponseEntity<String> getStatistics(){
        try {
            logger.info("@GetMapping/statistics");

            Statistic statistics = transactionService.getStatistics();
            if(statistics != null)
                return new ResponseEntity<String>(statistics.toString(), HttpStatus.OK);
            else{
                logger.info("Exception - HttpStatus_NO_CONTENT");
                return new ResponseEntity(HttpStatus.NO_CONTENT);               //Return 204 – No Content
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);      //Error 500 – Internal Server Error
        }
    }

}