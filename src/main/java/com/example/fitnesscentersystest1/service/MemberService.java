package com.example.fitnesscentersystest1.service;


import com.example.fitnesscentersystest1.util.FileUtil;
import java.util.List;

public class MemberService {

    public void registerMember(int id,String name,String plan,String duration){

        String data=id+","+name+","+plan+","+duration;

        FileUtil.saveMember(data);

    }

    public List<String> getMembers(){

        return FileUtil.readMembers();

    }

}