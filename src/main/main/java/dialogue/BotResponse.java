package dialogue;

public class BotResponse {
    private final String text;
    private final String photoUrl;

    public BotResponse(String text) {
        this.text = text;
        this.photoUrl = null;
    }

    public BotResponse(String text, String photoUrl) {
        this.text = text;
        this.photoUrl = photoUrl;
    }

    public String getText() {
        return text;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public boolean hasPhoto() {
        return photoUrl != null && !photoUrl.isBlank();
    }
}
