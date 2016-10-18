package com.matty_christopher.englandplayertracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FixturesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FixturesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FixturesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //private TextView textView;
    private TableLayout showFixtures;
    private View v;

    private OnFragmentInteractionListener mListener;

    public FixturesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FixturesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FixturesFragment newInstance(String param1, String param2) {
        FixturesFragment fragment = new FixturesFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_fixtures, container,false);
        showFixtures = (TableLayout) v.findViewById(R.id.fixture_table);

        //textView = (TextView)v.findViewById(R.id.textView2);
        //textView.setText("Hello");
        DatabaseConnection_Fixtures dbconnection=new DatabaseConnection_Fixtures(getContext());
        dbconnection.getFixtures(new Db_response<ArrayList<ArrayList>>() {
            @Override
            public void processFinish(ArrayList<ArrayList> output) {
                if(output.size()>0){
                    updateFixtures(output);
                }
            }
        });


      return v;
    }

    private void updateFixtures(ArrayList<ArrayList> output) {
        ArrayList<ArrayList<String>> rows=new ArrayList<>();
        ArrayList<Integer> rounds=new ArrayList<>();
        ArrayList<String> home=new ArrayList<>();
        ArrayList<String> away=new ArrayList<>();
        for(int i=0;i<output.size();i++){
            rows.add(output.get(i));
            rounds.add(Integer.parseInt(rows.get(i).get(0)));
            home.add(rows.get(i).get(1));
            away.add(rows.get(i).get(2));
            //Log.e("tag", ""+rows.get(i)+" "+rounds.get(i));

        }

        int currentRound=0;

        for (int i = 0; i <rows.size(); i++) {
            TableRow row = new TableRow(getActivity());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            int thisRound=rounds.get(i);
            if(thisRound>currentRound){
                currentRound=thisRound;
                TableRow rowRound = new TableRow(getActivity());
                rowRound.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView tv1 = new TextView(getActivity());
                tv1.setText("Round " + rounds.get(i));
                tv1.setTextColor(Color.BLACK);
                tv1.setTextSize(25);
                tv1.setGravity(Gravity.CENTER_HORIZONTAL);
                tv1.setPadding(5, 10, 5, 10);
                rowRound.addView(tv1);
                rowRound.setGravity(Gravity.CENTER_HORIZONTAL);
                rowRound.setBackgroundColor(Color.rgb(182, 236, 255));
                showFixtures.addView(rowRound, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            }

            TextView homeTxt = new TextView(getActivity());
            homeTxt.setText(home.get(i));
            homeTxt.setTextColor(Color.BLACK);
            homeTxt.setGravity(Gravity.CENTER_HORIZONTAL);
            homeTxt.setTextSize(13);
            homeTxt.setPadding(5, 5, 5, 5);
            row.addView(homeTxt);

            TextView vs = new TextView(getActivity());
            vs.setText("v");
            vs.setTextColor(Color.BLACK);
            vs.setTextSize(13);
            vs.setPadding(1, 5, 1, 5);
            row.addView(vs);

            TextView awayTxt = new TextView(getActivity());
            awayTxt.setText(away.get(i));
            awayTxt.setTextColor(Color.BLACK);
            awayTxt.setGravity(Gravity.CENTER_HORIZONTAL);
            awayTxt.setTextSize(13);
            awayTxt.setPadding(5, 5, 5, 5);
            row.addView(awayTxt);


            showFixtures.addView(row, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        }


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
        Activity a;

        if (context instanceof Activity){
            a=(Activity) context;
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
