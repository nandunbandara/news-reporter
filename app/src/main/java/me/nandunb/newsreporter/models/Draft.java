package me.nandunb.newsreporter.models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nandunb on 4/10/18.
 */

public class Draft extends News{

    private Date updatedOn;


    public Draft(){
        super();
        this.updatedOn = new Date();
    }

    public Draft(String photoUrl, String caption){

        super();
        this.updatedOn = new Date();
        super.photoUrl = photoUrl;
        super.caption = caption;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedOnDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
        Date now = updatedOn;
        return sdf.format(now);
    }
}
