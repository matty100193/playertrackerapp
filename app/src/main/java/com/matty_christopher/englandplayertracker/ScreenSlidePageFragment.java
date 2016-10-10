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
    public static final String ARG_PAGE = "page";
    private int mPageNumber;
    private static JSONArray data;
    private TableLayout tableLayout;
    private static int[] logo;
    private LayoutInflater inflate;
    private static ArrayList<String> wins;
    private static ArrayList<String> draws;
    private static ArrayList<String> losses;
    private static ArrayList<String> teams;


    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPageNumber = getArguments().getInt("ARG_PAGE");
        mPageNumber=getArguments().getInt("page");
        logo = getArguments().getIntArray("logo");
        wins= getArguments().getStringArrayList("wins");
        draws= getArguments().getStringArrayList("draws");
        losses= getArguments().getStringArrayList("losses");
        teams= getArguments().getStringArrayList("teams");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        tableLayout=(TableLayout)rootView.findViewById(R.id.squads_tables);
        inflate=inflater;
        fillTable();
        Log.e("check pos inner", "" + mPageNumber);

        return rootView;
    }

    private void fillTable() {
        if (teams.size() > 0) {

            View tr = inflate.inflate(R.layout.squadrow, null,false);
            ImageView teamLogo=(ImageView)tr.findViewById(R.id.imageView);
            Drawable logoDraw= ContextCompat.getDrawable(getActivity(), logo[mPageNumber]);
            teamLogo.setImageDrawable(logoDraw);
            TextView teamname=(TextView)tr.findViewById(R.id.name);
            teamname.setText(teams.get(mPageNumber ));
            TextView record=(TextView)tr.findViewById(R.id.textView3);
            record.setText("wins: " + wins.get(mPageNumber ) + "  draws: " + draws.get(mPageNumber ) + "  lost: " + losses.get(mPageNumber ));
            tableLayout.addView(tr);


        }

        for (int i = 0; i < 20; i++) {
            TableRow tr1 = new TableRow(getActivity());
            TextView txt = new TextView(getActivity());
            txt.setText("" + i);
            tr1.addView(txt);
            tr1.setMinimumHeight(200);
            if (i % 2 == 0) {
                tr1.setBackgroundColor(Color.rgb(226, 244, 66));
            }
            tableLayout.addView(tr1);
        }
    }

    public int getPageNumber() {
        return mPageNumber;
    }
}