package Main;

import java.io.FileOutputStream;
import java.io.IOException;

public class WriteToFile {

    private String filename;
    private FileOutputStream fOut;

    protected WriteToFile(String filename){
        this.filename = filename;
    }

    public void write(int id, byte[] data){
        //create the fileoutputstream, to write down bytes to text file.
        try {
            // Open the fileoutputstream
            fOut = new FileOutputStream(this.filename, true);
            // Write the ID first
            fOut.write(String.format("ID:%2X\nDATA:" , id).getBytes());
            // Loop out each byte
            for (byte b : data) {
                fOut.write(String.format("%2X", b).getBytes());
            }
            // exit with a line break for next reading.
            fOut.write(String.format("%2S", "\n").getBytes());
        }catch(IOException e){
            System.out.println(e);
        }
    }
}
