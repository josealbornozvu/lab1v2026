package com.udea.lab1v2026;

import com.udea.lab1v2026.DTO.CustomerDTO;
import com.udea.lab1v2026.controller.CustomerController;
import com.udea.lab1v2026.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @Test
    void getAllCustomers_devuelve200ConLista() {
        when(customerService.getAllCustomers()).thenReturn(List.of(new CustomerDTO()));

        ResponseEntity<List<CustomerDTO>> response = customerController.getAllCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getCustomerById_devuelve200() {
        CustomerDTO dto = new CustomerDTO();
        when(customerService.getCustomerById(1L)).thenReturn(dto);

        ResponseEntity<CustomerDTO> response = customerController.getCustomerById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(dto, response.getBody());
    }

    @Test
    void createCustomer_conBalance_devuelve200() {
        CustomerDTO dto = new CustomerDTO();
        dto.setBalance(100.0);
        when(customerService.createCustomer(dto)).thenReturn(dto);

        ResponseEntity<CustomerDTO> response = customerController.createCustomer(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(dto, response.getBody());
    }

    @Test
    void createCustomer_sinBalance_lanzaExcepcion() {
        CustomerDTO dto = new CustomerDTO();
        dto.setBalance(null);

        assertThrows(IllegalArgumentException.class,
                () -> customerController.createCustomer(dto));
        verifyNoInteractions(customerService);
    }

    @Test
    void updateCustomer_devuelve200() {
        CustomerDTO dto = new CustomerDTO();
        when(customerService.updateCustomer(1L, dto)).thenReturn(dto);

        ResponseEntity<CustomerDTO> response = customerController.updateCustomer(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(dto, response.getBody());
    }

    @Test
    void deleteCustomer_devuelve204() {
        ResponseEntity<Void> response = customerController.deleteCustomer(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(customerService).deleteCustomer(1L);
    }
}