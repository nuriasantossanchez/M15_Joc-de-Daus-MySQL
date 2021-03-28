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
 * Clase de la capa de Utilidades
 *
 * Implemente la interfaz RepresentationModelAssembler
 * (pertenece al modulo de Spring HATEOAS, org.springframework.hateoas.server)
 *
 * Convierte un objeto de dominio en un RepresentationModel, que es una clase base
 * para que los DTO recopilen enlaces, un EntityModel simple que envuelve un objeto
 * de dominio y le agrega enlaces.
 *
 */

@Component
public class PlayerModelAssembler implements RepresentationModelAssembler<Player, EntityModel<PlayerDto>> {

    @Autowired
    private ModelMapper modelMapper;

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

    public PlayerDto convertToDto(Player player){
        PlayerDto playerDto = modelMapper.map(player, PlayerDto.class);
        return playerDto;
    }
}
