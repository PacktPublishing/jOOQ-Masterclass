package com.classicmodels.pojo.java16records;

import java.util.List;

public record RecordProduct(String productName, String productVendor, Integer quantityInStock, List<RecordOrderdetail> orderdetail) {}