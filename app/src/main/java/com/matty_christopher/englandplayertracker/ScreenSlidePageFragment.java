package com.matty_christopher.englandplayertracker;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScreenSlidePageFragment extends Fragment {
    private JSONArray data;
    private TableLayout tableLayout;
    private LayoutInflater inflate;
    private String wins;
    private String draws;
    private String losses;
    private String teams;
    private int logo;

    public static ScreenSlidePageFragment newInstance(int logos,String win,String draw,String loss,String team,String output) {
        ScreenSlidePageFragment fragmentFirst = new ScreenSlidePageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("logo", logos);
        bundle.putString("wins", win);
        bundle.putString("draws", draw);
        bundle.putString("losses", loss);
        bundle.putString("teams",team);
        bundle.putString("data", output);
        fragmentFirst.setArguments(bundle);
        return fragmentFirst;
    }


    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logo = getArguments().getInt("logo");
        wins= getArguments().getString("wins");
        draws= getArguments().getString("draws");
        losses= getArguments().getString("losses");
        teams= getArguments().getString("teams");
        String stringData= getArguments().getString("data");
        try {
            data=new JSONArray(stringData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        tableLayout=(TableLayout)rootView.findViewById(R.id.squads_tables);
        inflate=inflater;
        fillTable();

        return rootView;
    }

    private void fillTable() {
        if (teams.length() > 0) {

            View tr = inflate.inflate(R.layout.squadrow, null,false);
            ImageView teamLogo=(ImageView)tr.findViewById(R.id.imageView);
            Drawable logoDraw= ContextCompat.getDrawable(getActivity(), logo);
            teamLogo.setImageDrawable(logoDraw);
            TextView teamname=(TextView)tr.findViewById(R.id.name);
            teamname.setText(teams);
            TextView record=(TextView)tr.findViewById(R.id.textView3);
            record.setText("wins: " + wins + "  draws: " + draws + "  lost: " + losses);
            tableLayout.addView(tr);

            try {
                int currPos=0;
                boolean passed=false;
                playerloop:
                for(int j=0;j<data.length();j++,currPos++){
                    JSONObject jsonobject = data.getJSONObject(j);
                    String currTeamName=jsonobject.getString("name");
                    String playerName=jsonobject.getString("Name");
                    String position=jsonobject.getString("position");
                    int age=jsonobject.optInt("Age", 0);
                    int played=jsonobject.optInt("played", 0);
                    int goals=jsonobject.optInt("goals", 0);
                    if(!currTeamName.equals(teams) && passed){

                        break playerloop;
                    }
                    else if(currTeamName.equals(teams) && !playerName.equals("null")){
                        if(!passed){
                            passed=true;
                        }



                        View tr_player = inflate.inflate(R.layout.player_row, null,false);
                        TextView currPlayer=(TextView)tr_player.findViewById(R.id.name_txt);
                        currPlayer.setText(playerName);

                        TextView currPosiotion=(TextView)tr_player.findViewById(R.id.textView2);
                        currPosiotion.setText(position);

                        TextView currAge=(TextView)tr_player.findViewById(R.id.agetxt);
                        currAge.setText("Age: "+age);

                        TextView currMatches=(TextView)tr_player.findViewById(R.id.matchesTxt);
                        currMatches.setText("Matches: "+played);

                        TextView currGoals=(TextView)tr_player.findViewById(R.id.textView4);
                        currGoals.setText("Goals: "+goals);

                        tableLayout.addView(tr_player);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }




    }

}