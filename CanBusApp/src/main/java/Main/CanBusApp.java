package Main;

import CanWrapper.Canlib;
import CanWrapper.CanlibException;
import CanWrapper.Handle;
import CanWrapper.Message;

public class CanBusApp {

    private Handle handle;
    private boolean hasConnection = false;

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
    }

    /**
     * Function to go on the channel of the can-bus and start receiving/sending specific messages.
     * @param requestedPID
     * @return
     * @throws CanlibException
     */
    public Message getFromCan(int requestedPID) throws CanlibException {
        // Go on the bus.
        handle.busOn();
        //send a request for ENGINE_RPM
        handle.write(new Message(CAN_CODES.REQUEST_MSG, new byte[]{0x2, CAN_CODES.MODE_SCD, (byte) requestedPID, 0x55, 0x55, 0x55, 0x55}, 8, 0));
        //Boolean to keep loop going until correct message is received.
        boolean receivedMsg = false;
        //Making sure loops breaks after x loops by keeping tally of how many loops is done.
        int nrOfLoops = 0;
        Message msg = null;
        do{
            nrOfLoops++;
            if(handle.hasMessage()){
                msg = handle.read(); // read the message from canbus.
            }else{
                continue; // continue to next iteration of loop and skip code thats under.
            }
            if(msg.data[2] == requestedPID){
                receivedMsg = true;
            }else{
                if(msg.isErrorFrame()){
                    //Dump msg if error so we can see the data.
                    System.out.println("Message ID:");
                    System.out.println(msg.id);
                    System.out.println("Message data:");
                    System.out.println(msg.data[0]+" - "+msg.data[1]+" - "+msg.data[2]+" - "+msg.data[3]+" - "+msg.data[4]+" - "+msg.data[5]+" - "+msg.data[6]+" - "+msg.data[7]);
                    System.out.println("Message flag:");
                    System.out.println(msg.flags);
                    System.out.println("Message length:");
                    System.out.println(msg.length);
                }
                msg = null;
                continue;
            }
        }while(receivedMsg || nrOfLoops < 10); // break loop when right message received or when max X nr of loops have iterated.
        // go off the bus
        handle.busOff();
        return msg;
    }
}
