package jocDeDaus.service;

import jocDeDaus.entity.CrapsRoll;
import jocDeDaus.entity.Game;
import jocDeDaus.entity.Player;
import jocDeDaus.repository.ICrapsRollRepository;
import jocDeDaus.repository.IGameRepository;
import jocDeDaus.repository.IPlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * Clase de la capa Service, implementa la interface ICrapsRollService
 *
 * Anotaciones:
 * @Service
 * Indica que la clase es un "Servicio", esto es, una operacion ofrecida como una interface que esta solo en el modelo,
 * sin un estado encapsulado.
 *
 * Sirve como una especializacion de @Component, lo que permite que las clases de implementacion se detecten
 * automaticamente a traves del escaneo del classpath
 *
 * @Autowired
 * Marca un constructor, campo, metodo setter o metodo de configuracion para ser detectado
 * automaticamente por la funcionalidad de inyeccion de dependencias de Spring
 *
 */
@Service
public class CrapsRollServiceImpl implements ICrapsRollService {

    @Autowired
    ICrapsRollRepository iCrapsRollRepository;


    @Override
    public CrapsRoll saveCrapsRoll(CrapsRoll crapsRoll) {
        return iCrapsRollRepository.save(crapsRoll);
    }

    @Override
    public void deleteCrapsRollsByPlayer(List<CrapsRoll> crapsRolls) {
        iCrapsRollRepository.deleteInBatch(crapsRolls);
    }

    @Override
    public List<CrapsRoll> listCrapsRollsByPlayer(Player player) {
        return iCrapsRollRepository.findCrapsRollsByPlayer(player);
    }


    @Override
    public List<Player> computeSuccessRankingEveryPLayer(List<CrapsRoll> crapsRolls) {
        /*
        Predicate winPredicate =  i -> i.equals(7);

        BiFunction<Integer, Integer, List<Double>> func =
                (wins, all) -> Collections.singletonList(Double.valueOf(wins / all) * 100);

        List<Player> players = iPlayerRepository.findAll();

        Map<Long,List<CrapsRoll>> successCrapsRollsByPlayerMap = (Map<Long, List<CrapsRoll>>) players.stream()
                .map (Player::getCrapsRolls)
                .filter(winPredicate)
                .collect(toMap(Player::getIdPlayer,Player::getCrapsRolls));


        // Partition students into passing and failing
        Map<Boolean, List<CrapsRoll>> passingFailing = crapsRolls.stream()
                .collect(partitioningBy(s -> s.getRollResult().equals(7)));

         */

        return null;
    }
}
