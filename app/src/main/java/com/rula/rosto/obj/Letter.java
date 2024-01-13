package com.rula.rosto.obj;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Letter {
    String id;
    String title, text;
    String time;
    public Letter(){}
    public Letter(String id, String title, String text, long time) {
        this.time = String.valueOf(time);
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }
}
