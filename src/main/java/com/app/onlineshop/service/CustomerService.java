package com.app.onlineshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import com.app.onlineshop.repository.CustomerRepository;
import com.app.onlineshop.model.Customer;
import com.app.onlineshop.DTO.request.CustomerRequest;
import com.app.onlineshop.DTO.response.CustomerResponse;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest requestDTO) {
        Customer customer = new Customer();
        customer.setCustomerName(requestDTO.getCustomerName());
        customer.setCustomerAddress(requestDTO.getCustomerAddress());
        customer.setCustomerCode(requestDTO.getCustomerCode());
        customer.setCustomerPhone(requestDTO.getCustomerPhone());
        customer.setPic(requestDTO.getPic());
        customer.setActive(true);
        Customer savedCustomer = customerRepository.save(customer);
        return new CustomerResponse(
                savedCustomer.getCustomerId(),
                savedCustomer.getCustomerName(),
                savedCustomer.getCustomerAddress(),
                savedCustomer.getCustomerCode(),
                savedCustomer.getCustomerPhone(),
                savedCustomer.isActive(),
                savedCustomer.getPic());
    }

    @Transactional
    public CustomerResponse updateCustomer(Long customerId, CustomerRequest requestDTO) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer existingCustomer = optionalCustomer.get();
            existingCustomer.setCustomerName(requestDTO.getCustomerName());
            existingCustomer.setCustomerAddress(requestDTO.getCustomerAddress());
            existingCustomer.setCustomerPhone(requestDTO.getCustomerPhone());
            existingCustomer.setActive(requestDTO.isActive());
            existingCustomer.setPic(requestDTO.getPic());
            Customer updatedCustomer = customerRepository.save(existingCustomer);
            return new CustomerResponse(
                    updatedCustomer.getCustomerId(),
                    updatedCustomer.getCustomerName(),
                    updatedCustomer.getCustomerAddress(),
                    updatedCustomer.getCustomerCode(),
                    updatedCustomer.getCustomerPhone(),
                    updatedCustomer.isActive(),
                    updatedCustomer.getPic());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer with ID " + customerId + " not found");
        }
    }

    @Transactional
    public void deleteCustomer(Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            customerRepository.deleteById(customerId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer with ID " + customerId + " not found");
        }
    }
}
