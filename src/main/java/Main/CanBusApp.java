package Main;

import CanWrapper.Canlib;
import CanWrapper.CanlibException;
import CanWrapper.Handle;
import CanWrapper.Message;

public class CanBusApp {

    private Handle handle;
    private boolean hasConnection = false;
    private boolean sendRequest = false;

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
        handle.busOn();
    }

    /**
     * Function to go on the channel of the can-bus and start receiving/sending specific messages.
     * @param requestedPID
     * @return
     * @throws CanlibException
     */
    public Message getFromCan(int requestedPID) throws CanlibException {
        handle.write(new Message(CAN_CODES.REQUEST_MSG, new byte[]{0x2, CAN_CODES.MODE_SCD, 0x0C, 0x55, 0x55, 0x55, 0x55}, 8, 0));
        sendRequest = true;
        //Making sure loops breaks after x loops by keeping tally of how many loops is done.
        int nrOfReads = 0;
        Message msg = null;
        System.out.println("-----------------------------------------------------");
        do{
            System.out.println("teasafasda");
            if(handle.hasMessage()){
                msg = handle.read(); // read the message from canbus.
                if(msg.data[2] == 0x0C){
                    //receivedMsg = true;
                    return msg;
                }
            }else{
                handle.write(new Message(CAN_CODES.REQUEST_MSG, new byte[]{0x2, CAN_CODES.MODE_SCD, 0x0C, 0x55, 0x55, 0x55, 0x55}, 8, 0));
                }
            nrOfReads++;
            continue;
        }while(nrOfReads < 10); // break loop when right message received or when max X nr of loops have iterated.
        nrOfReads = 0;
        return msg;
    }
}
