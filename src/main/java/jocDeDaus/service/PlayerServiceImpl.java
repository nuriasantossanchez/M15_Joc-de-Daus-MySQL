package jocDeDaus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jocDeDaus.entity.Player;
import jocDeDaus.entity.Game;
import jocDeDaus.repository.IPlayerRepository;

import java.util.List;
import java.util.Optional;

/**
 * Clase de la capa Service, implementa la interface IPlayerService
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
public class PlayerServiceImpl implements IPlayerService {

    @Autowired
    IPlayerRepository iPlayerRepository;


    @Override
    public Player savePlayer(Player player) {
        return iPlayerRepository.save(player);
    }

    @Override
    public Optional<Player> findPlayerById(Long idPlayer) {
        return iPlayerRepository.findById(idPlayer);
    }

    @Override
    public List<Player> listPlayers() {
        return iPlayerRepository.findAll();
    }
}
