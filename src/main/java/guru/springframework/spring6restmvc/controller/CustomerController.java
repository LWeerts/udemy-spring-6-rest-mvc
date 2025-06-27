package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping(value = "{customerId}")
    public Customer getCustomerById(@PathVariable("customerId") UUID customerId) {
        return customerService.getCustomer(customerId);
    }
}
