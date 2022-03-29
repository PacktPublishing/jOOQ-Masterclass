package com.classicmodels.dao;

import com.classicmodels.entity.Customer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

@Repository
public class Dao {

    @PersistenceContext
    private EntityManager em;

    public Query fetchCustomers() {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
        Root<Customer> customer = criteria.from(Customer.class);

        ParameterExpression<String> p1 = builder.parameter(String.class, "p1");
        criteria.where(
                builder.like(customer.get("customerName"), p1)
        );

        criteria.orderBy(
                builder.asc(customer.get("creditLimit"))
        );

        return em.createQuery(criteria);
    }
    
    public Query fetchCustomerdetails() {
        
        return em.createQuery("SELECT c.city, c.country "
                + "FROM Customerdetail c WHERE c.customerNumber = ?1", Tuple.class);
    }
}