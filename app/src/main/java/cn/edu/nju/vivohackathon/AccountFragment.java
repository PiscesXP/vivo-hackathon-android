package cn.edu.nju.vivohackathon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import cn.edu.nju.vivohackathon.tools.network.powerpost.PowerPostCallback;


public class AccountFragment extends Fragment implements PowerPostCallback {

    final int Request_userInfo = 1,Request_achievement = 2,Request_creation = 3;



    

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public AccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }


    @Override
    public void onFail(int reqID, String errorMessage) {
        Toast.makeText(this.getActivity(),R.string.network_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(int reqID, JSONObject resultJson) {
        switch(reqID){

        }
    }
















}
