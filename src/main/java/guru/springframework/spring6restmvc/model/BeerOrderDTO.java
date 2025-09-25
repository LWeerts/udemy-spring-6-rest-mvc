package guru.springframework.spring6restmvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class BeerOrderDTO {

    private UUID id;
    private Long version;
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;

    private BigDecimal paymentAmount;

    @NotBlank
    @Size(max = 255)
    private String customerRef;
    private CustomerDTO customer;

    private Set<BeerOrderLineDTO> beerOrderLines;
    private BeerOrderShipmentDTO beerOrderShipment;

}
