package Webservices;

import Model.Exceptions.*;
import Model.Exceptions.NotFoundException;
import Model.User;
import Model.Record;
import Model.Managers.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RentWebservice {
    @Inject
    RecordManager recordManager;

    @Inject
    UserManager userManager;

    @DELETE
    @Path("/cart")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeRentFromCart(String body) {

        JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();
        try {
            User user = userManager.getUserByID(jsonBody.get("userid").getAsString());
            user.removeFromCart(recordManager.getRecordByID(jsonBody.get("recordid").getAsString()));
            return Response.ok(user.getCart()).build();
        } catch (NotFoundException e) {
            return Response.status(404, e.toString()).build();
        } catch (RentalException e) {
            return Response.status(400, e.toString()).build();
        }
    }
}
