package pro.belbix.epcomparator.repositories2;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pro.belbix.epcomparator.dto.ImportantEventsDTO;

public interface ImportantEventsRepository2 extends JpaRepository<ImportantEventsDTO, String> {

    List<ImportantEventsDTO> findAllByOrderByBlockDate();

    ImportantEventsDTO findFirstByOrderByBlockDateDesc();
}
