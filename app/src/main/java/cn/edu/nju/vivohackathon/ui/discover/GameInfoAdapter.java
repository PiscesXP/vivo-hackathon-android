package cn.edu.nju.vivohackathon.ui.discover;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.edu.nju.vivohackathon.MainActivity;
import cn.edu.nju.vivohackathon.R;

public class GameInfoAdapter extends RecyclerView.Adapter<GameInfoAdapter.GameInfoViewHolder> {

    private List<GameInfo> mGameInfoList;

    private static final String TAG = GameInfoAdapter.class.getSimpleName();

    public static class GameInfoViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView description;
        public Button button;
        public GameInfoViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.in_discover_item_iton);
            title = v.findViewById(R.id.in_discover_item_gamename);
            description = v.findViewById(R.id.in_discover_item_remarks);
            button = v.findViewById(R.id.btn_enter_game);
        }

    }

    public GameInfoAdapter(List<GameInfo> gameInfoList) {
        mGameInfoList = gameInfoList;
    }

    @NonNull
    @Override
    public GameInfoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.in_discover_item, viewGroup, false);
        return new GameInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameInfoViewHolder viewHolder, int i) {
        final GameInfo gameInfo = mGameInfoList.get(i);
        viewHolder.title.setText(gameInfo.getTitle());
        viewHolder.description.setText(gameInfo.getDescription());
        String base64 = gameInfo.getImageBase64();
        if (base64 != null && !base64.isEmpty()) {
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            viewHolder.image.setImageBitmap(decodedByte);
        } else {
            Log.i(TAG, "No image found for gameID:" + gameInfo.getGameID());

        }
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int gameID = gameInfo.getGameID();
                Log.i(TAG, "Enter gameID:" + gameID);
                //TODO 进入游戏
            }
        });
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGameInfoList.size();
    }
}
