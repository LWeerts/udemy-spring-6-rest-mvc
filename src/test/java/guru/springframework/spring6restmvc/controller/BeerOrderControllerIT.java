package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.entities.BeerOrder;
import guru.springframework.spring6restmvc.entities.BeerOrderLine;
import guru.springframework.spring6restmvc.model.*;
import guru.springframework.spring6restmvc.repositories.BeerOrderRepository;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static guru.springframework.spring6restmvc.controller.BeerControllerTest.jwtRequestPostProcessor;
import static org.hamcrest.core.Is.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class BeerOrderControllerIT {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    @Order(1)  // otherwise testCreateBeerOrder would increase size to 7
    void testListBeerOrders() throws Exception {
        mockMvc.perform(get(BeerOrderController.BEER_ORDER_PATH)
                        .with(jwtRequestPostProcessor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(6)));
    }

    @Test
    @Order(2)
    void testGetBeerOrderById() throws Exception {
        BeerOrder beerOrder = beerOrderRepository.findAll().getFirst();

        mockMvc.perform(get(BeerOrderController.BEER_ORDER_PATH_ID, beerOrder.getId())
                        .with(jwtRequestPostProcessor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(beerOrder.getId().toString())));
    }

    @Test
    @Order(3)
    void testCreateBeerOrder() throws Exception {
        var beer = beerRepository.findAll().getFirst();
        var customer = customerRepository.findAll().getFirst();

        var orderLine = BeerOrderLineCreateDTO.builder()
                .beerId(beer.getId())
                .orderQuantity(6).build();

        var beerOrder = BeerOrderCreateDTO.builder()
                .customerId(customer.getId())
                .customerRef(customer.getName())
                .beerOrderLines(Set.of(orderLine))
                .build();

        mockMvc.perform(post(BeerOrderController.BEER_ORDER_PATH)
                        .with(jwtRequestPostProcessor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerOrder)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Transactional
    @Test
    @Order(4)
    void testUpdateBeerOrder() throws Exception {
        var beerOrder = beerOrderRepository.findAll().getFirst();

        final String updatedCustomerRef = beerOrder.getCustomerRef() + " updated";

        Set<BeerOrderLineUpdateDTO> beerOrderLinesUpdated = new HashSet<>();
        for (BeerOrderLine beerOrderLine : beerOrder.getBeerOrderLines()) {
            beerOrderLinesUpdated.add(BeerOrderLineUpdateDTO.builder()
                    .id(beerOrderLine.getId())
                    .beerId(beerOrderLine.getBeer().getId())
                    .orderQuantity(beerOrderLine.getOrderQuantity())
                    .quantityAllocated(beerOrderLine.getQuantityAllocated())
                    .build());
        }

        BeerOrderShipmentUpdateDTO beerOrderShipmentUpdateDTO = null;
        if (beerOrder.getBeerOrderShipment() != null) {
            beerOrderShipmentUpdateDTO = BeerOrderShipmentUpdateDTO.builder()
                    .trackingNumber(beerOrder.getBeerOrderShipment().getTrackingNumber()).build();
        }

        var beerOrderUpdateDTO = BeerOrderUpdateDTO.builder()
                .customerId(beerOrder.getCustomer().getId())
                .customerRef(updatedCustomerRef)
                .beerOrderLines(beerOrderLinesUpdated)
                .beerOrderShipment(beerOrderShipmentUpdateDTO)
                .build();

        mockMvc.perform(put(BeerOrderController.BEER_ORDER_PATH_ID, beerOrder.getId())
                        .with(jwtRequestPostProcessor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerOrderUpdateDTO)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(BeerOrderController.BEER_ORDER_PATH_ID, beerOrder.getId())
                        .with(jwtRequestPostProcessor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerRef", is(updatedCustomerRef)));
    }

    @Transactional
    @Rollback
    @Test
    @Order(5)
    void testDeleteBeerOrder() throws Exception {
        var beerOrder = beerOrderRepository.findAll().getFirst();
        mockMvc.perform(delete(BeerOrderController.BEER_ORDER_PATH_ID, beerOrder.getId())
                .with(jwtRequestPostProcessor))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(BeerOrderController.BEER_ORDER_PATH_ID, beerOrder.getId())
                .with(jwtRequestPostProcessor))
                .andExpect(status().isNotFound());
    }
}
