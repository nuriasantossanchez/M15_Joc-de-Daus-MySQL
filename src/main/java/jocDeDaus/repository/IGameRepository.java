package jocDeDaus.repository;

import jocDeDaus.entity.CrapsRoll;
import jocDeDaus.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jocDeDaus.entity.Game;

import java.util.List;


/**
 * Interface de la capa Repository, extiende JpaRepository
 *
 */
@Repository
@Transactional
public interface IGameRepository extends JpaRepository<Game, Long> {

}
