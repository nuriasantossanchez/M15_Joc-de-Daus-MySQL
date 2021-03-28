package jocDeDaus.util.assembler;

import jocDeDaus.controller.PlayerController;
import jocDeDaus.dto.GameDto;
import jocDeDaus.dto.RankingDto;
import jocDeDaus.entity.Game;
import jocDeDaus.entity.Player;
import jocDeDaus.entity.Ranking;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

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
 * Utiliza el objeto ModelMapper para realizar el mapeo de objetos de tipo Game
 * a objetos de tipo GameDto.
 * Marcada con la anotacion @Autowired, la clase ModelMapper es automaticamente detectada por Spring
 *
 * Anotaciones:
 *
 * @Component
 * Indica que una clase es un "componente".
 * Estas clases se consideran candidatas para la deteccion automatica cuando se utiliza una configuracion
 * basada en anotaciones y un escaneo de classpath.
 * Tambien se pueden considerar otras anotaciones a nivel de clase como identificacion de un componente,
 * normalmente un tipo especial de componente: por ejemplo, la anotación @Repository
 *
 */
@Component
public class RankingModelAssembler implements RepresentationModelAssembler<Ranking, EntityModel<RankingDto>> {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Metodo abstracto de la interfaz RepresentationModelAssembler
     * Convierte un objeto de tipo Game en un EntityModel de tipos GameDto.
     * El objeto EntityModel envuelve un objeto de dominio y le agrega enlaces
     *
     * @param ranking, objeto de tipo Game
     * @return objeto de tipo EntityModel que envuelve a un objeto de tipo GameDto
     * y le agrega enlaces
     */
    public EntityModel<RankingDto> toModel(Ranking ranking) {

        RankingDto rankingDto = convertToDto(ranking);

        return EntityModel.of(rankingDto,
                        linkTo(methodOn(PlayerController.class).averageSuccessRankingAllPlayers()).withSelfRel(),
                        linkTo(methodOn(PlayerController.class).playerLoser()).withRel("loser"),
                        linkTo(methodOn(PlayerController.class).playerWinner()).withRel("winner"));
    }

    /**
     * Realizar el mapeo de objetos de tipo Game a objetos de tipo GameDto, haciendo uso de un objeto
     * de tipo ModelMapper qu es injectado en la clase mediante la anotacion @Autowired
     *
     * @param ranking, objeto de tipo Ranking
     * @return objeto de tipo RankingDto
     */
    public RankingDto convertToDto(Ranking ranking) {
        RankingDto rankingDto = modelMapper.map(ranking, RankingDto.class);
        return rankingDto;
    }

}
