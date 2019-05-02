package Main;

public class FormulaCollection {

    public FormulaCollection(){
    }

    /**
     * Function to calculate the RPM from 2 bytes of data received from a can-bus
     * @param dataA
     * @param dataB
     * @return
     */
    public int getRpm(byte dataA, byte dataB){
        int RPM = (256*dataA+dataB)/4; // Formula to compute RPM from byte data.
        return RPM;
    }

    /**
     * Function to calculate the throttle position from a byte of data received from a can-bus
     * @param dataA
     * @return
     */
    public int getThrottlePosition(byte dataA){
        int Throttle_position = (100/255)*dataA; // Formula to compute throttle position from byte data.
        return Throttle_position;
    }

    /**
     * Function to calculate the engine coolant temperature from a byte of data received from a can-bus
     * @param byteA
     * @return
     */
    public int getEngineCoolantTemp(byte byteA){
        return byteA - 40; // Formula to compute the engine coolant temperature.
    }

}
