package Main;

import CanWrapper.Message;
import Main.FormulaCollection;

public class Main {

    public static CanBusApp canbus;

    // FOR TESTING THE CANBUS ITSELF ONLY.

    public static void main(String[] args){
        try{
            FormulaCollection formula = new FormulaCollection();
            canbus = new CanBusApp("00-00000-00000-0", "1");
            while(true){
                Message msg = canbus.getFromCan(CAN_CODES.ENGINE_RPM);
                if(msg==null){
                    System.out.println("Null");
                }else{
                    int result = formula.getRpm(msg.data[3], msg.data[4]);
                    System.out.println("RPM: "+result);
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
