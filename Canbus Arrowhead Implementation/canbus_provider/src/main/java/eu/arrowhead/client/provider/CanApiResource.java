package eu.arrowhead.client.provider;

import CanWrapper.Message;
import Main.CanBusApp;
import Main.FormulaCollection;
import Main.ReadPIDCodes;
import eu.arrowhead.client.common.model.IOMessage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * CanApiResource is the Api resource which will handle the paths and handle the responses.
 */
@Path("controller")
public class CanApiResource {

  private FormulaCollection formula;
  private CanBusApp canBus;
  private ReadPIDCodes pids;

  /**
   * Construct that makes the formula class which will be used for calculating the response from the CAN bus and makes the
   * Can bus application to read from the can bus.
   */
  public CanApiResource(){
    try {
      formula = new FormulaCollection();
      canBus = new CanBusApp("73-30130-00441-2", "10043");
      pids = new ReadPIDCodes();
    }catch(Exception e){
      System.out.println(e);
    }
  }

  /**
   * Function that repsonse to the url: address:port/controller/rpm, will return the engines RPM.
   * @param context
   * @param token
   * @param signature
   * @return
   */
  @GET
  @Path("enginerpm")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRPM(@Context SecurityContext context, @QueryParam("token") String token, @QueryParam("signature") String signature) {
    if (context.isSecure()) {
      RequestVerification.verifyRequester(context, token, signature);
    }
    Message msg = null;
    int data = -1;
    try {
      msg = canBus.getFromCan(pids.getPIDCode("enginerpm"));
    }catch(Exception e){
      System.out.println("Failure");
    }
    if(msg != null) {
      data = formula.getRpm(msg.data[3], msg.data[4]);
      System.out.println("RPM: " + data);
      return Response.status(200).entity(new IOMessage(data, "rpm", 0, System.currentTimeMillis())).build();
    }else{
      return Response.status(200).entity(new IOMessage(data, "rpm", -1, System.currentTimeMillis())).build();
    }

  }

  /**
   * Function that response to the url: address:port/controller/enginecoolanttemp, will return the temperature of the engines coolant.
   * @param context
   * @param token
   * @param signature
   * @return
   */
  @GET
  @Path("enginecoolanttemp")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCoolantTemp(@Context SecurityContext context, @QueryParam("token") String token, @QueryParam("signature") String signature){
    if (context.isSecure()) {
      RequestVerification.verifyRequester(context, token, signature);
    }
    Message msg = null;
    int data = -1;
    try {
      msg = canBus.getFromCan(pids.getPIDCode("enginecooltemp"));
    }catch(Exception e){
      System.out.println("Failure");
    }
    if(msg != null){
      data = formula.getEngineCoolantTemp(msg.data[3]);
      System.out.println("ECT: "+ data);
      return Response.status(200).entity(new IOMessage(data, "enginecooltemp", 0, System.currentTimeMillis())).build();
    }else{
      return Response.status(200).entity(new IOMessage(data, "enginecooltemp", -1, System.currentTimeMillis())).build();
    }

  }


}
