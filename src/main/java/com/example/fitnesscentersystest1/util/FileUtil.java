package com.example.fitnesscentersystest1.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    private static final String FILE_NAME="members.txt";

    public static void saveMember(String data){

        try{
            FileWriter writer=new FileWriter(FILE_NAME,true);
            writer.write(data+"\n");
            writer.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public static List<String> readMembers(){

        List<String> members=new ArrayList< >();

        try{
            BufferedReader reader=new BufferedReader(new FileReader(FILE_NAME));
            String line;

            while((line=reader.readLine())!=null){
                members.add(line);
            }

            reader.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return members;
    }
}