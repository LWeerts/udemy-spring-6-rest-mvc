package guru.springframework.spring6restmvc.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;

import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class BeerDeletedEvent {

    private UUID beerId;

    private Authentication authentication;
}
