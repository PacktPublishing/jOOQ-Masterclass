package com.classicmodels.repository;

import com.classicmodels.entity.Productline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface ProductlineRepository extends JpaRepository<Productline, Long> {      
}
