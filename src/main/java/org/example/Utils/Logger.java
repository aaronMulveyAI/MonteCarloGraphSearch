package org.example.Utils;


import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private FileWriter fileWriter;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String DATA;

    public Logger(String filePath) {
        this.DATA = "";
        try {
            boolean APPEND = true;
            fileWriter = new FileWriter(filePath, APPEND);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void log(String message) {
        try {
            String timeStamp = dateFormat.format(new Date());
            fileWriter.write(timeStamp + " - " + message + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void write(String message){
        this.DATA = this.DATA.concat(message + "\n");
    }

    public void save(){
        try {
            fileWriter.write(DATA);
            fileWriter.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.DATA = "";
    }
}