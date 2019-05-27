package cn.edu.nju.vivohackathon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.vivohackathon.tools.network.powerpost.PowerPost;
import cn.edu.nju.vivohackathon.tools.network.powerpost.PowerPostCallback;
import cn.edu.nju.vivohackathon.ui.discover.GameInfo;
import cn.edu.nju.vivohackathon.ui.discover.GameInfoAdapter;


public class DiscoverFragment extends Fragment implements PowerPostCallback {

    private static final int REQUEST_GET_GAME_LIST = 1;
    private static final int REQUEST_GET_GAME_INFO = 2;

    private View mView;

    private RecyclerView mRecyclerView;
    private List<GameInfo> mGameInfoList;
    private GameInfoAdapter mGameInfoAdapter;

    private OnFragmentInteractionListener mListener;

    public DiscoverFragment() {
    }

    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_discover, container, false);

        //加入discover
        mRecyclerView = mView.findViewById(R.id.discover_recycleview);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mGameInfoList = new ArrayList<>();
        mGameInfoList.add(new GameInfo("Python", "描述1", null, 1));
        mGameInfoList.add(new GameInfo("Mining", "描述2", null, 2));
        mRecyclerView.setAdapter(mGameInfoAdapter);
        refreshRecyclerView();

        return mView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    //-------------------------------------------
    private void fetchGameList() {
        PowerPost
                .request(REQUEST_GET_GAME_LIST, getContext(), "getGameList")
                .data("page", 0)
                .callback(this);
    }


    private void fetchGameInfo(final int gameID) {
        PowerPost
                .request(REQUEST_GET_GAME_INFO, getContext(), "getGameInfo")
                .data("gameID", gameID)
                .callback(new PowerPostCallback() {
                    @Override
                    public void onFail(int reqID, String errorMessage) {
                        Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(int reqID, JSONObject resultJson) {
                        GameInfo gameInfo = new GameInfo(
                                resultJson.getString("title"),
                                resultJson.getString("description"),
                                resultJson.getString("img"),
                                gameID
                        );
                        mGameInfoList.add(gameInfo);
                        refreshRecyclerView();
                    }
                });
    }

    private void refreshRecyclerView() {
        mGameInfoAdapter = new GameInfoAdapter(mGameInfoList);
        mRecyclerView.setAdapter(mGameInfoAdapter);
    }

    @Override
    public void onFail(int reqID, String errorMessage) {
        //不显示了...
        //Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(int reqID, JSONObject resultJson) {
        switch (reqID) {
            case REQUEST_GET_GAME_LIST:
                //TODO
                JSONArray array = resultJson.getJSONArray("");
                for (int i = 0; i < array.size(); ++i) {
                    fetchGameInfo(array.getIntValue(i));
                }
                break;
            default:
        }
    }
}
