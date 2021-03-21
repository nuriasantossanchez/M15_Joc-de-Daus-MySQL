package jocDeDaus.dto;

import jocDeDaus.entity.Game;
import jocDeDaus.entity.Player;

public class CrapsRollDto {

    private Long idCrapsRoll;
    private Long idPlayer;
    private Short crapOne;
    private Short crapTwo;
    private Short rollResult;

    public CrapsRollDto() {
    }

    public Long getIdCrapsRoll() {
        return idCrapsRoll;
    }

    public void setIdCrapsRoll(Long idCrapsRoll) {
        this.idCrapsRoll = idCrapsRoll;
    }

    public Long getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(Long idPlayer) {
        this.idPlayer = idPlayer;
    }

    public Short getCrapOne() {
        return crapOne;
    }

    public void setCrapOne(Short crapOne) {
        this.crapOne = crapOne;
    }

    public Short getCrapTwo() {
        return crapTwo;
    }

    public void setCrapTwo(Short crapTwo) {
        this.crapTwo = crapTwo;
    }

    public Short getRollResult() {
        return rollResult;
    }

    public void setRollResult(Short rollResult) {
        this.rollResult = rollResult;
    }

    @Override
    public String toString() {
        return "CrapsRollDto {" +
                "idCrapsRoll=" + idCrapsRoll +
                ", idPlayer=" + idPlayer +
                ", crapOne=" + crapOne +
                ", crapTwo=" + crapTwo +
                ", rollResult=" + rollResult +
                '}';
    }
}
