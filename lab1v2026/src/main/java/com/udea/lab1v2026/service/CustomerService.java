package com.udea.lab1v2026.service;

import com.udea.lab1v2026.DTO.CustomerDTO;
import com.udea.lab1v2026.DTO.TransactionDTO;
import com.udea.lab1v2026.entity.Customer;
import com.udea.lab1v2026.mapper.CustomerMapper;
import com.udea.lab1v2026.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDTO).toList();
    }
    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id).map(customerMapper::toDTO)
                .orElseThrow(()-> new RuntimeException("Cliente no encontrado"));
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        return customerMapper.toDTO(customerRepository.save(customer));
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        existing.setFirstName(customerDTO.getFirstName());
        existing.setLastName(customerDTO.getLastName());
        existing.setAccountNumber(customerDTO.getAccountNumber());
        existing.setBalance(customerDTO.getBalance());

        return customerMapper.toDTO(customerRepository.save(existing));
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado");
        }
        customerRepository.deleteById(id);
    }
}
