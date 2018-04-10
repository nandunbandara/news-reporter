package me.nandunb.newsreporter.models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nandunb on 3/31/18.
 */

public class Post extends News{

    private String email;
    private String displayName;
    private int likes;

    public Post(){
        super();
    }

    public Post(String email, String displayName, String photoUrl, String caption){
        super();
        this.email = email;
        this.displayName = displayName;
        super.photoUrl = photoUrl;
        super.caption = caption;
        super.createdOn = new Date();
        this.likes = 0;
    }

    public String getEmail(){
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCaption() {
        return caption;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
