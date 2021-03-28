package jocDeDaus.controller;

import jocDeDaus.dto.CrapsRollDto;
import jocDeDaus.dto.PlayerDto;
import jocDeDaus.dto.RankingDto;
import jocDeDaus.entity.CrapsRoll;
import jocDeDaus.entity.Ranking;
import jocDeDaus.service.ICrapsRollService;
import jocDeDaus.service.IGameService;
import jocDeDaus.service.IPlayerService;
import jocDeDaus.util.assembler.CrapsRollModelAssembler;
import jocDeDaus.util.IUtilities;
import jocDeDaus.util.assembler.RankingModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jocDeDaus.controller.exception.PlayerNotFoundException;
import jocDeDaus.entity.Player;
import jocDeDaus.entity.Game;
import jocDeDaus.util.assembler.PlayerModelAssembler;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * Clase de la capa Controller
 *
 * La anotacion @RestController convierte a la aplicacion en un REST Service, basado en el intercambio
 * de recursos (elementos de informacion) entre componentes de la red, clientes y servidores, que
 * se comunican a traves del protocolo HTTP
 *
 * Anotaciones:
 *
 * @RestController
 * Anotacion que a su vez esta anotada con @Controller y @ResponseBody.
 * Los tipos que llevan esta anotacion se tratan como controladores donde los metodos @RequestMapping
 * asumen la semantica @ResponseBody por defecto
 *
 * @Autowired
 * Marca un constructor, campo, metodo setter o metodo de configuracion para ser detectado
 * automaticamente por la funcionalidad de inyeccion de dependencias de Spring
 *
 * @GetMapping
 * Anotacion compuesta que actua como un atajo para @RequestMapping(method = RequestMethod.GET).
 * El punto de acceso a la peticion sera http://localhost:8081/{path}, en este caso
 * (el puerto 8081 queda especificado en el archivo application.properties, del directorio resources)
 *
 * @PostMapping
 * Anotacion compuesta que actua como un atajo para @RequestMapping(method = RequestMethod.POST).
 *
 * @PutMapping
 * Anotacion compuesta que actua como un atajo para @RequestMapping(method = RequestMethod.PUT).
 *
 * @DeleteMapping
 * Anotacion compuesta que actua como un atajo para @RequestMapping(method = RequestMethod.DELETE).
 */
@RestController
public class PlayerController{

    private final IUtilities iUtilities;
    private final IPlayerService iPlayerService;
    private final ICrapsRollService iCrapsRollService;
    private final IGameService iGameService;
    private final PlayerModelAssembler playerModelAssembler;
    private final CrapsRollModelAssembler crapsRollModelAssembler;
    private final RankingModelAssembler rankingModelAssembler;

    /**
     * Constructor de la clase
     *
     * @param iUtilities
     * @param iPlayerService
     * @param iCrapsRollService
     * @param iGameService
     * @param playerModelAssembler
     * @param crapsRollModelAssembler
     * @param rankingModelAssembler
     */
    @Autowired
    public PlayerController(IUtilities iUtilities, IPlayerService iPlayerService,
                            ICrapsRollService iCrapsRollService,
                            IGameService iGameService,
                            PlayerModelAssembler playerModelAssembler,
                            CrapsRollModelAssembler crapsRollModelAssembler,
                            RankingModelAssembler rankingModelAssembler) {

        this.iUtilities = iUtilities;
        this.iPlayerService = iPlayerService;
        this.iCrapsRollService = iCrapsRollService;
        this.iGameService = iGameService;
        this.playerModelAssembler = playerModelAssembler;
        this.crapsRollModelAssembler = crapsRollModelAssembler;
        this.rankingModelAssembler = rankingModelAssembler;
    }

    /**
     * Mapeo de una peticion HTTP POST, a la URL http://localhost:8081/players
     *
     * Crea un jugador
     *
     * @param newPlayer
     * @return objeto generico de tipo ResponseEntity con enlaces agregados
     */
    @PostMapping("/players")
    public ResponseEntity<?> newPlayer(@Valid @RequestBody Player newPlayer) {
        List<Player> players = iPlayerService.listPlayers();
        Optional<Player> uniqueNickName = iUtilities.checkUniqueNickName(newPlayer, players);

        if (!uniqueNickName.isPresent()){
            Player player = iPlayerService.savePlayer(newPlayer);

            Double successRanking = iUtilities.computePlayerSuccessRanking(player.getCrapsRolls());
            player.setRanking(successRanking);

            EntityModel<PlayerDto> entityModel = playerModelAssembler.toModel(player);
            return ResponseEntity
                    .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Problem.create()
                .withTitle("Please select another Nick Name.")
                .withDetail("There is another player with that Nick Name."));
    }

