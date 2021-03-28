package jocDeDaus.util.assembler;

import jocDeDaus.dto.PlayerDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import jocDeDaus.controller.PlayerController;
import jocDeDaus.entity.Player;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Clase de la capa Controller
 *
 * Implemente la interfaz RepresentationModelAssembler
 * (pertenece al modulo de Spring HATEOAS, org.springframework.hateoas.server)
 *
 * Convierte un objeto de dominio en un RepresentationModel, que es una clase base
 * para que los DTO recopilen enlaces, un EntityModel simple que envuelve un objeto
 * de dominio y le agrega enlaces.
 *
 * Utiliza el objeto ModelMapper para realizar el mapeo de objetos de tipo Player
 * a objetos de tipo PlayerDto.
 * Marcada con la anotacion @Autowired, la clase ModelMapper es automaticamente detectada por Spring
 *
 * Anotaciones:
 *
 * @Component
 * Indica que una clase es un "componente".
 * Estas clases se consideran candidatas para la detección automatica cuando se utiliza una configuracion
 * basada en anotaciones y un escaneo de classpath.
 * Tambien se pueden considerar otras anotaciones a nivel de clase como identificacion de un componente,
 * normalmente un tipo especial de componente: por ejemplo, la anotacion @Repository
 *
 */
@Component
public class PlayerModelAssembler implements RepresentationModelAssembler<Player, EntityModel<PlayerDto>> {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Metodo abstracto de la interfaz RepresentationModelAssembler
     * Convierte un objeto de tipo Player en un EntityModel de tipo PlayerDto.
     * El objeto EntityModel envuelve un objeto de dominio y le agrega enlaces
     *
     * @param player, objeto de tipo Player
     * @return objeto de tipo EntityModel que envuelve a un objeto de tipo PlayerDto
     * y le agrega enlaces
     */
    @Override
    public EntityModel<PlayerDto> toModel(Player player) {
        PlayerDto playerDto = convertToDto(player);

        return EntityModel.of(playerDto,
                linkTo(methodOn(PlayerController.class).one(player.getIdPlayer())).withSelfRel(),
                linkTo(methodOn(PlayerController.class).newPlayer(player)).withRel("new"),
                linkTo(methodOn(PlayerController.class).updatePlayer(player, player.getIdPlayer())).withRel("update"),
                linkTo(methodOn(PlayerController.class).deletePlayer(player.getIdPlayer())).withRel("delete"),
                linkTo(methodOn(PlayerController.class).allPlayers()).withRel("all"));
    }

    /**
     * Realizar el mapeo de objetos de tipo Player a objetos de tipo PlayerDto, haciendo uso de un objeto
     * de tipo ModelMapper qu es injectado en la clase mediante la anotacion @Autowired
     *
     * @param player, objeto de tipo Player
     * @return objeto de tipo PlayerDto
     */
    public PlayerDto convertToDto(Player player){
        PlayerDto playerDto = modelMapper.map(player, PlayerDto.class);
        return playerDto;
    }

}
