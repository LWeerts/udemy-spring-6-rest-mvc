package guru.springframework.spring6restmvc.mappers;

import guru.springframework.spring6restmvc.entities.BeerOrder;
import guru.springframework.spring6restmvc.model.BeerOrderDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerOrderMapper {
    BeerOrder BeerOrderDTOToBeerOrder(BeerOrderDTO beerOrderDTO);
    BeerOrderDTO BeerOrderToBeerOrderDTO(BeerOrder beerOrder);
}