    /**
     * Mapeo de una peticion HTTP PUT, a la URL http://localhost:8081/players/{id}
     *
     * Modifica el nombre de un jugador
     *
     * @param newPlayer
     * @param idPlayer
     * @return objeto generico de tipo ResponseEntity con enlaces agregados
     */
    @PutMapping("/players/{id}")
    public ResponseEntity<?> updatePlayer(@Valid @RequestBody Player newPlayer, @PathVariable(name="id") Long idPlayer) {
        List<Player> players = iPlayerService.listPlayers();

        Optional<Player> uniqueNickName = iUtilities.checkUniqueNickName(newPlayer, players);
        if (!uniqueNickName.isPresent()){
            Player updatedPlayer = iPlayerService.findPlayerById(idPlayer)
                    .map(player -> {
                        player.setName(newPlayer.getName().trim());

                        Double successRanking = iUtilities.computePlayerSuccessRanking(player.getCrapsRolls());
                        player.setRanking(successRanking);

                        return iPlayerService.savePlayer(player);
                    })
                    .orElseGet(() -> {
                        newPlayer.setIdPlayer(idPlayer);

                        Double successRanking = iUtilities.computePlayerSuccessRanking(newPlayer.getCrapsRolls());
                        newPlayer.setRanking(successRanking);

                        return iPlayerService.savePlayer(newPlayer);
                    });

            EntityModel<PlayerDto> playerDto = playerModelAssembler.toModel(updatedPlayer);
            return ResponseEntity
                    .created(playerDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(playerDto);
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Problem.create()
                        .withTitle("Please select another Nick Name.")
                        .withDetail("There is another player with that Nick Name."));
    }

    /**
     * Mapeo de una peticion HTTP GET, a la URL http://localhost:8081/players/{id}
     *
     * Get one player
     *
     * @param idPlayer
     * @return objeto generico de tipo ResponseEntity con enlaces agregados
     */
    @GetMapping("/players/{id}")
    public ResponseEntity<?> one(@PathVariable(name="id") Long idPlayer) {
        Player player = iPlayerService.findPlayerById(idPlayer)
                .orElseThrow(() -> new PlayerNotFoundException(idPlayer));

        EntityModel<PlayerDto> playerDto = playerModelAssembler.toModel(player);
        return ResponseEntity
                .created(playerDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(playerDto);
    }

    /**
     * Mapeo de una peticion HTTP DELETE, a la URL http://localhost:8081/players/{id}
     *
     * Delete one player
     *
     * @param idPlayer
     * @return objeto generico de tipo ResponseEntity con enlaces agregados
     */
    @DeleteMapping("/players/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable(name="id") Long idPlayer) {
        Player player = iPlayerService.findPlayerById(idPlayer)
                .orElseThrow(() -> new PlayerNotFoundException(idPlayer));

        iPlayerService.deletePlayer(player);
        return ResponseEntity.noContent().build();
    }

    /**
     * Mapeo de una peticion HTTP GET, a la URL http://localhost:8081/players
     *
     * Listado de jugadores junto con su porcentaje medio de exito
     *
     * @return objeto generico de tipo ResponseEntity con enlaces agregados
     */
    @GetMapping("/players")
    public ResponseEntity<?> allPlayers(){
        List<Player> players = iPlayerService.listPlayers();

        players.forEach(p -> p.setRanking(iUtilities.computePlayerSuccessRanking(p.getCrapsRolls())));

        List<EntityModel<PlayerDto>> playersDto = players.stream()
                .map(playerModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<PlayerDto>> collectionModel =
                CollectionModel.of(playersDto,
                        linkTo(methodOn(PlayerController.class).allPlayers()).withSelfRel());

        return ResponseEntity
                .created(collectionModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(collectionModel);
    }

    /**
     * Mapeo de una peticion HTTP POST, a la URL http://localhost:8081/players/{id}/games
     *
     * Jugador realiza tirada de dados
     *
     * @param idPlayer
     * @return objeto generico de tipo ResponseEntity con enlaces agregados
     */
    @PostMapping("/players/{id}/games")
    public ResponseEntity<?> newCrapsRollPlayer(@PathVariable(name="id") Long idPlayer) {
        Player player = iPlayerService.findPlayerById(idPlayer)
                .orElseThrow(() -> new PlayerNotFoundException(idPlayer));

        CrapsRoll newCrapsRoll = iUtilities.generateNewCrapsRoll(idPlayer);

        Game game = iUtilities.generateNewGame(player);

        game.setIdCrapsRoll(iGameService.countAll()+1);

        game.setGameResult(newCrapsRoll.getRollResult().equals(7)?true:false);

        newCrapsRoll.setGame(game);

        CrapsRoll crapsRollPlayer= iCrapsRollService.saveCrapsRoll(newCrapsRoll);

        EntityModel<CrapsRollDto> entityModel = crapsRollModelAssembler.toModel(crapsRollPlayer);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /**
     * Mapeo de una peticion HTTP GET, a la URL http://localhost:8081/players/{id}/games
     *
     * Listado de tiradas de un jugador
     *
     * @param idPlayer
     * @return objeto generico de tipo ResponseEntity con enlaces agregados
     */
    @GetMapping("/players/{id}/games")
    public ResponseEntity<?> allCrapsRollsByPlayer(@PathVariable(name="id") Long idPlayer) {
        Player player = iPlayerService.findPlayerById(idPlayer)
                .orElseThrow(() -> new PlayerNotFoundException(idPlayer));

        List<CrapsRoll> crapsRollsPlayer= iCrapsRollService.listCrapsRollsByPlayer(player);

        if (!crapsRollsPlayer.isEmpty()){
            List<EntityModel<CrapsRollDto>> crapsRollsByPlayerDto = crapsRollsPlayer.stream()
                    .map(crapsRollModelAssembler::toModel)
                    .collect(Collectors.toList());

            CollectionModel<EntityModel<CrapsRollDto>> collectionModel =
                    CollectionModel.of(crapsRollsByPlayerDto,
                            linkTo(methodOn(PlayerController.class).allCrapsRollsByPlayer(idPlayer)).withSelfRel());

            return ResponseEntity
                    .created(collectionModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(collectionModel);
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * Mapeo de una peticion HTTP DELETE, a la URL http://localhost:8081/players/{id}/games
     *
     * Elimina tiradas de un jugador
     *
     * @param idPlayer
     * @return objeto generico de tipo ResponseEntity con enlaces agregados
     */
    @DeleteMapping("/players/{id}/games")
    public ResponseEntity<?> deleteCrapsRollsByPlayer(@PathVariable(name="id") Long idPlayer) {
        Player player = iPlayerService.findPlayerById(idPlayer)
                .orElseThrow(() -> new PlayerNotFoundException(idPlayer));

        List<CrapsRoll> crapsRollsByPlayer = iCrapsRollService.listCrapsRollsByPlayer(player);

        iCrapsRollService.deleteCrapsRollsByPlayer(crapsRollsByPlayer);
        return ResponseEntity.noContent().build();
    }

    /**
     * Mapeo de una peticion HTTP GET, a la URL http://localhost:8081/players/ranking
     *
     * Ranking medio, porcentaje medio de exito de todos los jugadores
     *
     * @return objeto generico de tipo ResponseEntity con enlaces agregados
     */
    @GetMapping("/players/ranking")
    public ResponseEntity<?> averageSuccessRankingAllPlayers() {
        List<Game> allGames = iGameService.allGames();

        Double averageRankingAllPlayers = iUtilities.computeAverageRankingAllPlayers(allGames);
        Ranking ranking = new Ranking();
        ranking.setAverageRankingAllPlayers(averageRankingAllPlayers);
        ranking.setGames(allGames);

        EntityModel<RankingDto> rankingDto = rankingModelAssembler.toModel(ranking);

        return ResponseEntity
                .created(rankingDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(rankingDto);
    }

    /**
     * Mapeo de una peticion HTTP GET, a la URL http://localhost:8081/players/ranking/loser
     *
     * Jugador con peor porcentaje de exito
     *
     * @return objeto generico de tipo ResponseEntity con enlaces agregados
     */
    @GetMapping("/players/ranking/loser")
    public ResponseEntity<?> playerLoser() {
        List<Player> allPlayers = iPlayerService.listPlayers();

        Optional<Player> playerLoser = iUtilities.getWorstPlayer(allPlayers);

        EntityModel<PlayerDto> playerDto = playerModelAssembler.toModel(playerLoser.get());

        return ResponseEntity
                .created(playerDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(playerDto);
    }

    /**
     * Mapeo de una peticion HTTP GET, a la URL http://localhost:8081/players/ranking/winner
     *
     * Jugador con mejor porcentaje de exito
     *
     * @return objeto generico de tipo ResponseEntity con enlaces agregados
     */
    @GetMapping("/players/ranking/winner")
    public ResponseEntity<?> playerWinner() {
        List<Player> allPlayers = iPlayerService.listPlayers();

        Optional<Player> playerWinner = iUtilities.getBestPlayer(allPlayers);

        EntityModel<PlayerDto> playerDto = playerModelAssembler.toModel(playerWinner.get());

        return ResponseEntity
                .created(playerDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(playerDto);
    }
}
