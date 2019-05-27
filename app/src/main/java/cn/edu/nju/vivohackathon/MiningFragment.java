package cn.edu.nju.vivohackathon;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.edu.nju.vivohackathon.game.mining.Mining;
import cn.edu.nju.vivohackathon.game.python.Python;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MiningFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private View mView;

    private int mMiningSize = 5;
    private ImageView mImageView[][];
    private Mining mMining;

    private static final String TAG = GameFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    public MiningFragment() {
        // Required empty public constructor
    }


    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
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
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_mining, container, false);

        ((ImageView)mView.findViewById(R.id.iv_reset_mining)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"游戏重置成功",Toast.LENGTH_SHORT).show();
                reset();
            }
        });


        return mView;
    }


    private void reset(){
        final AbsoluteLayout absoluteLayout = mView.findViewById(R.id.game_absolute);
        absoluteLayout.removeAllViewsInLayout();
        miningStart();
        mImageView = mingingInit(5);
    }

    /**
     * 初始化方格.
     */
    private ImageView[][] mingingInit(final int Size) {
        ImageView imageViewArr[][] = new ImageView[Size][Size];
        mMiningSize = Size;
        int toLeft = 160, toTop = 100;
        final AbsoluteLayout absoluteLayout = mView.findViewById(R.id.game_absolute);
        for (int row = 0; row < Size; row++) {
            //创建一行

            final int ColumnCount[] = new int[]{3, 4, 5, 4, 3};
            for (int column = 0; column < ColumnCount[row]; ++column) {
                final ImageView imageView = new ImageView(getContext());
                imageView.setBackground(new ColorDrawable(Color.TRANSPARENT));
                imageView.setMaxWidth(100);
                //TODO
                if (mMining.get_grid(row, column) == null) {
                    imageView.setImageDrawable(getActivity().getDrawable(R.drawable.p3));
                } else {

                    imageView.setImageDrawable(getActivity().getDrawable(R.drawable.p4));
                }
                //位置

                imageView.setX(148 * column + (5 - ColumnCount[row]) * 75 + toLeft);

                imageView.setY(130 * row + toTop);
                imageView.setLayoutParams(new RelativeLayout.LayoutParams(200, 200));


                final int myrow = row;
                final int mycolumn = column;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "Click:" + myrow + ", " + mycolumn);

                        mMining.mine(myrow, mycolumn);
                        imageView.setBackground(new ColorDrawable(Color.TRANSPARENT));

                        //用textview替代image
                        imageView.setBackground(null);
                        imageView.setForeground(null);
                        imageView.setImageDrawable(null);

                    }
                });
                imageViewArr[row][column] = imageView;
                absoluteLayout.addView(imageView);
                String mine = mMining.get_grid(myrow, mycolumn);
                if (mine != null) {
                    Log.d(TAG, String.format("Found at %d,%d:%s", row, column, mine));
                    TextView textView = new TextView(getContext());
                    textView.setX(imageView.getX() + 64 - mine.length() * 5);
                    textView.setY(imageView.getY() + 64);
                    textView.setText(mine);
                    textView.setTextSize(16);
                    TextPaint textPaint = textView.getPaint();
                    textPaint.setFakeBoldText(true);
                    textView.setTextColor(Color.rgb(128, 0, 128));
                    absoluteLayout.addView(textView);
                }
            }
        }
        return imageViewArr;
    }


    private void miningStart() {
        try {
            InputStream inputStream = getContext().getAssets().open("data.Mining");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            mMining = Mining.read(bufferedReader);
        } catch (Exception e) {
            Log.e(TAG, "Error on miningStart:" + e.toString());
        }
    }


    //刷新网格
    private void refreshGrid() {
        for (int row = 0; row < mMiningSize; row++) {
            for (int column = 0; column < mMiningSize; ++column) {
                ImageView button = mImageView[row][column];
                if (mMining.get_grid(row, column) == null) {
                    setButtonImage(button, false);
                } else {
                    setButtonImage(button, true);
                }
            }
        }
        if (mMining.fail()) {
            Toast.makeText(getContext(), "通关失败", Toast.LENGTH_LONG).show();
        }
        if (mMining.pass()) {
            Toast.makeText(getContext(), "通关成功", Toast.LENGTH_LONG).show();
        }
    }

    //设置图片
    private void setButtonImage(ImageView imageView, boolean hasCircle) {
        if (hasCircle) {
            imageView.setImageDrawable(getActivity().getDrawable(R.drawable.p4));
        } else {
            imageView.setImageDrawable(getActivity().getDrawable(R.drawable.p3));
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
