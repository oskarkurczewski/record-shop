package Webservices;
import Model.Exceptions.BasicException;
import Model.Exceptions.InputException;
import Model.Exceptions.NotFoundException;
import Model.Exceptions.RentalException;
import Model.Managers.RecordManager;
import Model.Record;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;


@ApplicationScoped
@Path("records")
public class RecordWebservice {

    @Inject
    private RecordManager recordManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getRecord(@QueryParam("recordid") String recordid) {
        if (recordid != null) {
            try {
                Record recordFound = recordManager.getRecordByID(recordid);
                return Response.ok(recordFound).build();
            } catch (NotFoundException e) {
                return Response.status(404).entity(e).build();
            }
        } else {
            return Response.ok(recordManager.getAllRecords()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewRecord(String body) {
        try {
            JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();

            String title = jsonBody.get("title").getAsString();
            if (title.matches("^[a-z0-9_-]{3,50}$")) {
                throw new InputException("Title must be between 3 and 50 characters");
            }

            String artist = jsonBody.get("artist").getAsString();
            if (artist.matches("^[a-z0-9_-]{3,50}$")) {
                throw new InputException("Artist name must be between 3 and 50 characters");
            }

            String releaseDate = jsonBody.get("releaseDate").getAsString();

            Record record = new Record(title, artist, releaseDate);

            recordManager.appendRecord(record);
            return Response.status(201).entity(record).build();
        } catch (InputException e) {
            return Response.status(400).entity(e).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteRecord(@QueryParam("recordid") String recordid) {
        try {
            Record record = recordManager.getRecordByID(recordid);
            recordManager.removeRecord(recordid);
            return Response.ok(record).build();
        } catch (RentalException e) {
            return Response.status(400).entity(e).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        }
    }

    @POST
    @Path("modify")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyRecord(String body) {
        try {
            JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();

            String recordid = jsonBody.get("recordid").getAsString();
            if (!recordid.matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b")) {
                throw new InputException("Wrong uuid format");
            }

            Record record = recordManager.getRecordByID(recordid);

            String title = jsonBody.get("title").getAsString();
            if (title.length() != 0) {
                if (!title.matches("^[a-z0-9_-]{3,50}$")) {
                    throw new InputException("Title must be between 3 and 50 characters");
                }

                record.setTitle(title);
            }

            String artist = jsonBody.get("artist").getAsString();
            if (artist.length() != 0) {
                if (!artist.matches("^[a-z0-9_-]{3,50}$")) {
                    throw new InputException("Artist name must be between 3 and 50 characters");
                }

                record.setArtist(artist);
            }

            String releaseDate = jsonBody.get("releaseDate").getAsString();
            if (releaseDate.length() != 0) {
                record.setReleaseDate(releaseDate);
            }

            recordManager.appendRecord(record);

            return Response.status(200).entity(record).build();
        } catch (InputException | ParseException e) {
            return Response.status(400).entity(e).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        }
    }
}
