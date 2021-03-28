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
 * Clase de la capa Controller.
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
     * Constructor de la clase, parametrizado con las interfaces IGameService, IPlayerService y las clases
     * CrapsRollModelAssembler y PlayerModelAssembler, que implementan la interface RepresentationModelAssembler
     * Marcado con la anotacion @Autowired, la clase controlador es automaticamente detectada por Spring
     * @param iPlayerService, interfaz de tipo IPlayerService, implementada por la clase PlayerServiceImpl,
     *                      en la que se exponen los servicios o funcionalidades accesibles via HTTP
     *
     * @param iGameService, interfaz de tipo IGameService, implementada por la clase GameServiceImpl,
     * @param playerModelAssembler, instancia de tipo PlayerModelAssembler, convierte un objeto de dominio en
     * @param crapsRollModelAssembler, instancia de tipo CrapsRollModelAssembler, convierte un objeto de dominio en
*                          un RepresentationModel, esto es, un EntityModel que envuelve al objeto de dominio
     * @param iUtilities
     * @param iCrapsRollService
     * @param rankingModelAssembler
     *

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
     * Representa el mapeo de una peticion HTTP POST, a la URL
     * http://localhost:8081/shops
     *
     * Accede a la capa de servicio GameServiceImpl mediante su interface IGameService
     * y hace uso del servicio 'saveShop(newGame)' para salvar el nuevo objeto creado,
     * de tipo Game, y lo retorna en forma de ResponseEntity, esto es, modelando el objeto
     * de dominio a objeto DTO y agregando enlaces
     *
     * @param newPlayer, tipo Game anotado con @RequestBody para indicar que el parametro de metodo
     *                 debe estar vinculada al cuerpo de la solicitud web.
     *                 El cuerpo de la solicitud se pasa en formato JSON, segun el tipo de contenido de la solicitud.
     *                 Se aplica la validacion automatica anotando el argumento con @Valid
     *
     * @return objeto generico de tipo ResponseEntity, formado por un objeto de tipo GameDto,
     * que contiene la nueva tienda creada, junto con enlaces agregados
     */
    @PostMapping("/players")
    public ResponseEntity<?> newPlayer(@Valid @RequestBody Player newPlayer) {
        // crea un jugador

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
     * Representa el mapeo de una peticion HTTP PUT, a la URL http://localhost:8181/employees/{valor numerico}
     *
     * Accede a la capa de servicio EmployeeServiceImpl mediante su interface IEmployeeService
     * y hace uso del metodo 'saveEmployee(employee)' para salvar el objeto modificado o crear uno nuevo,
     * en caso de que no exista en el sistema un empleado con un id que corresponda al valor numerico
     * pasado en la URL
     *
     * @param newPlayer, tipo Employee anotado con @RequestBody para indicar que el parametro de metodo
     *                     debe estar vinculada al cuerpo de la solicitud web.
     *                     El cuerpo de la solicitud se pasa en formato JSON, segun el tipo de contenido de la solicitud.
     *                     Se aplica la validacion automatica anotando el argumento con @Valid
     *
     * @param idPlayer, tipo Long anotado con @PathVariable para indicar que es un parametro de metodo
     *            y debe estar vinculado a una variable de tipo plantilla de URI (URI template)
     *            Indica el id del empleado a modificar. En caso de que no exista un empleado
     *            con ese id, crea uno nuevo
     *
     * @return objeto generico de tipo ResponseEntity, formado por un objeto de tipo EmployeeDto,
     * que contiene el empleado modificado (o nuevo), junto con enlaces agregados
     */
    @PutMapping("/players/{id}")
    public ResponseEntity<?> updatePlayer(@Valid @RequestBody Player newPlayer, @PathVariable(name="id") Long idPlayer) {
        // modifica nombre de un jugador

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
     * Representa el mapeo de una peticion HTTP PUT, a la URL http://localhost:8181/employees/{valor numerico}
     *
     * Accede a la capa de servicio EmployeeServiceImpl mediante su interface IEmployeeService
     * y hace uso del metodo 'saveEmployee(employee)' para salvar el objeto modificado o crear uno nuevo,
     * en caso de que no exista en el sistema un empleado con un id que corresponda al valor numerico
     * pasado en la URL
     *
     *
     * @param idPlayer, tipo Long anotado con @PathVariable para indicar que es un parametro de metodo
     *            y debe estar vinculado a una variable de tipo plantilla de URI (URI template)
     *            Indica el id del empleado a modificar. En caso de que no exista un empleado
     *            con ese id, crea uno nuevo
     *
     * @return objeto generico de tipo ResponseEntity, formado por un objeto de tipo EmployeeDto,
     * que contiene el empleado modificado (o nuevo), junto con enlaces agregados
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

    @DeleteMapping("/players/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable(name="id") Long idPlayer) {
        // delete one player

        Player player = iPlayerService.findPlayerById(idPlayer)
                .orElseThrow(() -> new PlayerNotFoundException(idPlayer));

        iPlayerService.deletePlayer(player);

        return ResponseEntity.noContent().build();
    }

    /**
     * Representa el mapeo de una peticion HTTP GET, a la URL
     * http://localhost:8081/players
     *
     * Accede a la capa de servicio GameServiceImpl mediante su interface IGameService
     * y hace uso del servicio 'listShops()' para recuperar un listado de tipos Game
     * en forma de ResponseEntity, esto es, modelando el objeto de dominio a objeto DTO
     * y agregando enlaces al objeto de dominio
     *
     * @return objeto generico de tipo ResponseEntity, formado por un listado de tipos GameDto,
     * que contiene todos las tiendas que hay en el sistema, junto con enlaces agregados
     */
    @GetMapping("/players")
    public ResponseEntity<?> allPlayers(){

        // listado de jugadores con su porcentaje medio de exito

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
     * Representa el mapeo de una peticion HTTP POST, a la URL
     * http://localhost:8081/players/{id}/games
     *
     * Accede a la capa de servicio GameServiceImpl mediante su interface IGameService
     * y hace uso del servicio 'findShopById(shopId)' para recuperar un objeto de tipo Game
     * cuyo id coincida con el shopId pasado en el PathVariable
     *
     * En caso de que no existiese ningun objeto Game con el shopId especificado en el PathVariable,
     * lanza una exception
     *
     * Despues accede a la capa de servicio PlayerServiceImpl mediante su interface
     * IPlayerService y hace uso del servicio 'savePicture(newPlayer)' para salvar el nuevo objeto creado
     *
     * Antes de salvar el nuevo objeto Player, comprueba la capacidad de la tienda,
     * para ello accede al servicio currentShopCapacity(shopId) de la interface IGameService.
     * En caso de que la tienda no tenga capacidad suficiente, informa con el mensaje correspondiente
     * y no hace el salvado del objeto Player
     *
     * @param idPlayer, tipo Long anotado con @PathVariable para indicar que es un parametro de metodo
     *                y debe estar vinculado a una variable de tipo plantilla de URI (URI template)
     *                Indica el id de la tienda en la que se quiere salvar un nuevo objeto de tipo Player
     *
     * @return objeto generico de tipo ResponseEntity, formado por un objeto de tipo PlayerDto,
     * que contiene el nuevo cuadro creado, junto con enlaces agregados
     */
    @PostMapping("/players/{id}/games")
    public ResponseEntity<?> newCrapsRollPlayer(@PathVariable(name="id") Long idPlayer) {

        // jugador realiza tirada de dados

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
     * Representa el mapeo de una peticion HTTP POST, a la URL
     * http://localhost:8081/shops/{id}/pictures
     *
     * Accede a la capa de servicio GameServiceImpl mediante su interface IGameService
     * y hace uso del servicio 'findShopById(shopId)' para recuperar un objeto de tipo Game
     * cuyo id coincida con el shopId pasado en el PathVariable
     *
     * En caso de que no existiese ningun objeto Game con el shopId especificado en el PathVariable,
     * lanza una exception
     *
     * Despues accede a la capa de servicio PlayerServiceImpl mediante su interface
     * IPlayerService y hace uso del servicio 'savePicture(newPlayer)' para salvar el nuevo objeto creado
     *
     * Antes de salvar el nuevo objeto Player, comprueba la capacidad de la tienda,
     * para ello accede al servicio currentShopCapacity(shopId) de la interface IGameService.
     * En caso de que la tienda no tenga capacidad suficiente, informa con el mensaje correspondiente
     * y no hace el salvado del objeto Player
     *
     * @param idPlayer, tipo Long anotado con @PathVariable para indicar que es un parametro de metodo
     *                y debe estar vinculado a una variable de tipo plantilla de URI (URI template)
     *                Indica el id de la tienda en la que se quiere salvar un nuevo objeto de tipo Player
     *
     * @return objeto generico de tipo ResponseEntity, formado por un objeto de tipo PlayerDto,
     * que contiene el nuevo cuadro creado, junto con enlaces agregados
     */
    @GetMapping("/players/{id}/games")
    public ResponseEntity<?> allCrapsRollsByPlayer(@PathVariable(name="id") Long idPlayer) {

        // listado de tiradas de un jugador

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
     * Representa el mapeo de una peticion HTTP DELETE, a la URL
     * http://shops:8081/shops/{id}/pictures}
     *
     * Accede a la capa de servicio GameServiceImpl mediante su interface IGameService
     * y hace uso del servicio 'findShopById(shopId)' para recuperar un objeto de tipo Game
     * cuyo id coincida con el shopId pasado en el PathVariable
     *
     * En caso de que no existiese ningun objeto Game con el shopId especificado en el PathVariable,
     * lanza una exception
     *
     * Despues accede a la capa de servicio PlayerServiceImpl mediante su interface IPlayerService
     * y hace uso del servicio 'firePictures(shop.getPictures())' para eliminar en lote
     * (deleteInBatch, en una sola query) el listado de cuadros asociados a una determinada tienda,
     * El servicio es parametrizado con el listado de cuadros a eliminar
     *
     * @param idPlayer, tipo Long anotado con @PathVariable para indicar que es un parametro de metodo
     *            y debe estar vinculado a una variable de tipo plantilla de URI (URI template)
     *            Indica el id de la tienda concreta en la que se eliminaran todos sus cuadros
     *
     * @return objeto generico de tipo ResponseEntity, con una respuesta de operacion valida
     */
    @DeleteMapping("/players/{id}/games")
    public ResponseEntity<?> deleteCrapsRollsByPlayer(@PathVariable(name="id") Long idPlayer) {
        // elimina tiradas de un jugador

        Player player = iPlayerService.findPlayerById(idPlayer)
                .orElseThrow(() -> new PlayerNotFoundException(idPlayer));

        List<CrapsRoll> crapsRollsByPlayer = iCrapsRollService.listCrapsRollsByPlayer(player);

        iCrapsRollService.deleteCrapsRollsByPlayer(crapsRollsByPlayer);

        return ResponseEntity.noContent().build();
    }

    /**
     * Representa el mapeo de una peticion HTTP POST, a la URL
     * http://localhost:8081/shops/{id}/pictures
     *
     * Accede a la capa de servicio GameServiceImpl mediante su interface IGameService
     * y hace uso del servicio 'findShopById(shopId)' para recuperar un objeto de tipo Game
     * cuyo id coincida con el shopId pasado en el PathVariable
     *
     * En caso de que no existiese ningun objeto Game con el shopId especificado en el PathVariable,
     * lanza una exception
     *
     * Despues accede a la capa de servicio PlayerServiceImpl mediante su interface
     * IPlayerService y hace uso del servicio 'savePicture(newPlayer)' para salvar el nuevo objeto creado
     *
     * Antes de salvar el nuevo objeto Player, comprueba la capacidad de la tienda,
     * para ello accede al servicio currentShopCapacity(shopId) de la interface IGameService.
     * En caso de que la tienda no tenga capacidad suficiente, informa con el mensaje correspondiente
     * y no hace el salvado del objeto Player
     *
     * @return objeto generico de tipo ResponseEntity, formado por un objeto de tipo PlayerDto,
     * que contiene el nuevo cuadro creado, junto con enlaces agregados
     */
    @GetMapping("/players/ranking")
    public ResponseEntity<?> averageSuccessRankingAllPlayers() {

        // ranking medio, porcentaje medio de exito de todos los jugadores

        List<Game> allGames = iGameService.allGames();

        Double averageRankingAllPlayers = iUtilities.computeAverageRankingAllPlayers(allGames);
        Ranking ranking = new Ranking();
        ranking.setAverageRankingAllPlayers(averageRankingAllPlayers);
        ranking.setGames(allGames);

        System.out.println("================================");
        System.out.println(averageRankingAllPlayers);
        System.out.println("================================");

        EntityModel<RankingDto> rankingDto = rankingModelAssembler.toModel(ranking);

        return ResponseEntity
                .created(rankingDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(rankingDto);
    }

    /**
     * Representa el mapeo de una peticion HTTP POST, a la URL
     * http://localhost:8081/shops/{id}/pictures
     *
     * Accede a la capa de servicio GameServiceImpl mediante su interface IGameService
     * y hace uso del servicio 'findShopById(shopId)' para recuperar un objeto de tipo Game
     * cuyo id coincida con el shopId pasado en el PathVariable
     *
     * En caso de que no existiese ningun objeto Game con el shopId especificado en el PathVariable,
     * lanza una exception
     *
     * Despues accede a la capa de servicio PlayerServiceImpl mediante su interface
     * IPlayerService y hace uso del servicio 'savePicture(newPlayer)' para salvar el nuevo objeto creado
     *
     * Antes de salvar el nuevo objeto Player, comprueba la capacidad de la tienda,
     * para ello accede al servicio currentShopCapacity(shopId) de la interface IGameService.
     * En caso de que la tienda no tenga capacidad suficiente, informa con el mensaje correspondiente
     * y no hace el salvado del objeto Player
     *
     * @return objeto generico de tipo ResponseEntity, formado por un objeto de tipo PlayerDto,
     * que contiene el nuevo cuadro creado, junto con enlaces agregados
     */
    @GetMapping("/players/ranking/loser")
    public ResponseEntity<?> playerLoser() {

        // jugador con peor porcentaje de exito

        List<Player> allPlayers = iPlayerService.listPlayers();

        Optional<Player> playerLoser = iUtilities.getWorstPlayer(allPlayers);

        System.out.println("================================");
        System.out.println("Ranking:");
        System.out.println(playerLoser.get().getRanking());
        System.out.println("================================");

        EntityModel<PlayerDto> playerDto = playerModelAssembler.toModel(playerLoser.get());

        return ResponseEntity
                .created(playerDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(playerDto);
    }

    /**
     * Representa el mapeo de una peticion HTTP POST, a la URL
     * http://localhost:8081/shops/{id}/pictures
     *
     * Accede a la capa de servicio GameServiceImpl mediante su interface IGameService
     * y hace uso del servicio 'findShopById(shopId)' para recuperar un objeto de tipo Game
     * cuyo id coincida con el shopId pasado en el PathVariable
     *
     * En caso de que no existiese ningun objeto Game con el shopId especificado en el PathVariable,
     * lanza una exception
     *
     * Despues accede a la capa de servicio PlayerServiceImpl mediante su interface
     * IPlayerService y hace uso del servicio 'savePicture(newPlayer)' para salvar el nuevo objeto creado
     *
     * Antes de salvar el nuevo objeto Player, comprueba la capacidad de la tienda,
     * para ello accede al servicio currentShopCapacity(shopId) de la interface IGameService.
     * En caso de que la tienda no tenga capacidad suficiente, informa con el mensaje correspondiente
     * y no hace el salvado del objeto Player
     *
     * @return objeto generico de tipo ResponseEntity, formado por un objeto de tipo PlayerDto,
     * que contiene el nuevo cuadro creado, junto con enlaces agregados
     */
    @GetMapping("/players/ranking/winner")
    public ResponseEntity<?> playerWinner() {

        // jugador con mejor porcentaje de exito
        List<Player> allPlayers = iPlayerService.listPlayers();

        Optional<Player> playerWinner = iUtilities.getBestPlayer(allPlayers);

        System.out.println("================================");
        System.out.println("Ranking:");
        System.out.println(playerWinner.get().getRanking());
        System.out.println("================================");

        EntityModel<PlayerDto> playerDto = playerModelAssembler.toModel(playerWinner.get());

        return ResponseEntity
                .created(playerDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(playerDto);
    }



}
