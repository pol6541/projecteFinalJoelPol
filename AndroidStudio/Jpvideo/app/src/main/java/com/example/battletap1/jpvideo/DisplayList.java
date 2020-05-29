package com.example.battletap1.jpvideo;

import android.widget.ImageView;

public class DisplayList {
    String name;
    String link;
    String auth;

    public DisplayList(String name, String link, String auth) {
        this.name = name;
        this.link = link;
        this.auth = auth;
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
}
