package com.matty_christopher.englandplayertracker;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Results_Fragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static int NUM_PAGES = 1;
    private OnFragmentInteractionListener mListener;
    private Spinner dropdown;
    private ViewPager mPager;
    private View view;
    private ArrayList<ResultsSlidePageAdapter> mPagerAdapterList;

    private ArrayList<ArrayList<String>> homeTeams;
    private ArrayList<ArrayList<String>> awayTeams;

    private ArrayList<ArrayList<Integer>> homeScores;
    private ArrayList<ArrayList<Integer>> awayScores;
    private List<Integer> list_rounds;

    public Results_Fragment() {
        // Required empty public constructor
    }


    public static Results_Fragment newInstance(String param1, String param2) {
        Results_Fragment fragment = new Results_Fragment();;
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
        view= inflater.inflate(R.layout.activity_screen_slide_results, container, false);
        dropdown=(Spinner)view.findViewById(R.id.spinner);

        DatabaseConnection databaseConnection=new DatabaseConnection(getContext());
        databaseConnection.getMatches(new Db_response<JSONArray>() {
            @Override
            public void processFinish(JSONArray output) {
                if (output.length() > 0) {
                    fillFragment(output);
                } else {
                    Toast.makeText(getActivity(), "Problem retrieving data", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    private void fillFragment(JSONArray output) {
        final int resultsSize=38;
        Set<Integer>rounds=new TreeSet<>();
        homeTeams=new ArrayList<>(resultsSize);
        awayTeams=new ArrayList<>(resultsSize);
        homeScores=new ArrayList<>(resultsSize);
        awayScores=new ArrayList<>(resultsSize);
        for(int i=0;i<36;i++){
            homeTeams.add(new ArrayList<String>());
            awayTeams.add(new ArrayList<String>());
            homeScores.add(new ArrayList<Integer>());
            awayScores.add(new ArrayList<Integer>());
        }
        for(int i=0;i<output.length();i++){

            try {
                JSONObject jsonobject = output.getJSONObject(i);
                int index=jsonobject.getInt("round");
                rounds.add(index);
                homeTeams.get(index-1).add(jsonobject.getString("home"));
                awayTeams.get(index-1).add(jsonobject.getString("away"));
                homeScores.get(index-1).add(jsonobject.getInt("score_home"));
                awayScores.get(index-1).add(jsonobject.getInt("score_away"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        list_rounds=new ArrayList<>(rounds);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, list_rounds);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPagerAdapterList=new ArrayList<>();
        for(int i=0;i<36;i++){
            mPagerAdapterList.add(new ResultsSlidePageAdapter(getChildFragmentManager(),i));
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



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        int currentRound=Integer.parseInt(parent.getItemAtPosition(position).toString());

        mPager.setCurrentItem(0);
        mPagerAdapterList.get(currentRound-1).notifyDataSetChanged();
        mPager.setAdapter(mPagerAdapterList.get(currentRound-1));
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        NUM_PAGES=homeTeams.get(currentRound-1).size();
        mPagerAdapterList.get(currentRound-1).notifyDataSetChanged();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }





    private class ResultsSlidePageAdapter extends FragmentStatePagerAdapter {

        private int currentRound;
        public ResultsSlidePageAdapter(FragmentManager fm,int currentRound) {
            super(fm);
            this.currentRound=currentRound;

        }

        @Override
        public ResultsSlidePageFragment getItem(int position) {

            Bundle bundle = new Bundle();
            Integer round=currentRound;
            String homeTeam=homeTeams.get(currentRound).get(position);
            String awayTeam=awayTeams.get(currentRound).get(position);
            int homeScore=homeScores.get(currentRound).get(position);
            int awayScore=awayScores.get(currentRound).get(position);
            Log.e("check pos outer", ""+currentRound+" "+homeTeam);
            return ResultsSlidePageFragment.newInstance(round,homeTeam,awayTeam,homeScore,awayScore);

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
