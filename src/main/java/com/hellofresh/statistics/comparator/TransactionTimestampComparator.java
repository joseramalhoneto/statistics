package com.hellofresh.statistics.comparator;

import com.hellofresh.statistics.model.Transaction;

import java.util.Comparator;

public class TransactionTimestampComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction o1, Transaction o2) {
        return o1.getTimestamp().compareTo(o2.getTimestamp());
    }
}