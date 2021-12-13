package Webservices;

import Model.Exceptions.InputException;
import Model.Exceptions.NotFoundException;
import Model.Exceptions.PermissionException;
import Model.Exceptions.RentalException;
import Model.Managers.RecordManager;
import Model.Managers.UserManager;
import Model.User;
import Model.Record;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("users")
public class RentWebservice {

    @Inject
    private UserManager userManager;

    @Inject
    private RecordManager recordManager;

    @GET
    @Path("{userid}/cart")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCart(@PathParam("userid") String userid) {
        try {
            User user = userManager.getUserByID(userid);
            return Response.ok(user.getCart()).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        }
    }

    @POST
    @Path("/cart")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRentToCart(String body) {

        JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();
        try {
            User user = userManager.getUserByID(jsonBody.get("userid").getAsString());
            user.addToCart(recordManager.getRecordByID(jsonBody.get("recordid").getAsString()));
            return Response.ok(user.getCart()).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        } catch (RentalException e) {
            return Response.status(400).entity(e).build();
        }
    }

    @DELETE
    @Path("{userid}/cart/{recordid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeRentFromCart(@PathParam("userid") String userid, @PathParam("recordid") String recordid) {
        try {
            User user = userManager.getUserByID(userid);
            Record record = recordManager.getRecordByID(recordid);
            user.removeFromCart(record);
            return Response.ok(user.getCart()).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        } catch (RentalException e) {
            return Response.status(400).entity(e).build();
        }
    }

    @DELETE
    @Path("{userid}/cart")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeRentFromCart(@PathParam("userid") String userid)  {
        try {
            User user = userManager.getUserByID(userid);
            user.clearCart();
            return Response.ok(user.getCart()).build();
        } catch (NotFoundException | RentalException e) {
            return Response.status(404).entity(e).build();
        }
    }

    @POST
    @Path("/rent")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response rentFromCart(String body) {

        JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();
        try {
            User user = userManager.getUserByID(jsonBody.get("userid").getAsString());
            User renter = userManager.getUserByID(jsonBody.get("renterid").getAsString());
            user.rentCart(renter);
            return Response.ok(user.getRentals()).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        } catch (PermissionException e) {
            return Response.status(403).entity(e).build();
        } catch (InputException e) {
            return Response.status(400).entity(e).build();
        } catch (RentalException e) {
            return Response.status(400).entity(e).build();
        }
    }

    @POST
    @Path("/rent/clear")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response clearUserRentals(String body) {

        JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();
        try {
            User user = userManager.getUserByID(jsonBody.get("userid").getAsString());
            User renter = userManager.getUserByID(jsonBody.get("renterid").getAsString());
            user.clearRentals(renter);
            return Response.ok(user.getRentals()).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        } catch (PermissionException | RentalException e) {
            return Response.status(403).entity(e).build();
        }
    }

    @POST
    @Path("/rent/extend")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response extendRentReturnDays(String body) {

        JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();
        try {
            User user = userManager.getUserByID(jsonBody.get("userid").getAsString());
            String userid = jsonBody.get("userid").getAsString();
            String renterid = jsonBody.get("renterid").getAsString();
            int days = Integer.parseInt(jsonBody.get("days").toString());
            userManager.extendRentReturnDays(renterid, userid, days);
            return Response.ok(user.getRentals()).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        } catch (PermissionException | RentalException e) {
            return Response.status(403).entity(e).build();
        }
    }
}
