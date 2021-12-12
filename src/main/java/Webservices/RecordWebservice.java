package Webservices;
import Model.Managers.RecordManager;
import Model.Managers.UserManager;
import Model.Record;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@ApplicationScoped
@Path("records")
public class RecordWebservice {

    @Inject
    private UserManager userManager;

    @Inject
    private RecordManager recordManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRecords() {
        return Response.ok(recordManager).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewRecord(Record record) {
        return Response.ok(record).build();
    }

}
