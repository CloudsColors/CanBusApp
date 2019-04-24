package Main;

import Main.WriteToFile;
import CanWrapper.Canlib;
import CanWrapper.CanlibException;
import CanWrapper.Handle;
import CanWrapper.Message;
import CanWrapper.NoMessageException;

public class CanBusApp {

    private Handle handle;

    //ean = "73-30130-00861-8", sn = "10540"

    public CanBusApp(String ean, String sn) throws CanlibException{
        // A handle either needs a channel, or a EAN togheter with a Serial.no.
        handle = new Handle(ean, sn); // PCI kvaser card.
        // Sets the parameter for the can bus.
        handle.setBusParams(Canlib.canBITRATE_500K, 0, 0, 0, 0, 0);
    }

    public Message getFromCan(int requestedPID) throws CanlibException{
        // The handle goes on the bus
        handle.busOn();
        //send a request for ENGINE_RPM
        handle.write(new Message(CAN_CODES.REQUEST_MSG, new byte[]{0x2, CAN_CODES.MODE_SCD, (byte) requestedPID, 0x55, 0x55, 0x55, 0x55}, 8, 0));
        // Wait for a message at most 1s.
        if(handle.hasMessage(1000)){
            // if there's a message, read the message.
            Message msg = handle.read();
            // Check if received data is RPM related.
            if(msg.data[2] == requestedPID){
                handle.busOff();
                return msg;
            }else{
                if(msg.isErrorFrame()) {
                    //if msg is an error
                    handle.busOff();
                    return null;
                }
            }
        }else{
            //if no message, either vehicle turned off or some sort of error.
            handle.busOff();
            return null;
        }
        return null;
    }
}
