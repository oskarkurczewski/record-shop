package Model;

import Model.Exceptions.BasicException;
import Model.Exceptions.InputException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Record {
    private UUID recordID;
    private boolean isRented;
    private String title;
    private String artist;
    private Date releaseDate;

    public Record() {
        this.recordID = UUID.randomUUID();
    }

    public Record(String title, String artist, String releaseDate) throws InputException {
        this.isRented = false;
        this.recordID = UUID.randomUUID();
        this.title = title;
        this.artist = artist;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            this.releaseDate = formatter.parse(releaseDate);
        } catch (ParseException e) {
            throw new InputException("Cannot parse date");
        }

    }

    public boolean isRented() {
        return isRented;
    }

    public UUID getRecordID() {
        return recordID;
    }

    public void setRecordID(String id) {
        this.recordID = UUID.fromString(id);
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

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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

    public void setReleaseDate(String releaseDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        this.releaseDate = formatter.parse(releaseDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        return new EqualsBuilder()
                .append(recordID, record.recordID)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(recordID)
                .toHashCode();
    }
}
