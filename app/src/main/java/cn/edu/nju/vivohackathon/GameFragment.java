package cn.edu.nju.vivohackathon;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.edu.nju.vivohackathon.game.python.Python;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private View mView;

    private int mPythonSize = 0;
    private Button mButtonGrid[][];
    private Python mPython;

    private OnFragmentInteractionListener mListener;

    public GameFragment() {
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
        // Inflate the layout for this fragment       view =  inflater.inflate(R.layout.fragment_a, container, false);
        mView = inflater.inflate(R.layout.fragment_game, container, false);


        mButtonGrid = pythonInit(5);

        //方向按钮
        ((Button) mView.findViewById(R.id.btnPyUp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pythonUp();
            }
        });
        ((Button) mView.findViewById(R.id.btnPyDown)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pythonDown();
            }
        });
        ((Button) mView.findViewById(R.id.btnPyLeft)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pythonLeft();
            }
        });
        ((Button) mView.findViewById(R.id.btnPyRight)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pythonRight();
            }
        });

        return mView;
    }


    /**
     * 初始化方格.
     */
    private Button[][] pythonInit(final int Size) {
        Button buttonArray[][] = new Button[Size][Size];
        mPythonSize = Size;
        LinearLayout vertical = mView.findViewById(R.id.game_vertical);
        for (int row = 0; row < Size; row++) {
            //创建一行
            LinearLayout horizental = new LinearLayout(getContext());
            horizental.setOrientation(LinearLayout.HORIZONTAL);
            horizental.setWeightSum(3);
            for (int column = 0; column < Size; ++column) {
                Button button = new Button(getContext());
                button.setLayoutParams(new LinearLayout.LayoutParams(160, 160));
                if (column % 2 == 0) {
                    button.setBackground(getActivity().getDrawable(R.drawable.image_background));
                    button.setForeground(new ColorDrawable(Color.TRANSPARENT));
                    button.setTextSize(14);
                    button.setText("A");
                } else {
                    button.setForeground(getActivity().getDrawable(R.drawable.ic_home_black_24dp));
                    button.setBackground(getActivity().getDrawable(R.drawable.image_background));
                }
                buttonArray[row][column] = button;
                horizental.addView(button);
            }
            vertical.addView(horizental);
        }
        return buttonArray;
    }

    private void pythonStart() {
        try {
            InputStream inputStream = getActivity().getAssets().open("data.Python");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            mPython = Python.read(bufferedReader);
            for (int row = 0; row < mPythonSize; row++) {
                for (int column = 0; column < mPythonSize; ++column) {
                    Button button = mButtonGrid[row][column];
                    switch (mPython.get_grid(row, column)) {
                        //TODO 设置图像
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Error on pythonStart:" + e.toString());
        }
    }

    //方向键
    private void pythonLeft() {
        mPython.move(Python.left);
    }

    private void pythonUp() {
        mPython.move(Python.up);
    }

    private void pythonDown() {
        mPython.move(Python.down);
    }

    private void pythonRight() {
        mPython.move(Python.right);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
