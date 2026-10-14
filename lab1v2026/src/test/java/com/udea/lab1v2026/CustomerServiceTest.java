package com.udea.lab1v2026;

import com.udea.lab1v2026.DTO.CustomerDTO;
import com.udea.lab1v2026.entity.Customer;
import com.udea.lab1v2026.mapper.CustomerMapper;
import com.udea.lab1v2026.repository.CustomerRepository;
import com.udea.lab1v2026.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void getAllCustomers_devuelveListaMapeada() {
        Customer customer = new Customer();
        CustomerDTO dto = new CustomerDTO();
        when(customerRepository.findAll()).thenReturn(List.of(customer));
        when(customerMapper.toDTO(customer)).thenReturn(dto);

        List<CustomerDTO> result = customerService.getAllCustomers();

        assertEquals(1, result.size());
        assertSame(dto, result.get(0));
    }

    @Test
    void getCustomerById_existente_devuelveDTO() {
        Customer customer = new Customer();
        CustomerDTO dto = new CustomerDTO();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toDTO(customer)).thenReturn(dto);

        assertSame(dto, customerService.getCustomerById(1L));
    }

    @Test
    void getCustomerById_inexistente_lanzaExcepcion() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> customerService.getCustomerById(1L));

        assertEquals("Cliente no encontrado", ex.getMessage());
    }

    @Test
    void createCustomer_guardaYDevuelveDTO() {
        CustomerDTO entrada = new CustomerDTO();
        CustomerDTO salida = new CustomerDTO();
        Customer entity = new Customer();
        Customer guardado = new Customer();

        when(customerMapper.toEntity(entrada)).thenReturn(entity);
        when(customerRepository.save(entity)).thenReturn(guardado);
        when(customerMapper.toDTO(guardado)).thenReturn(salida);

        assertSame(salida, customerService.createCustomer(entrada));
        verify(customerRepository).save(entity);
    }

    @Test
    void updateCustomer_existente_actualizaCampos() {
        Customer existing = new Customer();
        CustomerDTO entrada = new CustomerDTO();
        entrada.setFirstName("Ana");
        entrada.setLastName("Perez");
        entrada.setAccountNumber("123");
        entrada.setBalance(500.0);
        CustomerDTO salida = new CustomerDTO();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(existing)).thenReturn(existing);
        when(customerMapper.toDTO(existing)).thenReturn(salida);

        CustomerDTO result = customerService.updateCustomer(1L, entrada);

        assertSame(salida, result);
        assertEquals("Ana", existing.getFirstName());
        assertEquals("Perez", existing.getLastName());
        assertEquals("123", existing.getAccountNumber());
        assertEquals(500.0, existing.getBalance(), 0.001);
    }

    @Test
    void updateCustomer_inexistente_lanzaExcepcion() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> customerService.updateCustomer(1L, new CustomerDTO()));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void deleteCustomer_existente_elimina() {
        when(customerRepository.existsById(1L)).thenReturn(true);

        customerService.deleteCustomer(1L);

        verify(customerRepository).deleteById(1L);
    }

    @Test
    void deleteCustomer_inexistente_lanzaExcepcion() {
        when(customerRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> customerService.deleteCustomer(1L));
        verify(customerRepository, never()).deleteById(any());
    }
}