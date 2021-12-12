package Webservices;

import Model.Exceptions.BasicException;
import Model.Managers.RecordManager;
import Model.Managers.UserManager;
import Model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("users")
public class UserWebservice {

    @Inject
    private UserManager userManager;

    @Inject
    private RecordManager recordManager;

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
        return Response.ok(userManager.getUserByID(userid)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user){
        try {
            userManager.appendUser(user);
        } catch (BasicException e) {
            return Response.status(501, e.toString()).build();
        }

        return Response.ok(user).build();
    }
}
