package com.retailbus.customer.service.impl;

import com.retailbus.customer.dto.CustomerRequest;
import com.retailbus.customer.dto.CustomerResponse;
import com.retailbus.customer.entity.Customer;
import com.retailbus.customer.exception.CustomerNotFoundException;
import com.retailbus.customer.mapper.CustomerMapper;
import com.retailbus.customer.repository.CustomerRepository;
import com.retailbus.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CustomerService Implementation.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        log.info("Creating new customer: {} {}", request.getFirstName(), request.getLastName());
        Customer customer = customerMapper.toEntity(request);
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created with ID: {}", savedCustomer.getId());
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        log.info("Updating customer with ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));

        Customer updatedCustomer = customerMapper.updateEntity(customer, request);
        Customer savedCustomer = customerRepository.save(updatedCustomer);
        log.info("Customer updated with ID: {}", id);
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAll().stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        log.info("Fetching customer with ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByPhone(String phone) {
        log.info("Fetching customer with phone: {}", phone);
        Customer customer = customerRepository.findByPhone(phone)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with phone: " + phone));
        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> searchCustomers(String keyword) {
        log.info("Searching customers with keyword: {}", keyword);
        List<Customer> results = new ArrayList<>();
        results.addAll(customerRepository.findByFirstNameContainingIgnoreCase(keyword));
        results.addAll(customerRepository.findByLastNameContainingIgnoreCase(keyword));
        return results.stream()
                .distinct()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getCustomersByCity(String city) {
        log.info("Fetching customers from city: {}", city);
        return customerRepository.findByCity(city).stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse addLoyaltyPoints(Long customerId, BigDecimal points) {
        log.info("Adding {} loyalty points to customer ID: {}", points, customerId);
        if (points == null || points.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Points must be a positive value");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));

        BigDecimal currentPoints = customer.getLoyaltyPoints() != null ? customer.getLoyaltyPoints() : BigDecimal.ZERO;
        customer.setLoyaltyPoints(currentPoints.add(points));
        Customer savedCustomer = customerRepository.save(customer);

        log.info("Loyalty points updated. Customer ID: {}. Total points: {}", customerId, savedCustomer.getLoyaltyPoints());
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse recordPurchase(Long customerId, BigDecimal purchaseAmount) {
        log.info("Recording purchase of ${} for customer ID: {}", purchaseAmount, customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));

        BigDecimal currentTotal = customer.getTotalPurchases() != null ? customer.getTotalPurchases() : BigDecimal.ZERO;
        customer.setTotalPurchases(currentTotal.add(purchaseAmount));

        // Award 1 loyalty point per dollar spent (business rule)
        BigDecimal pointsToAdd = purchaseAmount.setScale(0, java.math.RoundingMode.DOWN);
        BigDecimal currentPoints = customer.getLoyaltyPoints() != null ? customer.getLoyaltyPoints() : BigDecimal.ZERO;
        customer.setLoyaltyPoints(currentPoints.add(pointsToAdd));

        Customer savedCustomer = customerRepository.save(customer);
        log.info("Purchase recorded for customer ID: {}. Total purchases: ${}", customerId, savedCustomer.getTotalPurchases());
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        customerRepository.delete(customer);
        log.info("Customer deleted with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCustomerCount() {
        return customerRepository.count();
    }
}

