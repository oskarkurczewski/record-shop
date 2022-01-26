package Webservices;

import Model.Exceptions.BasicException;
import Model.Exceptions.InputException;
import Model.Exceptions.NotFoundException;
import Model.Managers.UserManager;
import Model.User;
import Model.UserType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("users")
public class UserWebservice {

    @Inject
    private UserManager userManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@QueryParam("login") String login){
        if (login != null) {
            return Response.ok(userManager.getUsersByLogin(login)).build();
        } else {
            return Response.ok(userManager.getAllUsers()).build();
        }
    }

    @GET
    @Path("{userID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userID") String userID) {
        try {
            User user = userManager.getUserByID(userID);
            return Response.ok(user).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        }
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(String body) {
        try {
            JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();
            String login = jsonBody.get("login").getAsString();
            String password = jsonBody.get("password").getAsString();
            if (!login.matches("^[a-zA-Z0-9_-]{8,16}$")) {
                throw new InputException("Login must be between 8 and 16 characters");
            }

            UserType type = UserType.valueOf(
                    jsonBody.get("type").getAsString()
            );

            User user = new User(login, password, type);
            userManager.appendUser(user);

            return Response.status(200).entity(user).build();
        } catch (NullPointerException | InputException | IllegalArgumentException | IllegalStateException e) {
            return Response.status(400).entity(e).build();
        }
    }

    @DELETE
    @Path("{userID}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("userID") String userID){
        try {
            User user = userManager.getUserByID(userID);
            userManager.removeUser(userID);
            return Response.ok(user).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        } catch (BasicException e) {
            return Response.status(400).entity(e).build();
        }
    }

    @POST
    @Path("{userID}/changeLogin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeUserLogin(@PathParam("userID") String userID, String body){
        try {
            JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();

            String login = jsonBody.get("login").getAsString();
            if (!login.matches("^[a-zA-Z0-9_-]{8,16}$")) {
                throw new InputException("Login must be between 8 and 16 characters");
            }

            User user = userManager.getUserByID(userID);
            userManager.setUserLogin(userID, login);

            return Response.ok(user).build();
        } catch (InputException e) {
            return Response.status(400).entity(e).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        }
    }

    @POST
    @Path("{userID}/activate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateUser(@PathParam("userID") String userID){
        try {
            User user = userManager.getUserByID(userID);
            user.activate();
            return Response.ok(user).build();
        } catch (NotFoundException e){
            return Response.status(404).entity(e).build();
        } catch (InputException e) {
            return Response.status(400).entity(e).build();
        }
    }

    @POST
    @Path("{userID}/deactivate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivateUser(@PathParam("userID") String userID){
        try {
            User user = userManager.getUserByID(userID);
            user.deactivate();
            return Response.ok(user).build();
        } catch(NotFoundException e){
            return Response.status(404).entity(e).build();
        } catch (InputException e) {
            return Response.status(400).entity(e).build();
        }

    }
}
