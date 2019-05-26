package cn.edu.nju.vivohackathon.ui;

public class GameInfo {

    private String title, description, imageBase64;

    public GameInfo(String title, String description, String imageBase64) {
        this.title = title;
        this.description = description;
        this.imageBase64 = imageBase64;
    }

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
}
