package guru.springframework.spring6restmvc.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Builder
@Data
public class BeerOrderLineDTO {

    private UUID id;
    private Long version;

    private Timestamp createdDate;
    private Timestamp lastModifiedDate;

    @NotEmpty
    private BeerDTO beer;

    @NotEmpty
    private Integer orderQuantity;
    private Integer quantityAllocated;


}
