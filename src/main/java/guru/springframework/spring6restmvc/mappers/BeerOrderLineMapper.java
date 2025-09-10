package guru.springframework.spring6restmvc.mappers;

import guru.springframework.spring6restmvc.entities.BeerOrderLine;
import guru.springframework.spring6restmvc.model.BeerOrderLineDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BeerOrderLineMapper {
    @Mapping(target = "beerOrder", ignore = true)
    BeerOrderLine DTOToBeerOrderLine(BeerOrderLineDTO beerOrderLineDTO);

    BeerOrderLineDTO beerOrderLineToDTO(BeerOrderLine beerOrderLine);
}
