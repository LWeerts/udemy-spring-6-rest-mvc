package guru.springframework.spring6restmvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Builder
@Data
public class BeerOrderShipmentDTO {

    private UUID id;
    private Long version;

    @NotBlank
    @Size(max = 50)
    private String trackingNumber;

    private Timestamp createdDate;
    private Timestamp lastModifiedDate;
}
