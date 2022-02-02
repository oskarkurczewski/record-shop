package Webservices;

import Model.Exceptions.InputException;
import Model.Exceptions.NotFoundException;
import Model.Exceptions.PermissionException;
import Model.Exceptions.RentalException;
import Model.Managers.RecordManager;
import Model.Managers.RentalManager;
import Model.Managers.UserManager;
import Model.Rental;
import Model.User;
import Model.Record;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.List;



@ApplicationScoped
@Path("users")
public class RentWebservice {

    @Inject
    private UserManager userManager;

    @Inject
    private RecordManager recordManager;

    @Inject
    private RentalManager rentalManager;

    @Inject
    private Principal principal;

    @Inject
    private JsonWebToken jsonWebToken;

    @GET
    @RolesAllowed({"ADMINISTRATOR", "RENTER"})
    @Path("/rentals")
    public List<Rental> getAllRentals() {
        return rentalManager.getAllRentals();
    }

    @GET
    @RolesAllowed({"ADMINISTRATOR", "RENTER"})
    @Path("/archiveRentals")
    public List<Rental> getAllArchiveRentals() {
        return rentalManager.getAllArchiveRentals();
    }

    @GET
    @RolesAllowed({"CLIENT", "RENTER"})
    @Path("/{userID}/cart")
    public List<Record> getCart(@PathParam("userID") String userID) throws NotFoundException {
        User user = userManager.getUserByID(userID);
        return user.getCart();
    }

    @POST
    @RolesAllowed("CLIENT")
    @Path("/{userID}/cart")
    public List<Record> addRentToCart(@PathParam("userID") String userID, String body) throws NotFoundException,
            RentalException
    {
        JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();
        String recordID = jsonBody.get("recordID").getAsString();

        User user = userManager.getUserByID(userID);
        user.addToCart(recordManager.getRecordByID(recordID));
        return user.getCart();
    }

    @DELETE
    @RolesAllowed({"CLIENT", "RENTER"})
    @Path("/{userID}/cart/{recordID}")
    public List<Record> removeRentFromCart(@PathParam("userID") String userID,
                                           @PathParam("recordID") String recordID) throws NotFoundException,
            RentalException
    {
        User user = userManager.getUserByID(userID);
        Record record = recordManager.getRecordByID(recordID);
        user.removeFromCart(record);
        return user.getCart();
    }

    @DELETE
    @RolesAllowed({"ADMINISTRATOR", "CLIENT", "RENTER"})
    @Path("/{userID}/cart")
    public List<Record> removeAllFromCart(@PathParam("userID") String userID) throws NotFoundException,
            RentalException {
        User user = userManager.getUserByID(userID);
        user.clearCart();
        return user.getCart();
    }

    @GET
    @RolesAllowed({"ADMINISTRATOR", "RENTER", "CLIENT"})
    @Path("/{userID}/rentals")
    public List<Rental> getRents(@PathParam("userID") String userID) throws NotFoundException {
        User user = userManager.getUserByID(userID);
        return user.getRentals();
    }

    @GET
    @RolesAllowed({"ADMINISTRATOR", "RENTER", "CLIENT"})
    @Path("/{userID}/rentals/archive")
    public List<Rental> getRentsArchive(@PathParam("userID") String userID) throws NotFoundException {
        User user = userManager.getUserByID(userID);
        return user.getArchiveRentals();
    }

    @POST
    @RolesAllowed({"ADMINISTRATOR", "RENTER"})
    @Path("/{userID}/rentals")
    public List<Rental> submitRentsFromCart(@PathParam("userID") String userID) throws NotFoundException, PermissionException, RentalException, InputException {
        User user = userManager.getUserByID(userID);

        User renter = userManager.getUserByID(jsonWebToken.getSubject());
        List<Rental> newRentals = user.rentCart(renter);
        rentalManager.appendRentals(newRentals);

        return user.getRentals();
    }

    @POST
    @RolesAllowed({"ADMINISTRATOR", "RENTER"})
    @Path("/{userID}/rentals/clear")
    public List<Rental> clearUserRentals(@PathParam("userID") String userID) throws NotFoundException,
            PermissionException,
            RentalException, InputException {

        User user = userManager.getUserByID(userID);
        User renter = userManager.getUserByID(jsonWebToken.getSubject());

        rentalManager.archiveRentals(user.getRentals());
        user.clearRentals(renter);
        return user.getRentals();
    }

    @POST
    @RolesAllowed({"ADMINISTRATOR", "RENTER"})
    @Path("/{userID}/rentals/extend")
    public List<Rental> extendRentReturnDays(@PathParam("userID") String userID,
                                             String body) throws NotFoundException,
            PermissionException,
            RentalException
    {
        JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();
        User user = userManager.getUserByID(userID);
        int days = Integer.parseInt(jsonBody.get("days").toString());

        userManager.extendRentReturnDays(jsonWebToken.getSubject(), userID, days);
        return user.getRentals();
    }
}
