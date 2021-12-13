package Webservices;

import Model.Exceptions.*;
import Model.Exceptions.NotFoundException;
import Model.User;
import Model.Record;
import Model.Managers.*;

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
    @Path("/cart/userid={userid},recordid={recordid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeRentFromCart(@PathParam("userid") String userid, @PathParam("recordid") String recordid) throws NotFoundException {
        try {
            User user = userManager.getUserByID(userid);
            Record record = recordManager.getRecordByID(recordid);
            user.removeFromCart(record);
            return Response.ok(user.getCart()).build();
        } catch (NotFoundException e) {
            return Response.status(404, e.toString()).build();
        } catch (RentalException e) {
            return Response.status(400, e.toString()).build();
        }
    }
}
