package Webservices;

import Model.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("user")
public class UserWebservice {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(){
        User u = new User(1, "peja997");
        u.setActive(true);
        return u;
    }
}
