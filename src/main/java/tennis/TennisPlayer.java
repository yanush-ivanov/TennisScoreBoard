package tennis;

public class TennisPlayer {
    public String playerName = "Player 1";
    private Integer numberOfPointsWon = 0;
    private Integer numberOfGamesWon = 0;
    private Integer numberOfSetsWon = 0;

    public void setNumberOfPointsWon(Integer numberOfPointsWon) {
        this.numberOfPointsWon = numberOfPointsWon;
    }

    public void setNumberOfGamesWon(Integer numberOfGamesWon) {
        this.numberOfGamesWon = numberOfGamesWon;
    }

    public Integer getPointsWon() {
        return numberOfPointsWon;
    }

    public Integer getNumberOfGamesWon() {
        return numberOfGamesWon;
    }

    public Integer getNumberOfSetsWon() {
        return numberOfSetsWon;
    }

    public void AddPointWon() {
        this.numberOfPointsWon++;
    }

    public void AddGameWon() {
        this.numberOfGamesWon++;
    }

    public void AddSetWon() {
        this.numberOfSetsWon++;
    }
}
