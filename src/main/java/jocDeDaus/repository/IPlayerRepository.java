package jocDeDaus.repository;

import jocDeDaus.entity.Game;
import jocDeDaus.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Interface de la capa Repository, extiende JpaRepository
 *
 */
@Repository
@Transactional
public interface IPlayerRepository extends JpaRepository<Player, Long> {

}
