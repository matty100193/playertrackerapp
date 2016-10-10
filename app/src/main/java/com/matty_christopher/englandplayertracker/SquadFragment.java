package com.matty_christopher.englandplayertracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SquadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SquadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SquadFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;
    private LayoutInflater inflate;

    private OnFragmentInteractionListener mListener;

    public SquadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SquadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SquadFragment newInstance(String param1, String param2) {
        SquadFragment fragment = new SquadFragment();
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
        this.inflate=inflater;
        v=inflater.inflate(R.layout.fragment_squad, container, false);

        EditText searchTeam=(EditText)v.findViewById(R.id.clubEditBox);
        Button button=(Button)v.findViewById(R.id.button);
        button.setOnClickListener(this);

        DatabaseConnection_Fixtures databaseConnection_fixtures=new DatabaseConnection_Fixtures();
        databaseConnection_fixtures.getSquads(new Db_response<JSONArray>() {
            @Override
            public void processFinish(JSONArray output) {
                if(output.length()>0){
                    fillTable(output);
                }
                else{
                    Toast.makeText(getActivity(), "Problem retrieving data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private void fillTable(JSONArray output) {
        TableLayout tableLayout=(TableLayout)v.findViewById(R.id.squads_tables);

        int[] logos={R.drawable.afc_bournemouth,R.drawable.arsenal,R.drawable.burnley,R.drawable.chelsea,R.drawable.crystal_palace,
        R.drawable.everton,R.drawable.hull_city,R.drawable.leicester,R.drawable.liverpool,R.drawable.manchester_city,R.drawable.manchester_united,
        R.drawable.middlesbrough,R.drawable.southampton,R.drawable.stoke,R.drawable.sunderland,R.drawable.swansea,R.drawable.tottenham,R.drawable.watford,
        R.drawable.west_bromwich,R.drawable.west_ham};

        ArrayList<String> wins=new ArrayList<>();
        ArrayList<String> draws=new ArrayList<>();
        ArrayList<String> losses=new ArrayList<>();
        ArrayList<String> teams=new ArrayList<>();
        for (int i=0;i<output.length();i++){
            String teamName="";
            String win="";
            String draw="";
            String lost="";
            try {
                JSONObject jsonobject = output.getJSONObject(i);
                teamName=jsonobject.getString("name");
                win=jsonobject.getString("wins");
                draw=jsonobject.getString("draws");
                lost=jsonobject.getString("lost");
                //Log.e("test team name",teamName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!teams.contains(teamName)){
                teams.add(teamName);
                wins.add(win);
                draws.add(draw);
                losses.add(lost);
            }

        }
        int loops=0;
        int startPos=0;
        long time1=new Date().getTime();
        for(int i=0;i<logos.length;i++){

            View tr = inflate.inflate(R.layout.squadrow, null,false);
            ImageView teamLogo=(ImageView)tr.findViewById(R.id.imageView);
            Drawable logo= ContextCompat.getDrawable(getActivity(),logos[i]);
            teamLogo.setImageDrawable(logo);
            TextView teamname=(TextView)tr.findViewById(R.id.name);
            teamname.setText(teams.get(i));
            TextView record=(TextView)tr.findViewById(R.id.textView3);
            record.setText("wins: " + wins.get(i) + "  draws: " + draws.get(i) + "  lost: " + losses.get(i));
            tableLayout.addView(tr);

            try {
                int currPos=0;
                playerloop:
                for(int j=startPos;j<output.length();j++,currPos++){
                    JSONObject jsonobject = output.getJSONObject(j);
                    String currTeamName=jsonobject.getString("name");
                    String playerName=jsonobject.getString("Name");
                    String position=jsonobject.getString("position");
                    int age=jsonobject.optInt("Age", 0);
                    int played=jsonobject.optInt("played", 0);
                    int goals=jsonobject.optInt("goals", 0);
                    loops++;
                    if(!currTeamName.equals(teams.get(i))){
                        currPos=j;
                        startPos=currPos;
                        //Log.e("check row", currTeamName + " " + teams.get(i) + " " + startPos+" "+j);
                        break playerloop;
                    }
                    else if(!playerName.equals("null")){




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
        long time2=new Date().getTime();
        Log.e("measure Time",""+(time2-time1)+" loops: "+loops);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                Toast.makeText(getActivity(), "Works", Toast.LENGTH_SHORT).show();
                break;
        }
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
}
