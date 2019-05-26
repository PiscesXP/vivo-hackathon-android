package cn.edu.nju.vivohackathon.ui.discover;

public class GameInfo {

    private String title, description, imageBase64;
    private int gameID;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public GameInfo(String title, String description, String imageBase64, int gameID) {
        this.title = title;
        this.description = description;
        this.imageBase64 = imageBase64;
        this.gameID = gameID;
    }
}
