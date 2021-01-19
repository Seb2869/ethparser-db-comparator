package pro.belbix.epcomparator.repositories1;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pro.belbix.epcomparator.dto.ImportantEventsDTO;

public interface ImportantEventsRepository1 extends JpaRepository<ImportantEventsDTO, String> {

    List<ImportantEventsDTO> findAllByOrderByBlockDate();

    ImportantEventsDTO findFirstByOrderByBlockDateDesc();
}
