package Main;

import CanWrapper.Canlib;
import CanWrapper.CanlibException;
import CanWrapper.Handle;
import CanWrapper.Message;

public class CanBusApp {

    private Handle handle;

    /**
     * Constructor to create a handle for the can-bus that should be communicated with.
     * @param ean
     * @param sn
     * @throws CanlibException
     */
    public CanBusApp(String ean, String sn) throws CanlibException{
        // A handle either needs a channel, or a EAN togheter with a Serial.no.
        handle = new Handle(ean, sn); // PCI kvaser card.
        // Sets the parameter for the can bus.
        handle.setBusParams(Canlib.canBITRATE_500K, 0, 0, 0, 0, 0);
        // Go on the bus.
        handle.busOn();
    }

    /**
     * Function to go on the channel of the can-bus and start receiving/sending specific messages.
     * @param requestedPID
     * @return
     * @throws CanlibException
     */
    public Message getFromCan(byte requestedPID) throws CanlibException {
        //send a request for ENGINE_RPM
        handle.write(new Message(ReadPIDCodes.REQUEST_MSG, new byte[]{0x2, ReadPIDCodes.MODE_SCD, requestedPID, 0x55, 0x55, 0x55, 0x55}, 8, 0));
        //Making sure loops breaks after x loops by keeping tally of how many loops is done.
        int nrOfLoops = 0;
        Message msg = null;
        do{
            // Check if theres a message in the buffer.
            if(handle.hasMessage()){
                // read the message
                msg = handle.read();
                // check if msg is the requested msg
                if(msg.data[2] == requestedPID){
                    return msg;
                }
            // resend the request message
            }else{
                // resend the request.
                handle.write(new Message(ReadPIDCodes.REQUEST_MSG, new byte[]{0x2, ReadPIDCodes.MODE_SCD, requestedPID, 0x55, 0x55, 0x55, 0x55}, 8, 0));
                // continue to next iteration
                continue;
            }
        }while(nrOfLoops < 10); // break loop when right message received or when max X nr of loops have iterated.
        return msg;
    }
}
