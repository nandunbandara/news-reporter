package me.nandunb.newsreporter.models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nandunb on 4/10/18.
 */

public class News {

    protected String caption;
    protected String photoUrl;
    protected Date createdOn;

    public News() {
        super();
        this.createdOn = new Date();
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCaption() {
        return caption;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
        Date now = createdOn;
        return sdf.format(now);
    }
}
