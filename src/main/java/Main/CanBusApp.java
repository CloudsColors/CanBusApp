package Main;

import Main.WriteToFile;
import CanWrapper.Canlib;
import CanWrapper.CanlibException;
import CanWrapper.Handle;
import CanWrapper.Message;
import CanWrapper.NoMessageException;
import Main.CAN_CODES;

public class CanBusApp {

    private Handle handle;

    //ean = "73-30130-00861-8", sn = "10540"

    public CanBusApp(String ean, String sn) throws CanlibException{
        // A handle either needs a channel, or a EAN togheter with a Serial.no.
        handle = new Handle(ean, sn); // PCI kvaser card.
        // Sets the parameter for the can bus.
        handle.setBusParams(Canlib.canBITRATE_500K, 0, 0, 0, 0, 0);
    }

    public int getRPM() throws CanlibException{
        // The handle goes on the bus
        handle.busOn();
        //send a request for ENGINE_RPM
        handle.write(new Message(CAN_CODES.REQUEST_MSG, new byte[]{0x2, CAN_CODES.MODE_SCD, CAN_CODES.ENGINE_RPM, 0x55, 0x55, 0x55, 0x55}, 8, 0));
        // Wait for a message at most 1s.
        if(handle.hasMessage(1000)){
            // if there's a message, read the message.
            Message msg = handle.read();
            // Check if received data is RPM related.
            if(msg.data[2] == CAN_CODES.ENGINE_RPM){
                int dataA = msg.data[3]; // DataA according to byte[] structure is byte 4
                int dataB = msg.data[4]; // DataB according to byte[] structure is byte 5
                int RPM = (256*dataA+dataB)/4; // Formula to compute RPM from byte data.
                handle.busOff();
                return RPM;
            }else{
                if(msg.isErrorFrame()) {
                    //if msg is an error
                    handle.busOff();
                    return -1;
                }
            }
        }else{
            //if no message, either vehicle turned off or some sort of error.
            handle.busOff();
            return -1;
        }
        return -1;
    }
}
