package pro.belbix.epcomparator.repositories1;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.belbix.epcomparator.entity.HarvestTvlEntity;

public interface HarvestTvlRepository1 extends JpaRepository<HarvestTvlEntity, String> {

    HarvestTvlEntity findFirstByCalculateTimeAndLastTvl(long time, double lastTvl);

}
