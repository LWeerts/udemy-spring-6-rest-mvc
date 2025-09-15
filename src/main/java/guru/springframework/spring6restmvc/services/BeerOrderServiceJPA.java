package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.controller.NotFoundException;
import guru.springframework.spring6restmvc.entities.BeerOrder;
import guru.springframework.spring6restmvc.entities.BeerOrderLine;
import guru.springframework.spring6restmvc.mappers.BeerOrderMapper;
import guru.springframework.spring6restmvc.model.BeerOrderCreateDTO;
import guru.springframework.spring6restmvc.model.BeerOrderDTO;
import guru.springframework.spring6restmvc.model.BeerOrderLineCreateDTO;
import guru.springframework.spring6restmvc.repositories.BeerOrderRepository;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeerOrderServiceJPA implements BeerOrderService {
    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final BeerRepository beerRepository;
    private final BeerOrderMapper beerOrderMapper;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
    private static final int MAX_PAGE_SIZE = 1000;

    @Override
    public Page<BeerOrderDTO> listBeerOrders(Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        return beerOrderRepository.findAll(pageRequest).map(beerOrderMapper::beerOrderToBeerOrderDTO);
    }

    @Override
    public Optional<BeerOrderDTO> getBeerOrderById(UUID id) {
        return beerOrderRepository.findById(id).map(beerOrderMapper::beerOrderToBeerOrderDTO);
    }

    @Override
    public BeerOrderDTO saveNewBeerOrder(BeerOrderCreateDTO beerOrderCreateDTO) {
        Set<BeerOrderLine> beerOrderLines = new HashSet<>();
        for (BeerOrderLineCreateDTO orderLineCreateDTO : beerOrderCreateDTO.getBeerOrderLines()) {
            var beerOrderLine = BeerOrderLine.builder()
                    .beer(beerRepository.findById(orderLineCreateDTO.getBeerId()).orElseThrow(
                            () -> new NotFoundException("Beer not found with id " + orderLineCreateDTO.getBeerId())))
                    .orderQuantity(orderLineCreateDTO.getOrderQuantity())
                    .build();
            beerOrderLines.add(beerOrderLine);
        }

        BeerOrder beerOrder = BeerOrder.builder()
                .customer(customerRepository.findById(beerOrderCreateDTO.getCustomerId()).orElseThrow(
                        () -> new NotFoundException("Customer not found")))
                .customerRef(beerOrderCreateDTO.getCustomerRef())
                .beerOrderLines(beerOrderLines)
                .build();

        return beerOrderMapper.beerOrderToBeerOrderDTO(beerOrderRepository.save(beerOrder));
    }


    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            queryPageSize = pageSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : pageSize;
        }

        Sort sort = Sort.by(Sort.Order.asc("createdDate"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }
}
