package eu.arrowhead.client.common.model;

import org.glassfish.jersey.internal.guava.MoreObjects;

public class IOMessage {

    private Integer data;
    private String typeOfMsg;
    private Long requestedTimeStamp;
    private Long responseTimeStamp;

    public IOMessage(){
    }

    public IOMessage(Integer data, String typeOfMsg, Long requestedTimeStamp, Long responseTimeStamp){
        this.requestedTimeStamp = requestedTimeStamp;
        this.responseTimeStamp = responseTimeStamp;
        this.data = data;
        this.typeOfMsg = typeOfMsg;
    }

    public Long getRequestedTimeStamp() {
        return requestedTimeStamp;
    }

    public Long getRespondsTimeStamp() {
        return responseTimeStamp;
    }

    public Integer getData() {
        return data;
    }

    public String getTypeOfMsg() {
        return typeOfMsg;
    }

    public Long getTripTime(){
        if(requestedTimeStamp != null && responseTimeStamp != null){
            return requestedTimeStamp-responseTimeStamp;
        }else{
            return null;
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("data", data).add("typeOfMsg", typeOfMsg).add("requestedTimeStamp", requestedTimeStamp).add("responseTimeStamp", responseTimeStamp).toString();
    }

}
