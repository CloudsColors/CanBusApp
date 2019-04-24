package Main;

import CanWrapper.CanlibException;
import CanWrapper.Message;

public class RequestCan {

    private CanBusApp canBusApp;

    public RequestCan(String ean, String sn){
        try {
            canBusApp = new CanBusApp(ean, sn);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public int getRequested(String request) throws CanlibException {
        switch(request){
            case("rpm"):{
                Message msg = canBusApp.getFromCan(CAN_CODES.ENGINE_RPM);
                if(msg == null){
                    return -2;
                }
                return getRpm(msg.data[3], msg.data[4]);
            }
            case("throttleposition"):{
                Message msg = canBusApp.getFromCan(CAN_CODES.THROTTLE_POSITION);
                if(msg == null){
                    return -2;
                }
                return getThrottlePosition(msg.data[3]);
            }
            default:{
                return -3;
            }
        }
    }

    private int getRpm(byte dataA, byte dataB){
        int RPM = (256*dataA+dataB)/4; // Formula to compute RPM from byte data.
        return RPM;
    }

    private int getThrottlePosition(byte dataA){
        int Throttle_position = (100/255)*dataA; // Formula to compute throttle position from byte data.
        return Throttle_position;
    }

}
