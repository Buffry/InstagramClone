package com.instagramclone.instagramclone.Utils;

public class StringManipulation {

    public static String expandUsername(String username){
        return username.replace(".", " ");
    }


    public static String collapseUsername(String username){
        return username.replace(" ", ".");
    }
}
