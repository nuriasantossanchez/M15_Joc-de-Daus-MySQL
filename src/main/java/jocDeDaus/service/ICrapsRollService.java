package jocDeDaus.service;

import jocDeDaus.entity.CrapsRoll;
import jocDeDaus.entity.Player;
import java.util.List;

/**
 * Interface de la capa Service
 *
 */

public interface ICrapsRollService {

    CrapsRoll saveCrapsRoll(CrapsRoll crapsRoll); // save crapsRoll

    void deleteCrapsRollsByPlayer(List<CrapsRoll> crapsRolls); // delete crapsRolls by player

    List<CrapsRoll> listCrapsRollsByPlayer(Player player); // list all crapsRolls by player

}
