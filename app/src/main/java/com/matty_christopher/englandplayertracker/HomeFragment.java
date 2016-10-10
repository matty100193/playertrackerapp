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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int NUM_PAGES = 20;
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private JSONArray output;
    private int[] logos;
    private ArrayList<String> wins;
    private ArrayList<String> draws;
    private ArrayList<String> losses;
    private ArrayList<String> teams;
    private Bundle bundle;
    private View v;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        v= inflater.inflate(R.layout.activity_screen_slide, container, false);
        DatabaseConnection_Fixtures databaseConnection_fixtures=new DatabaseConnection_Fixtures();
        databaseConnection_fixtures.getSquads(new Db_response<JSONArray>() {
            @Override
            public void processFinish(JSONArray output) {
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
        Log.e("measure Time",""+(time2-time1)+" loops: ");
        logos= new int[]{R.drawable.afc_bournemouth, R.drawable.arsenal, R.drawable.burnley, R.drawable.chelsea, R.drawable.crystal_palace,
                R.drawable.everton, R.drawable.hull_city, R.drawable.leicester, R.drawable.liverpool, R.drawable.manchester_city, R.drawable.manchester_united,
                R.drawable.middlesbrough, R.drawable.southampton, R.drawable.stoke, R.drawable.sunderland, R.drawable.swansea, R.drawable.tottenham, R.drawable.watford,
                R.drawable.west_bromwich, R.drawable.west_ham};

        bundle = new Bundle();
        bundle.putIntArray("logo", logos);
        bundle.putStringArrayList("wins", wins);
        bundle.putStringArrayList("draws",draws);
        bundle.putStringArrayList("losses", losses);
        bundle.putStringArrayList("teams",teams);

        mPager = (ViewPager) v.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager(),bundle);
        new setAdapterTask().execute();
    }

    private class setAdapterTask extends AsyncTask<Void,Void,Void> {
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mPager.setAdapter(mPagerAdapter);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private final Bundle fragmentBundle;
        private boolean start;

        public ScreenSlidePagerAdapter(FragmentManager fm,Bundle data) {
            super(fm);
            fragmentBundle = data;
            start=true;

        }

        @Override
        public ScreenSlidePageFragment getItem(int position) {
            Log.e("check pos ini", "" + position);

            final ScreenSlidePageFragment f = new ScreenSlidePageFragment();
            f.setArguments(this.fragmentBundle);

            fragmentBundle.putInt("page", position);

            return f;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
