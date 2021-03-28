package jocDeDaus.repository;

import jocDeDaus.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface de la capa Repository, extiende JpaRepository
 *
 */

@Repository
@Transactional
public interface IPlayerRepository extends JpaRepository<Player, Long> {

}
