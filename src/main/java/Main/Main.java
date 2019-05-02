package Main;

import CanWrapper.Message;
import Main.FormulaCollection;

public class Main {

    public static CanBusApp canbus;

    // FOR TESTING THE CANBUS ITSELF ONLY.

    public static void main(String[] args){
        try{
            FormulaCollection formula = new FormulaCollection();
            canbus = new CanBusApp("73-30130-00441-2", "10043");
            while(true){
                Message msg = canbus.getFromCan(CAN_CODES.ENGINE_RPM);
                System.out.println(msg);
                if(msg!=null){
                    int result = formula.getRpm(msg.data[3], msg.data[4]);
                    System.out.println("RPM: "+result);
                }else{
                    System.out.println("null");
                }
            }
        }catch(Exception e){
            System.out.println("agsag");
            System.out.println(e);
        }
    }
}
