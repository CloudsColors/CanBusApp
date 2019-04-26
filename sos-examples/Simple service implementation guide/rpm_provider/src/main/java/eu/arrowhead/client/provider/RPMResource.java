package eu.arrowhead.client.provider;

import Main.CanBusApp;
import Main.RequestCan;
import eu.arrowhead.client.common.model.IOMessage;
import eu.arrowhead.client.common.model.RPMInput;
import eu.arrowhead.client.common.model.RPMOutput;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("controller")
public class RPMResource {

  private RequestCan rc;

  public RPMResource(){
    try {
      rc = new RequestCan("73-30130-00441-2", "10043");
    }catch(Exception e){
      System.out.println(e);
    }
  }

  @POST
  @Path("rpm")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRPM(@Valid IOMessage input, @Context SecurityContext context, @QueryParam("token") String token,
                         @QueryParam("signature") String signature) {
    if (context.isSecure()) {
      RequestVerification.verifyRequester(context, token, signature);
    }
    int result = 0;
    try {
      result = rc.getRequested(input.getTypeOfMsg());
    }catch(Exception e){
      System.out.println(e);
    }
    System.out.println("Result: "+result);
    return Response.status(200).entity(new IOMessage(result, input.getTypeOfMsg(), input.getRequestedTimeStamp(), System.currentTimeMillis())).build();
  }

  @GET
  @Path("enginecooltemp")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCoolantTemp(){
    return Response.status(200).entity(new IOMessage(1337, "enginecooltemp", System.currentTimeMillis(), System.currentTimeMillis())).build();
  }

}
