package Main;

public class CAN_CODES {

    /*-------------Modes-------------*/
    public static final int MODE_SCD = 0x01; // Show current data
    public static final int MODE_FFD = 0x02; // Show freeze frame data
    public static final int MODE_SDTD = 0X03; // Show storage diagnostic trouble codes
    public static final int MODE_CDTC = 0x04; // Clear diagnostic trouble codes and stored values
    public static final int MODE_TROSM = 0x05; // Test result, oxygen sensor monitoring (non can only)
    public static final int MODE_TROCM = 0x06; // Test results other component/system monitoring)
    public static final int MODE_SPDTC = 0x07; // Show pending diagnostic trouble codes
    public static final int MODE_COOOC = 0x08; // control operation of on-board component/system
    public static final int MODE_RVI = 0x09; // Request vehicle information
    /*-------------PID codes-------------*/
    public static final int FREEZE_DTC = 0x02; // freeze dtc
    public static final int FUEL_SYSTEM_STATUS = 0x03; // fuel system status
    public static final int CALC_ENGINE_LOAD = 0x04; // Calculated engine load
    public static final int ENGINE_COOL_TEMP = 0x05; // Engine Coolant temperature.
    public static final int FUEL_PRESSURE = 0x0A; // Fuel pressure.
    public static final int ENGINE_RPM = 0x0C; // Engine RPM
    public static final int VEHICLE_SPEED = 0x0D; // Vehicle speed
    public static final int THROTTLE_POSITION = 0x11; // Throttle position
    /*-------------Type of message-------------*/
    public static final int REQUEST_MSG = 0x7DF;
    public static final int[] RESPONSE_MSG = {0x7E8, 0x7E9, 0x7EA, 0x7EB, 0x7EC, 0x7ED, 0x7EE, 0x7EF};

}
