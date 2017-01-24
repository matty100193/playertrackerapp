package com.matty_christopher.englandplayertracker;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.HashMap;


public class ResultsSlidePageFragment extends Fragment {


    private TableLayout tableLayout;
    private LayoutInflater inflate;
    private String hometeam;
    private String awayteam;
    private int homescore,awayscore;
    HashMap<String,Integer> teamLogos;
    public static ResultsSlidePageFragment newInstance(int round,String homeTeam,String awayTeam,int homeScore,int awayScore){
        //Log.e("tag",homeTeam);
        ResultsSlidePageFragment fragment=new ResultsSlidePageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("round", round);
        bundle.putString("homeTeam", homeTeam);
        bundle.putString("awayTeam", awayTeam);
        bundle.putInt("homeScore", homeScore);
        bundle.putInt("awayScore", awayScore);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ResultsSlidePageFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hometeam=getArguments().getString("homeTeam");
        awayteam=getArguments().getString("awayTeam");
        homescore=getArguments().getInt("homeScore");
        awayscore=getArguments().getInt("awayScore");
        teamLogos=new HashMap<>();
        teamLogos.put("AFC Bournemouth",R.drawable.afc_bournemouth);
        teamLogos.put("Arsenal",R.drawable.arsenal);
        teamLogos.put("Burnley",R.drawable.burnley);
        teamLogos.put("Chelsea",R.drawable.chelsea);
        teamLogos.put("Crystal Palace",R.drawable.crystal_palace);
        teamLogos.put("Everton",R.drawable.everton);
        teamLogos.put("Hull City",R.drawable.hull_city);
        teamLogos.put("Leicester City",R.drawable.leicester);
        teamLogos.put("Liverpool",R.drawable.liverpool);
        teamLogos.put("Manchester City",R.drawable.manchester_city);
        teamLogos.put("Manchester United",R.drawable.manchester_united);
        teamLogos.put("Middlesbrough",R.drawable.middlesbrough);
        teamLogos.put("Southampton",R.drawable.southampton);
        teamLogos.put("Stoke City",R.drawable.stoke);
        teamLogos.put("Sunderland",R.drawable.sunderland);
        teamLogos.put("Swansea City",R.drawable.swansea);
        teamLogos.put("Tottenham Hotspur",R.drawable.tottenham);
        teamLogos.put("Watford",R.drawable.watford);
        teamLogos.put("West Bromwich Albion",R.drawable.west_bromwich);
        teamLogos.put("West Ham United",R.drawable.west_ham);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        tableLayout=(TableLayout)rootView.findViewById(R.id.squads_tables);
        inflate=inflater;
        fillTable();

        return rootView;
    }

    private void fillTable() {

        View tr = inflate.inflate(R.layout.match_row, null,false);
        ImageView homeLogo=(ImageView)tr.findViewById(R.id.homeLogo);
        Integer logoDrawable=teamLogos.get(hometeam);
        Drawable logoDraw= ContextCompat.getDrawable(getActivity(), logoDrawable);
        homeLogo.setImageDrawable(logoDraw);
        ImageView awayLogo=(ImageView)tr.findViewById(R.id.awayLogo);
        logoDrawable=teamLogos.get(awayteam);
        logoDraw= ContextCompat.getDrawable(getActivity(), logoDrawable);
        awayLogo.setImageDrawable(logoDraw);
        TextView homeScoreTxt=(TextView)tr.findViewById(R.id.scoreHome);
        homeScoreTxt.setText(""+homescore);
        TextView awayScoreTxt=(TextView)tr.findViewById(R.id.scoreAway);
        awayScoreTxt.setText(""+awayscore);
        tableLayout.addView(tr);
    }
}
