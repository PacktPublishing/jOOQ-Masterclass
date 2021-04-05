package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import jooq.generated.tables.pojos.Employee;
import jooq.generated.tables.pojos.Orderdetail;
import jooq.generated.tables.pojos.Product;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public List<Product> loadProductsAsc(long productId, int size) {

        return classicModelsRepository.fetchProductsPageAsc(productId, size);
    }

    public List<Product> loadProductsDesc(long productId, int size) {

        return classicModelsRepository.fetchProductsPageDesc(productId, size);
    }

    public List<Employee> loadEmployeesOfficeCodeAscSalaryDesc(String officeCode, int salary, int size) {

        return classicModelsRepository.fetchEmployeesPageOfficeCodeAscSalaryDesc(officeCode, salary, size);
    }

    public List<Employee> loadEmployeesOfficeCodeAscSalaryAsc(String officeCode, int salary, int size) {

        return classicModelsRepository.fetchEmployeesPageOfficeCodeAscSalaryAsc(officeCode, salary, size);
    }

    public List<Orderdetail> loadOrderdetailPageOrderIdAscProductIdQuantityOrderedDesc(
            long orderId, long productId, int quantityOrdered, int size) {

        return classicModelsRepository.fetchOrderdetailPageOrderIdAscProductIdQuantityOrderedDesc(
                orderId, productId, quantityOrdered, size);
    }

    public List<Product> loadProductsBuyPriceGtMsrp(long productId, int size) {

        return classicModelsRepository.fetchProductsBuyPriceGtMsrp(productId, size);
    }
}
