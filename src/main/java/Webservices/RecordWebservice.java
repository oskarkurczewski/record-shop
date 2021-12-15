package Webservices;
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
    public Response getRecords() {
        return Response.ok(recordManager.getAllRecords()).build();
    }

    @GET
    @Path("{recordID}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getRecord(@PathParam("recordID") String recordID) {
        try {
            Record recordFound = recordManager.getRecordByID(recordID);
            return Response.ok(recordFound).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewRecord(String body) {
        try {
            JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();

            String title = jsonBody.get("title").getAsString();
            if (!title.matches("^[a-zA-Z0-9_ -]{3,50}$")) {
                throw new InputException("Title must be between 3 and 50 characters");
            }

            String artist = jsonBody.get("artist").getAsString();
            if (!artist.matches("^[a-zA-Z0-9_ -]{3,50}$")) {
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
    @Path("{recordID}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteRecord(@PathParam("recordID") String recordID) {
        try {
            Record record = recordManager.getRecordByID(recordID);
            recordManager.removeRecord(recordID);
            return Response.ok(record).build();
        } catch (RentalException e) {
            return Response.status(400).entity(e).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        }
    }

    @POST
    @Path("{recordID}/edit")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyRecord(@PathParam("recordID") String recordID, String body) {
        try {
            JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();

            if (!recordID.matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b")) {
                throw new InputException("Wrong uuid format");
            }

            Record record = recordManager.getRecordByID(recordID);

            String title = jsonBody.get("title").getAsString();
            if (title.length() != 0 || !title.matches("^[a-zA-Z0-9_ -]{3,50}$")) {
                throw new InputException("Title must be between 3 and 50 characters");
            }


            String artist = jsonBody.get("artist").getAsString();
            if (artist.length() != 0 || !artist.matches("^[a-zA-Z0-9_ -]{3,50}$")) {
                throw new InputException("Artist name must be between 3 and 50 characters");
            }

            String releaseDate = jsonBody.get("releaseDate").getAsString();

            recordManager.modifyRecord(record, title, artist, releaseDate);

            return Response.status(200).entity(record).build();
        } catch (InputException | ParseException e) {
            return Response.status(400).entity(e).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        }
    }
}
