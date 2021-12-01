import java.util.Date;

public class Record {
    private boolean isRented;
    private final int recordID;
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
}
