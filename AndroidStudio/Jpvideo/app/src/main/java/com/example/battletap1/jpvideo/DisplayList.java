package com.example.battletap1.jpvideo;

import java.util.Date;

public class DisplayList {
    String name;
    String link;
    String auth;
    Long temps;

    public DisplayList(String name, String link, String auth) {
        this.name = name;
        this.link = link;
        this.auth = auth;

        temps = new Date().getTime();
    }
    public DisplayList(){
        
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getAuth() {
        return auth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public long getTime() {
        return temps;
    }

    public void seTime(long Time) {
        this.temps = Time;
    }
}
