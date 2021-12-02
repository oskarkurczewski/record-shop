import java.util.Date;

public class Record {
    private final int recordID;
    private boolean isRented;
    private String title;
    private String artist;
    private Date releaseDate;

    public Record(int recordID, String title, String artist, Date releaseDate) {
        this.isRented = false;
        this.recordID = recordID;
        this.title = title;
        this.artist = artist;
        this.releaseDate = releaseDate;
    }

    public boolean isRented() {
        return isRented;
    }

    public int getRecordID() {
        return recordID;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
