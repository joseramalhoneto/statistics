package com.hellofresh.statistics.model;

import com.hellofresh.statistics.utility.TransactionUtility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Statistic {
    private Long total;
    private Double sumX;
    private Double avgX;
    private Double sumY;
    private Double avgY;

    @Override
    public String toString() {
        return this.getTotal() + "," +
                TransactionUtility.setDoubleFormat(this.getSumX()) + "," +
                TransactionUtility.setDoubleFormat(this.getAvgX()) + "," +
                this.getSumY() + "," +
                this.getAvgY();
    }

}
