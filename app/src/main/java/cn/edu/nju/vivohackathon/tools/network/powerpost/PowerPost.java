package cn.edu.nju.vivohackathon.tools.network.powerpost;


import android.content.Context;

public abstract class PowerPost {

    public static PowerShitData request(Context context, final String url) {
        return new PowerShitData(context, url);
    }

}


