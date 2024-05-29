/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wtstockhausen.utils;

import java.lang.reflect.Field;
import java.util.logging.Logger;

/**
 *
 * @author william.stockhausen
 */
public class FieldFinder {
    public static final Field getField(Class cls,String name){
        if (cls==null) return null;
        Logger.getLogger(FieldFinder.class.getName()).info("-testing for field named "+name+" in "+cls.getName());
        Field[] flds = cls.getDeclaredFields();
        for (Field fld : flds) {
            Logger.getLogger(FieldFinder.class.getName()).info("--found field named "+fld.getName());
            if (fld.getName().equals(name)) return fld;
        }
        return getField(cls.getSuperclass(),name);
    }
}
