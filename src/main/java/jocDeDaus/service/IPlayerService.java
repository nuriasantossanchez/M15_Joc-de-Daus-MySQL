package jocDeDaus.service;

import jocDeDaus.entity.Player;
import java.util.List;
import java.util.Optional;

/**
 * Interface de la capa Service
 *
 */

public interface IPlayerService {

    Player savePlayer(Player player); // save a player

    Optional<Player> findPlayerById(Long idPlayer); // find player by Id

    List<Player> listPlayers(); // list all players

    void deletePlayer(Player player); // delete a player
}
