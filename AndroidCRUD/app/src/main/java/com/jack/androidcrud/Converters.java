package com.jack.androidcrud;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by Jack on 4/16/2018.
 */

public class Converters {

    @TypeConverter
    public Date fromTimeStamp(Long value){
        //get date from epoc number
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimeStamp(Date date){
        //get number since epoc
        return date == null ? null : date.getTime();
    }

}
