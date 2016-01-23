package com.qualcomm.ftcrobotcontroller.opmodes.customops.Storage;

/**
 * Created by root on 1/21/16.
 */
public class Saver
{

    int[] savedInfo = new int[5555555];
    int infoLocation;
    String[] infoTag = new String[5555555];
    int tagLocation;
    int savedInfoSize = savedInfo.length;

    public Saver() {}

    public void save(int info, String tag)
    {
        savedInfo[infoLocation] = info;
        infoTag[tagLocation] = tag;
        tagLocation ++;
        infoLocation ++;
    }

    public void spit(int amount)
    {

        int savedInfoSize = savedInfo.length;

        for(int i = 0; i < amount && i < savedInfoSize; i++)
        {

            System.out.println(infoTag[i] +" : " + savedInfo[i]);

        }

    }

    public void spitAll()
    {

        for(int i = 0;  i < savedInfoSize; i++)
        {

            if(infoTag[i] != null)
            {
                System.out.println(infoTag[i] + " : " + savedInfo[i] );
            }
            else
            {
                break;
            }
        }
    }

    public void spitTag(String tag)
    {

        for(int i = 0; i< savedInfoSize; i ++){
            if(infoTag[i] == tag){
                System.out.println(infoTag[i] + " : " + savedInfo[i]);
            }
        }
    }
}