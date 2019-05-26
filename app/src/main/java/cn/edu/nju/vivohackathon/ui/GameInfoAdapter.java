package cn.edu.nju.vivohackathon.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.edu.nju.vivohackathon.R;

public class GameInfoAdapter extends RecyclerView.Adapter<GameInfoAdapter.GameInfoViewHolder> {

    private List<GameInfo> mGameInfoList;

    public static class GameInfoViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView image;
        public TextView title;
        public TextView description;

        public GameInfoViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.heroImage);
            title = (TextView) v.findViewById(R.id.matchType);
            description = (TextView) v.findViewById(R.id.time);
            result = (TextView) v.findViewById(R.id.result);
        }

    }

    public GameInfoAdapter(List<GameInfo> gameInfoList) {
        mGameInfoList = gameInfoList;
    }

    @NonNull
    @Override
    public GameInfoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.record_preview, viewGroup, false);
        return new GameInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameInfoViewHolder viewHolder, int i) {
        GameInfo gameInfo = mGameInfoList.get(i);
        viewHolder.title.setText(gameInfo.getTitle());
        viewHolder.description.setText(gameInfo.getDescription());
        String base64 = gameInfo.getImageBase64();
        if (base64 != null && !base64.isEmpty()) {
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            viewHolder.image.setImageBitmap(decodedByte);
        }
    }

    @Override
    public int getItemCount() {
        return mGameInfoList.size();
    }
}
