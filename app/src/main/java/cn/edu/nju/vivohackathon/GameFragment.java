package cn.edu.nju.vivohackathon;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;


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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View mView;

    private OnFragmentInteractionListener mListener;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

//TODO

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment       view =  inflater.inflate(R.layout.fragment_a, container, false);
        mView = inflater.inflate(R.layout.fragment_game, container, false);


        LinearLayout vertical = mView.findViewById(R.id.game_vertical);

        //TableLayout tableLayout = mView.findViewById(R.id.game_vertical);

        for (int i = 0; i < 4; i++) {
            //创建一行
            LinearLayout horizental = new LinearLayout(getContext());
            horizental.setOrientation(LinearLayout.HORIZONTAL);
            horizental.setWeightSum(3);
            for (int column = 0; column < 4; ++column) {
                /*
                ImageView imageView = new ImageView(getContext());
                imageView.setBackground(getActivity().getDrawable(R.drawable.image_background));
                imageView.setImageDrawable(getActivity().getDrawable(R.drawable.ic_home_black_24dp));
                imageView.setMinimumWidth(32);
                imageView.setMaxWidth(32);
                row.addView(imageView);

                TextView textView = new TextView(getContext());
                textView.setText("A");
                textView.setWidth(32);
                textView.setBackground(getActivity().getDrawable(R.drawable.image_background));
                row.addView(textView);
*/
                Button button = new Button(getContext());
                button.setLayoutParams(new LinearLayout.LayoutParams(160,160));
                if (column % 2 == 0) {
                    button.setBackground(getActivity().getDrawable(R.drawable.image_background));
                    button.setForeground(new ColorDrawable(Color.TRANSPARENT));
                    button.setTextSize(14);
                    button.setText("A");
                } else {
                    button.setForeground(getActivity().getDrawable(R.drawable.ic_home_black_24dp));
                    button.setBackground(getActivity().getDrawable(R.drawable.image_background));
                }
                horizental.addView(button);
            }
            vertical.addView(horizental);
        }


        return mView;
    }

































    // TODO: Rename method, update argument and hook method into UI event
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
            //throw new RuntimeException(context.toString()
            //        + " must implement OnFragmentInteractionListener");
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
