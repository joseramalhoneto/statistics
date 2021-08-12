package com.hellofresh.statistics.utility;

import com.hellofresh.statistics.model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

public class TransactionUtility {

    public static final long TIME_LIMIT = 60000;
    public static final Double MIN_X = 0.0;
    public static final Double MAX_X = 1.0;
    public static final Integer MIN_Y = 1073741823;
    public static final Integer MAX_Y = 2147483647;

    public static long getLongInstance(){
        return Calendar
                .getInstance()
                .getTimeInMillis();
    }

    public static BigDecimal doubleToBigDecimal(Double value){
        return new BigDecimal(value)
                .setScale(10, RoundingMode.HALF_UP);
    }

    public static Transaction strToTransaction(String strTransaction){
        String[] split = strTransaction.split(",");

        LocalDateTime timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(split[0])), ZoneId.systemDefault());
        Double x = Double.valueOf(split[1]);
        Integer y = Integer.valueOf(split[2]);

        return new Transaction(timestamp, x, y);
    }

    public static long localDateTimeToLong(LocalDateTime localDateTime){
        return localDateTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    public static String setDoubleFormat(Double number) {
        DecimalFormat df = new DecimalFormat("###.##########");
        return df.format(number);
    }

}
