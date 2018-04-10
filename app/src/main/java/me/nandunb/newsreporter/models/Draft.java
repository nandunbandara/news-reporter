package me.nandunb.newsreporter.models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nandunb on 4/10/18.
 */

public class Draft {

    private Date updatedOn;


    public Draft(){
        this.updatedOn = new Date();
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
