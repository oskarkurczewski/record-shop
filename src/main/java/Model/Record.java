package Model;

import Model.Exceptions.InputException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

public class Record {
    private UUID recordID;
    private Rental currentRent;
    private final List<Rental> archiveRents = new ArrayList<>();
    private boolean isRented;

    private String title;
    private String artist;
    private LocalDate releaseDate;

    public Record(String title, String artist, String releaseDate) {
        this.isRented = false;
        this.recordID = UUID.randomUUID();
        this.title = title;
        this.artist = artist;
        this.releaseDate = LocalDate.parse(releaseDate);
    }

    public boolean isRented() {
        return isRented;
    }

    public UUID getRecordID() {
        return recordID;
    }

    public List<Rental> getArchiveRents() {
        return archiveRents;
    }

    public Rental getCurrentRent() {
        return currentRent;
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

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void rent(Rental rental) throws InputException {
        if (this.isRented) {
            throw new InputException("Record already rented");
        }
        this.isRented = true;
        this.currentRent = rental;
    }

    public void release() throws InputException {
        if (!this.isRented) {
            throw new InputException("This record is not rented");
        }
        this.archiveRents.add(this.currentRent);
        this.isRented = false;
        this.currentRent = null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setReleaseDate(String releaseDate) throws ParseException {
        this.releaseDate = LocalDate.parse(releaseDate);
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
