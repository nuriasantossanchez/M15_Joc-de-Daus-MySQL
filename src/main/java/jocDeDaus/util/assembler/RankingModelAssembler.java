package jocDeDaus.util.assembler;

import jocDeDaus.controller.PlayerController;
import jocDeDaus.dto.RankingDto;
import jocDeDaus.entity.Ranking;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
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
public class RankingModelAssembler implements RepresentationModelAssembler<Ranking, EntityModel<RankingDto>> {

    @Autowired
    private ModelMapper modelMapper;

    public EntityModel<RankingDto> toModel(Ranking ranking) {

        RankingDto rankingDto = convertToDto(ranking);

        return EntityModel.of(rankingDto,
                        linkTo(methodOn(PlayerController.class).averageSuccessRankingAllPlayers()).withSelfRel(),
                        linkTo(methodOn(PlayerController.class).playerLoser()).withRel("loser"),
                        linkTo(methodOn(PlayerController.class).playerWinner()).withRel("winner"));
    }

    public RankingDto convertToDto(Ranking ranking) {
        RankingDto rankingDto = modelMapper.map(ranking, RankingDto.class);
        return rankingDto;
    }
}
