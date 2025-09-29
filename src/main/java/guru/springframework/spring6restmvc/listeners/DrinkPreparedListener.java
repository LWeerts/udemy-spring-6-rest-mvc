package guru.springframework.spring6restmvc.listeners;

import guru.springframework.spring6restmvc.config.KafkaConfig;
import guru.springframework.spring6restmvc.repositories.BeerOrderLineRepository;
import guru.springframework.spring6restmvcapi.events.DrinkPreparedEvent;
import guru.springframework.spring6restmvcapi.model.BeerOrderLineStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DrinkPreparedListener {

    private final BeerOrderLineRepository beerOrderLineRepository;

    @KafkaListener(groupId = "DrinkPreparedListener", topics = KafkaConfig.DRINK_PREPARED_TOPIC)
    public void listenDrinkPrepared(DrinkPreparedEvent event) {
        beerOrderLineRepository.findById(event.getBeerOrderLine().getId()).ifPresentOrElse(beerOrderLine -> {
            beerOrderLine.setOrderLineStatus(BeerOrderLineStatus.COMPLETE);
            beerOrderLineRepository.save(beerOrderLine);
        }, () -> {
            log.error("Could not find beer order line with id {}", event.getBeerOrderLine().getId());
        });

    }
}
