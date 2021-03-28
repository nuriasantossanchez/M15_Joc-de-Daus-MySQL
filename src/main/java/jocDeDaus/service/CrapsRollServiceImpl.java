package jocDeDaus.service;

import jocDeDaus.entity.CrapsRoll;
import jocDeDaus.entity.Player;
import jocDeDaus.repository.ICrapsRollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;


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
}
