package com.matty_christopher.englandplayertracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Inflater;

public class SquadFragment extends Fragment implements View.OnClickListener {
    private static final int NUM_PAGES = 20;
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private JSONArray output;
    private int[] logos;
    private ArrayList<String> wins;
    private ArrayList<String> draws;
    private ArrayList<String> losses;
    private ArrayList<String> teams;
    private View v;
    private OnFragmentInteractionListener mListener;
    private AutoCompleteTextView searchbox;

    public SquadFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.activity_screen_slide, container, false);
        Button button=(Button)v.findViewById(R.id.button);
        button.setOnClickListener(this);
        searchbox=(AutoCompleteTextView)v.findViewById(R.id.searchbox);
        searchbox.setHint("enter team here...");

        DatabaseConnection_Fixtures databaseConnection_fixtures=new DatabaseConnection_Fixtures(getContext());
        databaseConnection_fixtures.getSquads(new Db_response<JSONArray>() {
            @Override
            public void processFinish(final JSONArray output) {
                if (output.length() > 0) {
                    getData(output);

                } else {
                    Toast.makeText(getActivity(), "Problem retrieving data", Toast.LENGTH_SHORT).show();
                }
            }
        });





        return v;
    }

    private void getData(JSONArray output) {
        this.output=output;

        wins = new ArrayList<>();
        draws = new ArrayList<>();
        losses = new ArrayList<>();
        teams = new ArrayList<>();
        long time1=new Date().getTime();
        for (int i = 0; i < output.length(); i++) {
            String teamName = "";
            String win = "";
            String draw = "";
            String lost = "";
            try {
                JSONObject jsonobject = output.getJSONObject(i);
                teamName = jsonobject.getString("name");
                win = jsonobject.getString("wins");
                draw = jsonobject.getString("draws");
                lost = jsonobject.getString("lost");
                //Log.e("test team name",teamName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!teams.contains(teamName)) {
                teams.add(teamName);
                wins.add(win);
                draws.add(draw);
                losses.add(lost);
            }

        }
        long time2=new Date().getTime();
        Log.e("measure Time", "" + (time2 - time1) + " loops: ");
        logos= new int[]{R.drawable.afc_bournemouth, R.drawable.arsenal, R.drawable.burnley, R.drawable.chelsea, R.drawable.crystal_palace,
                R.drawable.everton, R.drawable.hull_city, R.drawable.leicester, R.drawable.liverpool, R.drawable.manchester_city, R.drawable.manchester_united,
                R.drawable.middlesbrough, R.drawable.southampton, R.drawable.stoke, R.drawable.sunderland, R.drawable.swansea, R.drawable.tottenham, R.drawable.watford,
                R.drawable.west_bromwich, R.drawable.west_ham};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,teams);
        searchbox.setAdapter(adapter);
        searchbox.setThreshold(1);

        mPager = (ViewPager) v.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());

        new setAdapterTask().execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                gotoPage();
                break;
        }
    }

    private void gotoPage() {
        String word=searchbox.getText().toString();
        int pos=-1;
        for(int i=0;i<teams.size();i++){
            if(teams.get(i).equalsIgnoreCase(word)){
                pos=i;
                break;
            }
        }
        if(pos>=0){
            Toast.makeText(getActivity(), word, Toast.LENGTH_SHORT).show();
            mPager.setCurrentItem(pos);
            searchbox.setText("");
        }
        else{
            Toast.makeText(getActivity(), "Could not find team", Toast.LENGTH_SHORT).show();
        }
    }

    private class setAdapterTask extends AsyncTask<Void,Void,Void> {
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mPager.setAdapter(mPagerAdapter);
            mPager.setPageTransformer(true, new ZoomOutPageTransformer());
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

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public ScreenSlidePageFragment getItem(int position) {
            Bundle bundle = new Bundle();

            int logo=logos[position];
            String win=wins.get(position);
            String draw=draws.get(position);
            String loss=losses.get(position);
            String team=teams.get(position);
            String data=output.toString();

            Log.e("check pos outer", "" + position+" "+teams.get(position));

            return ScreenSlidePageFragment.newInstance(logo,win,draw,loss,team,data);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
