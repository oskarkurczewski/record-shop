package Webservices;
import Model.Exceptions.BasicException;
import Model.Exceptions.NotFoundException;
import Model.Exceptions.RentalException;
import Model.Managers.RecordManager;
import Model.Record;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@ApplicationScoped
@Path("records")
public class RecordWebservice {

    @Inject
    private RecordManager recordManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRecords() {
        return Response.ok(recordManager.getAllRecords()).build();
    }

    @GET
    @Path("id={recordid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getRecord(@PathParam("recordid") String recordid) {
        try {
            Record recordFound = recordManager.getRecordByID(recordid);
            return Response.ok(recordFound).build();
        } catch (NotFoundException e) {
            return Response.status(404, e.toString()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewRecord(Record record) {
        try {
            recordManager.appendRecord(record);
            return Response.ok(record).build();
        } catch (BasicException e) {
            return Response.status(400, e.toString()).build();
        }
    }

    @DELETE
    @Path("id={recordid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteRecord(@PathParam("recordid") String recordid) {
        try {
            Record record = recordManager.getRecordByID(recordid);
            recordManager.removeRecord(recordid);
            return Response.ok(record).build();
        } catch (RentalException e) {
            return Response.status(400, e.toString()).build();
        } catch (NotFoundException e) {
            return Response.status(404, e.toString()).build();
        }
    }

    @POST
    @Path("id={recordid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyRecord(@PathParam("recordid") String recordid, Record record) {
        try {
            record.setRecordID(recordid);
            recordManager.modifyRecord(record);

            Record recordFound = recordManager.getRecordByID(recordid);
            return Response.ok(recordFound).build();
        } catch (NotFoundException e) {
            return Response.status(404, e.toString()).build();
        } catch (BasicException e) {
            return Response.status(400, e.toString()).build();
        }
    }

}
