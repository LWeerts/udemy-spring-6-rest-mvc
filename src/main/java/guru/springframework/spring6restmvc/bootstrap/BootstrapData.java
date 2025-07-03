package guru.springframework.spring6restmvc.bootstrap;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) {

        generateBeers();
        generateCustomers();
    }

    private void generateBeers() {
        if (beerRepository.count() == 0) {
            Beer beer1 = Beer.builder()
                    .beerName("Heineken")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("RANDOM UPC 1")
                    .quantityOnHand(5000)
                    .price(BigDecimal.valueOf(3.50))
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();
            beerRepository.save(beer1);

            Beer beer2 = Beer.builder()
                    .beerName("Sunshine")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("12356")
                    .quantityOnHand(3000)
                    .price(BigDecimal.valueOf(4.50))
                    .build();
            beerRepository.save(beer2);

            Beer beer3 = Beer.builder()
                    .beerName("Grolsch")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("56983")
                    .quantityOnHand(150)
                    .price(BigDecimal.valueOf(4))
                    .build();
            beerRepository.save(beer3);
        }
    }

    private void generateCustomers() {
        if  (customerRepository.count() == 0) {
            Customer customer1 = Customer.builder()
                    .name("Mark")
                    .build();
            customerRepository.save(customer1);

            Customer customer2 = Customer.builder()
                    .name("Jane")
                    .build();
            customerRepository.save(customer2);

            Customer customer3 = Customer.builder()
                    .name("Timothy")
                    .build();
            customerRepository.save(customer3);
        }
    }
}
