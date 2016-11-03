package com.matty_christopher.englandplayertracker;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Results_Fragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private OnFragmentInteractionListener mListener;
    private Spinner dropdown;

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
        View view= inflater.inflate(R.layout.fragment_results_, container, false);
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

        Set<Integer>rounds=new TreeSet<>();
        for(int i=0;i<output.length();i++){
            try {
                JSONObject jsonobject = output.getJSONObject(i);
                rounds.add(jsonobject.getInt("round"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        List<Integer> list_rounds=new ArrayList<>(rounds);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, list_rounds);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);

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
        //Toast.makeText(parent.getContext(), ""+parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
