package guru.springframework.spring6restmvc.listeners;

import guru.springframework.spring6restmvc.entities.BeerAudit;
import guru.springframework.spring6restmvc.events.BeerDeletedEvent;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.repositories.BeerAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by jt, Spring Framework Guru.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerDeletedListener {

    private final BeerAuditRepository beerAuditRepository;

    @Async
    @EventListener
    public void listen(BeerDeletedEvent event) {

        val beerAudit = BeerAudit.builder()
                .id(event.getBeerId())
                .auditEventType("BEER_DELETED")
                .build();

        if (event.getAuthentication() != null && event.getAuthentication().getName() != null) {
            beerAudit.setPrincipalName(event.getAuthentication().getName());
        }

        val savedBeerAudit = beerAuditRepository.save(beerAudit);
        log.debug("Beer Audit Saved: " + savedBeerAudit.getId());

    }
}
