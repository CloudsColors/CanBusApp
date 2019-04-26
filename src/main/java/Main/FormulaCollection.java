package Main;

import CanWrapper.CanlibException;
import CanWrapper.Message;

public class FormulaCollection {

    public FormulaCollection(){
    }

    public int getRpm(byte dataA, byte dataB){
        int RPM = (256*dataA+dataB)/4; // Formula to compute RPM from byte data.
        return RPM;
    }

    public int getThrottlePosition(byte dataA){
        int Throttle_position = (100/255)*dataA; // Formula to compute throttle position from byte data.
        return Throttle_position;
    }

    public int getEngineCoolantTemp(byte byteA){
        return byteA - 40; // Formula to compute the engine coolant temperature.
    }

}
