package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerControllerIT {
    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testCustomerIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Test
    void testGetById() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO dto = customerController.getCustomerById(customer.getId());

        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(customer.getName(), dto.getName())
        );
    }

    @Test
    void testListBeers() {
        List<CustomerDTO> dtoList = customerController.listAllCustomers();

        assertEquals(3, dtoList.size());
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        customerRepository.deleteAll();
        List<CustomerDTO> dtoList = customerController.listAllCustomers();

        assertEquals(0, dtoList.size());
    }
}