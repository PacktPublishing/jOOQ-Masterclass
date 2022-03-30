package com.classicmodels.repository;

import com.classicmodels.entity.Customerdetail;
import com.classicmodels.pojo.SimpleCustomer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface CustomerdetailRepository extends JpaRepository<Customerdetail, Long> { 
    
    // This is a MySQL native query, but the jOOQ parser will emulate it
    // for our database dialect, which is a PostgreSQL database
    @Query(value = "SELECT c.customer_name as customerName, "
            + "d.address_line_first as addressLineFirst, d.address_line_second as addressLineSecond "
            + "FROM customer c JOIN customerdetail d "
            + "ON c.customer_number = d.customer_number "
            + "WHERE (NOT d.address_line_first <=> d.address_line_second)", 
            nativeQuery=true)
    List<SimpleCustomer> fetchCustomerNotSameAddress();
}
