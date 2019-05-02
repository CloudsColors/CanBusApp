package eu.arrowhead.client.provider;

import CanWrapper.Message;
import Main.CanBusApp;
import Main.FormulaCollection;
import Main.ReadServiceJson;
import eu.arrowhead.client.common.model.IOMessage;

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

  private FormulaCollection formula;
  private CanBusApp canBus;

  public RPMResource(){
    try {
      formula = new FormulaCollection();
      canBus = new CanBusApp("73-30130-00441-2", "10043");
    }catch(Exception e){
      System.out.println(e);
    }
  }

  @GET
  @Path("rpm")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRPM(@Context SecurityContext context, @QueryParam("token") String token, @QueryParam("signature") String signature) {
    if (context.isSecure()) {
      RequestVerification.verifyRequester(context, token, signature);
    }
    Message msg = null;
    int data = -1;
    try {
      msg = canBus.getFromCan(ReadServiceJson.ENGINE_RPM);
    }catch(Exception e){
      System.out.println("Failure");
    }
    if(msg != null) {
      data = formula.getRpm(msg.data[3], msg.data[4]);
      System.out.println("RPM: " + data);
    }
    return Response.status(200).entity(new IOMessage(data, "rpm", 0L, System.currentTimeMillis())).build();
  }

  @GET
  @Path("enginecooltemp")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCoolantTemp(@Context SecurityContext context, @QueryParam("token") String token, @QueryParam("signature") String signature){
    if (context.isSecure()) {
      RequestVerification.verifyRequester(context, token, signature);
    }
    Message msg = null;
    int data = -1;
    try {
      msg = canBus.getFromCan(ReadServiceJson.ENGINE_COOL_TEMP);
    }catch(Exception e){
      System.out.println("Failure");
    }
    if(msg != null){
      data = formula.getEngineCoolantTemp(msg.data[3]);
      System.out.println("ECT: "+ data);
    }
    return Response.status(200).entity(new IOMessage(data, "enginecooltemp", 0L, System.currentTimeMillis())).build();
  }

}
