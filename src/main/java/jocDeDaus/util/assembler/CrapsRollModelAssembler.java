package jocDeDaus.util.assembler;

import jocDeDaus.dto.CrapsRollDto;
import jocDeDaus.entity.CrapsRoll;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import jocDeDaus.controller.PlayerController;
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
public class CrapsRollModelAssembler implements RepresentationModelAssembler<CrapsRoll, EntityModel<CrapsRollDto>> {

    @Autowired
    private ModelMapper modelMapper;

    public EntityModel<CrapsRollDto> toModel(CrapsRoll crapsRoll) {

        CrapsRollDto crapsRollDto = convertToDto(crapsRoll);

        return EntityModel.of(crapsRollDto,
                linkTo(methodOn(PlayerController.class).newCrapsRollPlayer(crapsRoll.getIdPlayer())).withSelfRel(),
                linkTo(methodOn(PlayerController.class).allCrapsRollsByPlayer(crapsRoll.getIdPlayer())).withRel("all"),
                linkTo(methodOn(PlayerController.class).deleteCrapsRollsByPlayer(crapsRoll.getIdPlayer())).withRel("delete"));
    }

    public CrapsRollDto convertToDto(CrapsRoll crapsRoll) {
        CrapsRollDto crapsRollDto = modelMapper.map(crapsRoll, CrapsRollDto.class);
        return crapsRollDto;
    }
}
