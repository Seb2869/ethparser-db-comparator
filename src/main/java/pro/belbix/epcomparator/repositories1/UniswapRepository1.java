package pro.belbix.epcomparator.repositories1;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.belbix.epcomparator.dto.UniswapDTO;

public interface UniswapRepository1 extends JpaRepository<UniswapDTO, String> {

    @Query(nativeQuery = true, value = ""
        + "select count(owner) from ( "
        + "                  select owner, "
        + "                         SUBSTRING_INDEX(MAX(CONCAT(block_date, '_', owner_balance_usd)), '_', -1) balance "
        + "                  from uni_tx "
        + "                   where block_date <= :block_date "
        + "                  group by owner "
        + "              ) t "
        + "where balance > 10 ")
    Integer fetchOwnerCount(@Param("block_date") long blockDate);

    UniswapDTO findFirstByCoinOrderByBlockDesc(String coin);

    UniswapDTO findFirstByBlockDateBeforeAndCoinOrderByBlockDesc(long blockDate, String coin);

    @Query("select sum(t.amount) from UniswapDTO t "
        + "where t.coin = 'FARM' and t.owner = :owner and t.blockDate <= :from")
    List<Double> fetchAmountSum(@Param("from") long from, @Param("owner") String owner, Pageable pageable);

    @Query("select sum(t.otherAmount) from UniswapDTO t "
        + "where t.coin = 'FARM' and t.owner = :owner and t.blockDate <= :from")
    List<Double> fetchAmountSumUsd(@Param("from") long from, @Param("owner") String owner, Pageable pageable);

    List<UniswapDTO> findAllByOwnerAndCoinOrderByBlockDate(String owner, String coin);

    @Query("select t from UniswapDTO t where "
        + "t.owner = :owner and t.coin = 'FARM' and t.blockDate > :from and t.blockDate <= :to order by t.blockDate asc")
    List<UniswapDTO> fetchAllByOwner(@Param("owner") String owner, @Param("from") long from, @Param("to") long to);

    List<UniswapDTO> findAllByOrderByBlockDate();

    List<UniswapDTO> findAllByBlockDateGreaterThanOrderByBlockDate(long from);

    @Query("select t from UniswapDTO t where "
        + "t.ownerBalance is null "
        + "or t.ownerBalanceUsd is null "
        + "order by t.blockDate asc")
    List<UniswapDTO> fetchAllWithoutOwnerBalance();

}
