package cn.edu.nju.vivohackathon.tools.network.powerpost;


import android.content.Context;

public abstract class PowerPost {

    public static PowerShitData request(int reqID, Context context, final String url) {
        return new PowerShitData(reqID, context, url);
    }

}


