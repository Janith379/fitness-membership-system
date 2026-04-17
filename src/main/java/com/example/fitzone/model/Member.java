package com.example.fitzone.model;

public class Member {

    private int id;
    private String name;
    private String plan;
    private String duration;

    public Member(){}

    public Member(int id,String name,String plan,String duration){
        this.id=id;
        this.name=name;
        this.plan=plan;
        this.duration=duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
