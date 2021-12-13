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

@ApplicationScoped
@Path("users")
public class UserWebservice {

    @Inject
    private UserManager userManager;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByLogin(@QueryParam("login") String login, @QueryParam("userid") String userid){
        if (userid != null) {
            try {
                User user = userManager.getUserByID(userid);
                return Response.ok(user).build();
            } catch (NotFoundException e) {
                return Response.status(404, e.toString()).build();
            }
        } else if (login != null) {
            return Response.ok(userManager.getUserByLogin(login)).build();
        } else {
            return Response.ok(userManager.getAllUsers()).build();
        }
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(String body) {
            try {
                JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();
                String login = jsonBody.get("login").getAsString();
                if (login.matches("^[a-z0-9_-]{8,16}$")) {
                    throw new InputException("Login must be between 8 and 16 characters");
                }

                UserType type = UserType.valueOf(
                        jsonBody.get("type").getAsString()
                );

                User user = new User(login, type);
                userManager.appendUser(user);

                return Response.status(200).entity(user).build();
            } catch (NullPointerException | InputException | IllegalArgumentException | IllegalStateException e) {
                return Response.status(400).entity(e).build();
            }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(@QueryParam("userid") String userid){
        try {
            User user = userManager.getUserByID(userid);
            userManager.removeUser(userid);
            return Response.ok(user).build();
        } catch (NotFoundException e) {
            return Response.status(404, e.toString()).build();
        } catch (BasicException e) {
            return Response.status(400, e.toString()).build();
        }
    }

    @POST
    @Path("changeLogin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeUserLogin(@QueryParam("userid") String userid, String body){
        try {
            JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();

            String login = jsonBody.get("login").getAsString();
            if (login.matches("^[a-z0-9_-]{8,16}$")) {
                throw new InputException("Login must be between 8 and 16 characters");
            }

            User user = userManager.getUserByID(userid);
            userManager.setUserLogin(userid, login);

            return Response.ok(user).build();
        } catch (NotFoundException | InputException e) {
            return Response.status(400, e.toString()).build();
        }
    }

    @POST
    @Path("activate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateUser(@QueryParam("userid") String userid){
        try {
            User user = userManager.getUserByID(userid);
            user.activate();
            return Response.ok(user).build();
        } catch (NotFoundException e){
            return Response.status(404, e.toString()).build();
        } catch (InputException e) {
            return Response.status(400, e.toString()).build();
        }
    }

    @POST
    @Path("deactivate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivateUser(@QueryParam("userid") String userid){
        try {
            User user = userManager.getUserByID(userid);
            user.deactivate();
            return Response.ok(user).build();
        } catch(NotFoundException e){
            return Response.status(404, e.toString()).build();
        } catch (InputException e) {
            return Response.status(400, e.toString()).build();
        }

    }
}
