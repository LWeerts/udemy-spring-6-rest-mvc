package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Customer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<UUID, Customer> customers;

    public CustomerServiceImpl() {
        this.customers = new HashMap<>();

        Customer customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Hank")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .version(5)
                .customerName("Angela")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer3 =Customer.builder()
                .id(UUID.randomUUID())
                .version(2)
                .customerName("Zane")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customers.put(customer1.getId(), customer1);
        customers.put(customer2.getId(), customer2);
        customers.put(customer3.getId(), customer3);
    }

    @Override
    public List<Customer> getCustomers() {
        return new ArrayList<>(customers.values());
    }

    @Override
    public Customer getCustomer(UUID id) {

        return customers.get(id);
    }
}
