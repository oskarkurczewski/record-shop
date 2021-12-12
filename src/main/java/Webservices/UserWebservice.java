package Webservices;

import Model.Exceptions.BasicException;
import Model.Exceptions.NotFoundException;
import Model.Managers.RecordManager;
import Model.Managers.UserManager;
import Model.User;

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
    public Response getUsers(){
        return Response.ok(userManager.getAllUsers()).build();
    }

    @GET
    @Path("login={login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByLogin(@PathParam("login") String login){
        return Response.ok(userManager.getUserByLogin(login)).build();
    }

    @GET
    @Path("id={userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("userid") String userid){
        try {
            User user = userManager.getUserByID(userid);
            return Response.ok(user).build();
        } catch (NotFoundException e) {
            return Response.status(404, e.toString()).build();
        }
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user){
        try {
            userManager.appendUser(user);
        } catch (BasicException e) {
            return Response.status(400, e.toString()).build();
        }
        return Response.ok(user).build();
    }

    @DELETE
    @Path("id={userid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("userid") String userid){
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
    @Path("id={userid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeUserLogin(User user){
        try {
            userManager.setUserLogin(user.getUserID().toString(), user.getLogin());
        } catch (BasicException e) {
            return Response.status(400, e.toString()).build();
        }
        return Response.ok(user).build();
    }

    @POST
    @Path("id={userid}/activate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateUser(@PathParam("userid") String userid){
        try {
            User user = userManager.getUserByID(userid);
            user.activate();
            return Response.ok(user).build();
        } catch (NotFoundException e){
            return Response.status(404, e.toString()).build();
        } catch (BasicException e) {
            return Response.status(400, e.toString()).build();
        }
    }

    @POST
    @Path("id={userid}/deactivate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivateUser(@PathParam("userid") String userid){
        try {
            User user = userManager.getUserByID(userid);
            user.deactivate();
            return Response.ok(user).build();
        } catch(NotFoundException e){
            return Response.status(404, e.toString()).build();
        } catch (BasicException e) {
            return Response.status(400, e.toString()).build();
        }

    }
}
