package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.BeerOrderCreateDTO;
import guru.springframework.spring6restmvc.model.BeerOrderDTO;
import guru.springframework.spring6restmvc.model.BeerOrderUpdateDTO;
import guru.springframework.spring6restmvc.services.BeerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class BeerOrderController {

    private final BeerOrderService beerOrderService;

    public static final String BEER_ORDER_PATH = "/api/v1/beerorder";
    public static final String BEER_ORDER_PATH_ID = BEER_ORDER_PATH + "/{beerOrderId}";

    @GetMapping(value = BEER_ORDER_PATH)
    public Page<BeerOrderDTO> listBeerOrder(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ) {
        return beerOrderService.listBeerOrders(pageNumber, pageSize);
    }

    @GetMapping(value = BEER_ORDER_PATH_ID)
    public BeerOrderDTO getBeerOrder(@PathVariable("beerOrderId") UUID beerOrderId) {
        return beerOrderService.getBeerOrderById(beerOrderId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(value = BEER_ORDER_PATH)
    public ResponseEntity<Void> createBeerOrder(@Validated @RequestBody BeerOrderCreateDTO beerOrder) {
        BeerOrderDTO savedBeerOrder = beerOrderService.saveNewBeerOrder(beerOrder);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, BEER_ORDER_PATH + "/" + savedBeerOrder.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = BEER_ORDER_PATH_ID)
    public ResponseEntity<Void> updateBeerOrder(
            @PathVariable("beerOrderId") UUID beerOrderId,
            @Validated @RequestBody BeerOrderUpdateDTO beerOrderUpdateDTO) {

        if (beerOrderService.updateBeerOrder(beerOrderId, beerOrderUpdateDTO).isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}