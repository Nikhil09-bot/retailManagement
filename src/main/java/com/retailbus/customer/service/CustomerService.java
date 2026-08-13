package com.retailbus.customer.service;

import com.retailbus.customer.dto.CustomerRequest;
import com.retailbus.customer.dto.CustomerResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * CustomerService Interface.
 */
public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse updateCustomer(Long id, CustomerRequest request);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse getCustomerById(Long id);

    CustomerResponse getCustomerByPhone(String phone);

    List<CustomerResponse> searchCustomers(String keyword);

    List<CustomerResponse> getCustomersByCity(String city);

    /**
     * Add loyalty points after a purchase.
     */
    CustomerResponse addLoyaltyPoints(Long customerId, BigDecimal points);

    /**
     * Update the customer's total purchase amount.
     */
    CustomerResponse recordPurchase(Long customerId, BigDecimal purchaseAmount);

    void deleteCustomer(Long id);

    Long getCustomerCount();
}

