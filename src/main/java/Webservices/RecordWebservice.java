package Webservices;
import Model.Exceptions.InputException;
import Model.Exceptions.NotFoundException;
import Model.Exceptions.RentalException;
import Model.Managers.RecordManager;
import Model.Record;
import Model.Rental;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import utils.EntitySigner;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.List;


@ApplicationScoped
@Path("records")
public class RecordWebservice {

    @Inject
    private RecordManager recordManager;

    @GET
    @RolesAllowed({"CLIENT", "ADMINISTRATOR", "RENTER"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getRecords() {
        return Response.ok(recordManager.getAllRecords()).build();
    }

    @GET
    @RolesAllowed({"CLIENT", "ADMINISTRATOR", "RENTER"})
    @Path("{recordID}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getRecord(@PathParam("recordID") String recordID) {
        try {
            Record recordFound = recordManager.getRecordByID(recordID);
            return Response.ok(recordFound).header("Etag", EntitySigner.calculateSignature(recordFound)).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        }
    }

    @GET
    @RolesAllowed({"ADMINISTRATOR", "RENTER"})
    @Path("/{recordID}/currentRent")
    public Rental getCurrentRent(@PathParam("recordID") String recordID) throws NotFoundException {
        Record record = recordManager.getRecordByID(recordID);
        return record.getCurrentRent();
    }

    @GET
    @RolesAllowed({"ADMINISTRATOR", "RENTER"})
    @Path("/{recordID}/archiveRents")
    public List<Rental> getArchiveRents(@PathParam("recordID") String recordID) throws NotFoundException {
        Record record = recordManager.getRecordByID(recordID);
        return record.getArchiveRents();
    }

    @POST
    @RolesAllowed("ADMINISTRATOR")
    @Produces(MediaType.APPLICATION_JSON)
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
    @RolesAllowed("ADMINISTRATOR")
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
    @RolesAllowed("ADMINISTRATOR")
    @Path("{recordID}/edit")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyRecord(@PathParam("recordID") String recordID, String body, @HeaderParam("If-Match") @NotNull String etag) {
        System.out.println("SRAKEN PIERDAKEN");
        try {
            JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();

            if (!recordID.matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b")) {
                System.out.println("Wrong uuid format");
                throw new InputException("Wrong uuid format");
            }

            Record record = recordManager.getRecordByID(recordID);

            String title = jsonBody.get("title").getAsString();
            if (title.isEmpty() || !title.matches("^[a-zA-Z0-9_ -]{3,50}$")) {
                System.out.println("Title must be between 3 and 50 characters");
                throw new InputException("Title must be between 3 and 50 characters");
            }


            String artist = jsonBody.get("artist").getAsString();
            if (artist.isEmpty() || !artist.matches("^[a-zA-Z0-9_ -]{3,50}$")) {
                System.out.println("Artist name must be between 3 and 50 characters");
                throw new InputException("Artist name must be between 3 and 50 characters");
            }

            String releaseDate = jsonBody.get("releaseDate").getAsString();

            recordManager.modifyRecord(record, title, artist, releaseDate, etag);

            return Response.status(200).entity(record).build();
        } catch (InputException | ParseException e) {
            return Response.status(400).entity(e).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        }
    }
}
