package pro.belbix.epcomparator.repositories2;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.belbix.epcomparator.entity.HarvestTvlEntity;

public interface HarvestTvlRepository2 extends JpaRepository<HarvestTvlEntity, String> {

    HarvestTvlEntity findFirstByCalculateTimeAndLastTvl(long time, double lastTvl);

}
