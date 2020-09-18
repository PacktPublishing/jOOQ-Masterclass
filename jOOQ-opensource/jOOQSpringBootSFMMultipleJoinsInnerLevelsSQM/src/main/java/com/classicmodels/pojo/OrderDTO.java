package com.classicmodels.pojo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class OrderDTO implements Serializable {

    private static final long serialVersionUID = 1;
        
    private Long orderId;
    private LocalDate orderDate;
    private LocalDate shippedDate;
    private Set<OrderDetailDTO> details = new HashSet<>(); // or LinkedHashSet

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }        

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(LocalDate shippedDate) {
        this.shippedDate = shippedDate;
    }

    public Set<OrderDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(Set<OrderDetailDTO> details) {
        this.details = details;
    }     

    @Override
    public int hashCode() {
        
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.orderId);
        hash = 53 * hash + Objects.hashCode(this.orderDate);
        hash = 53 * hash + Objects.hashCode(this.shippedDate);
        
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final OrderDTO other = (OrderDTO) obj;
        if (!Objects.equals(this.orderId, other.orderId)) {
            return false;
        }
        
        if (!Objects.equals(this.orderDate, other.orderDate)) {
            return false;
        }
        
        if (!Objects.equals(this.shippedDate, other.shippedDate)) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "OrderDTO{" + "orderId=" + orderId + ", orderDate=" + orderDate 
                + ", shippedDate=" + shippedDate + ", details=" + details + '}';
    }
        
}
