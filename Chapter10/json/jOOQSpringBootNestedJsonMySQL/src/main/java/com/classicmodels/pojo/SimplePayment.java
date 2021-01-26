package com.classicmodels.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SimplePayment {

    public Long paymentNumber;
    public BigDecimal invoiceAmount;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.000000")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public LocalDateTime cachingDate;
    
    public List<SimpleBank> transactions;

    @Override
    public String toString() {
        return "SimplePayment{" + "paymentNumber=" + paymentNumber
                + ", invoiceAmount=" + invoiceAmount + ", cachingDate=" + cachingDate
                + ", transactions=" + transactions + '}';
    }
}
