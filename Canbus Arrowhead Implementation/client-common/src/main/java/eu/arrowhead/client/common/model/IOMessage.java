package eu.arrowhead.client.common.model;

import org.glassfish.jersey.internal.guava.MoreObjects;

public class IOMessage {

    private Integer data;
    private String sensorType;
    private Integer status;
    private Long responseTimeStamp;

    public IOMessage(){
    }

    public IOMessage(Integer data, String sensorType, Integer status, Long responseTimeStamp){
        this.status = status;
        this.responseTimeStamp = responseTimeStamp;
        this.data = data;
        this.sensorType = sensorType;
    }

    public Integer getStatus() {
        return status;
    }

    public Long getRespondsTimeStamp() {
        return responseTimeStamp;
    }

    public Integer getData() {
        return data;
    }

    public String getSensorType() {
        return sensorType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("data", data).add("sensorType", sensorType).add("status", status).add("responseTimeStamp", responseTimeStamp).toString();
    }

}
