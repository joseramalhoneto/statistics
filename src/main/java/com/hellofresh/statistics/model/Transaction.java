package com.hellofresh.statistics.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.hellofresh.statistics.utility.TransactionUtility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Transaction {
    @JsonFormat
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(pattern = "YYYY-MM-DDThh:mm:ss.sssZ")
    private LocalDateTime timestamp;

    private Double x;
    private Integer y;

    @JsonIgnore
    public long getLongInstance(){
        return TransactionUtility.getLongInstance();
    }

    @JsonIgnore
    public long localDateTimeToLong(){
        return this
                .getTimestamp()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }
}
