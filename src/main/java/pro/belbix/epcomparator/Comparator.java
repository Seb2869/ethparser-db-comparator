package pro.belbix.epcomparator;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pro.belbix.epcomparator.dto.HardWorkDTO;
import pro.belbix.epcomparator.dto.HarvestDTO;
import pro.belbix.epcomparator.dto.ImportantEventsDTO;
import pro.belbix.epcomparator.dto.RewardDTO;
import pro.belbix.epcomparator.dto.TransferDTO;
import pro.belbix.epcomparator.dto.UniswapDTO;
import pro.belbix.epcomparator.entity.IncomeEntity;
import pro.belbix.epcomparator.repositories1.HardWorkRepository1;
import pro.belbix.epcomparator.repositories1.HarvestRepository1;
import pro.belbix.epcomparator.repositories1.ImportantEventsRepository1;
import pro.belbix.epcomparator.repositories1.IncomeRepository1;
import pro.belbix.epcomparator.repositories1.RewardsRepository1;
import pro.belbix.epcomparator.repositories1.TransferRepository1;
import pro.belbix.epcomparator.repositories1.UniswapRepository1;
import pro.belbix.epcomparator.repositories2.HardWorkRepository2;
import pro.belbix.epcomparator.repositories2.HarvestRepository2;
import pro.belbix.epcomparator.repositories2.ImportantEventsRepository2;
import pro.belbix.epcomparator.repositories2.IncomeRepository2;
import pro.belbix.epcomparator.repositories2.RewardsRepository2;
import pro.belbix.epcomparator.repositories2.TransferRepository2;
import pro.belbix.epcomparator.repositories2.UniswapRepository2;

@Service
@Log4j2
public class Comparator {

    private final HardWorkRepository1 hardWorkRepository1;
    private final HardWorkRepository2 hardWorkRepository2;

    private final HarvestRepository1 harvestRepository1;
    private final HarvestRepository2 harvestRepository2;

    private final ImportantEventsRepository1 importantEventsRepository1;
    private final ImportantEventsRepository2 importantEventsRepository2;

    private final IncomeRepository1 incomeRepository1;
    private final IncomeRepository2 incomeRepository2;

    private final RewardsRepository1 rewardsRepository1;
    private final RewardsRepository2 rewardsRepository2;

    private final TransferRepository1 transferRepository1;
    private final TransferRepository2 transferRepository2;

    private final UniswapRepository1 uniswapRepository1;
    private final UniswapRepository2 uniswapRepository2;

    public Comparator(HardWorkRepository1 hardWorkRepository1,
                      HardWorkRepository2 hardWorkRepository2,
                      HarvestRepository1 harvestRepository1,
                      HarvestRepository2 harvestRepository2,
                      ImportantEventsRepository1 importantEventsRepository1,
                      ImportantEventsRepository2 importantEventsRepository2,
                      IncomeRepository1 incomeRepository1,
                      IncomeRepository2 incomeRepository2,
                      RewardsRepository1 rewardsRepository1,
                      RewardsRepository2 rewardsRepository2,
                      TransferRepository1 transferRepository1,
                      TransferRepository2 transferRepository2,
                      UniswapRepository1 uniswapRepository1,
                      UniswapRepository2 uniswapRepository2) {
        this.hardWorkRepository1 = hardWorkRepository1;
        this.hardWorkRepository2 = hardWorkRepository2;
        this.harvestRepository1 = harvestRepository1;
        this.harvestRepository2 = harvestRepository2;
        this.importantEventsRepository1 = importantEventsRepository1;
        this.importantEventsRepository2 = importantEventsRepository2;
        this.incomeRepository1 = incomeRepository1;
        this.incomeRepository2 = incomeRepository2;
        this.rewardsRepository1 = rewardsRepository1;
        this.rewardsRepository2 = rewardsRepository2;
        this.transferRepository1 = transferRepository1;
        this.transferRepository2 = transferRepository2;
        this.uniswapRepository1 = uniswapRepository1;
        this.uniswapRepository2 = uniswapRepository2;
    }

    public void start() {

        hardWorkRepository1.saveAll(
            findMissedElementsForFirstInSecond(
                "hardWorkRepository",
                hardWorkRepository1,
                hardWorkRepository2,
                HardWorkDTO::getId));

        harvestRepository1.saveAll(
            findMissedElementsForFirstInSecond(
                "harvestRepository",
                harvestRepository1,
                harvestRepository2,
                HarvestDTO::getId));

        importantEventsRepository1.saveAll(
            findMissedElementsForFirstInSecond(
                "importantEventsRepository",
                importantEventsRepository1,
                importantEventsRepository2,
                ImportantEventsDTO::getId));

        incomeRepository1.saveAll(
            findMissedElementsForFirstInSecond(
                "incomeRepository",
                incomeRepository1,
                incomeRepository2,
                IncomeEntity::getId));

        rewardsRepository1.saveAll(
            findMissedElementsForFirstInSecond(
                "rewardsRepository",
                rewardsRepository1,
                rewardsRepository2,
                RewardDTO::getId));

        transferRepository1.saveAll(
            findMissedElementsForFirstInSecond(
                "transferRepository",
                transferRepository1,
                transferRepository2,
                TransferDTO::getId));

        uniswapRepository1.saveAll(
            findMissedElementsForFirstInSecond(
                "uniswapRepository",
                uniswapRepository1,
                uniswapRepository2,
                UniswapDTO::getId));
    }

    private <T> List<T> findMissedElementsForFirstInSecond(
        String name,
        JpaRepository<T, String> repository1,
        JpaRepository<T, String> repository2,
        Function<T, String> idGetter) {

        long c1 = repository1.count();
        long c2 = repository2.count();

        log.info(name + " c1 {} c2 {}", c1, c2);
        if (c1 == c2) {
            return List.of();
        }

        List<T> dtos1 = repository1.findAll();
        List<T> dtos2 = repository2.findAll();
        Map<String, T> map1 = dtos1.stream().collect(toMap(idGetter, a -> a));
        List<T> missed = dtos2.stream()
            .filter(dto -> {
                boolean r = map1.containsKey(idGetter.apply(dto));
                if (!r) {
                    log.trace("Missed " + dto);
                }
                return !r;
            })
            .collect(Collectors.toList());
        log.info(name + " missed " + missed.size());
        return missed;
    }

}
