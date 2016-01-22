package com.qualcomm.ftcrobotcontroller.opmodes.customops.Storage;

/**
 * Created by root on 1/21/16.
 */
public class Saver {

    int[] savedInfo;
    int infoLocation;
    String[] infoTag;
    int tagLocation;

    public Saver(){

    }

    public void save(int info, String tag){
        savedInfo[infoLocation] = info;
        infoTag[tagLocation] = tag;
        tagLocation ++;
        infoLocation ++;
    }

    public void spit(int amount){

        int savedInfoSize = savedInfo.length;

        for(int i = 0; i < amount && i < savedInfoSize; i++){
            System.out.println(infoTag[i].toString() +":");
            System.out.print(savedInfo[i]);
        }

    }

    public void spitAll(){

        int savedInfoSize = savedInfo.length;

        for(int i = 0;  i < savedInfoSize; i++){
            System.out.println(infoTag[i].toString() + ":");
            System.out.print(savedInfo[i]);
        }

    }
}
