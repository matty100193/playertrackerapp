package com.matty_christopher.englandplayertracker;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

class DatabaseConnection_Fixtures {

    private static final String address="http://eziochrist.com/";

    public DatabaseConnection_Fixtures(){

    }


    public void getFixtures(Db_response<ArrayList<ArrayList>> response){
        new getFixturesAsync(response).execute();
    }

    public void getSquads(Db_response<ArrayList<ArrayList<String>>> response){
        Log.e("tag","test before");
        new getSquadsAsync(response).execute();
    }




    public class getFixturesAsync extends AsyncTask<Void,Void,ArrayList<ArrayList>> {

        final Db_response<ArrayList<ArrayList>> response;

        public getFixturesAsync(Db_response<ArrayList<ArrayList>> response){

            this.response=response;
        }



        @Override
        protected void onPostExecute(ArrayList<ArrayList> arrayLists) {
            response.processFinish(arrayLists);
        }

        @Override
        protected ArrayList<ArrayList> doInBackground(Void... params) {



            ArrayList<ArrayList> outer=new ArrayList<>();

            BufferedReader reader = null;
            OutputStreamWriter writer=null;

            HttpURLConnection con = null;
            try {
                URL u = new URL(address+"fixtures.php");
                con = (HttpURLConnection) u.openConnection();
                con.setRequestMethod("GET");
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                if ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                    line = sb.toString();
                    JSONArray jsonArray = new JSONArray(line);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ArrayList<String> inner=new ArrayList<>();
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        String round = jsonobject.getString("round");
                        String home=jsonobject.getString("home");
                        String away=jsonobject.getString("away");
                       // Log.e("Tag", home);
                        inner.add(round);
                        inner.add(home);
                        inner.add(away);
                        outer.add(inner);
                    }
                }

            }
            catch (Exception e) {
                Log.e("Tag", "Explanation of what was being attempted when the exception was thrown", e);
            }
            finally {
                if (con != null) {con.disconnect();}
                if(writer!=null){try {writer.close();} catch (IOException e) {Log.e("Tag", "Explanation of what was being attempted when the exception was thrown", e);}}
                if (reader != null) {try {reader.close();} catch (IOException e) {Log.e("Tag", "Explanation of what was being attempted when the exception was thrown", e);}}
            }


            return outer;
        }
    }

    public class getSquadsAsync extends AsyncTask<Void,Void,ArrayList<ArrayList<String>>> {

        final Db_response<ArrayList<ArrayList<String>>> response;

        public getSquadsAsync(Db_response<ArrayList<ArrayList<String>>> response){

            this.response=response;
        }



        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> arrayLists) {
            response.processFinish(arrayLists);
        }

        @Override
        protected ArrayList<ArrayList<String>> doInBackground(Void... params) {



            ArrayList<ArrayList<String>> outer=new ArrayList<>();

            BufferedReader reader = null;
            OutputStreamWriter writer=null;

            HttpURLConnection con = null;
            try {
                URL u = new URL(address+"squads.php");
                con = (HttpURLConnection) u.openConnection();
                con.setRequestMethod("GET");
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                if ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                    line = sb.toString();
                    JSONArray jsonArray = new JSONArray(line);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ArrayList<String> inner=new ArrayList<>();
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        String clubname = jsonobject.getString("name");
                        String wins=""+jsonobject.getInt("wins");
                        String draws=""+jsonobject.getInt("draws");
                        String losses=""+jsonobject.getInt("lost");
                        String playername = jsonobject.optString("Name", "");
                        String age=""+jsonobject.optInt("Age", -1);
                        String pos = jsonobject.optString("position", "");
                        // Log.e("Tag", home);
                        inner.add(clubname);
                        inner.add(wins);
                        inner.add(draws);
                        inner.add(losses);
                        inner.add(playername);
                        inner.add(age);
                        inner.add(pos);
                        Log.e("tag",clubname);
                        outer.add(inner);
                    }
                }

            }
            catch (Exception e) {
                Log.e("Tag", "Explanation of what was being attempted when the exception was thrown", e);
            }
            finally {
                if (con != null) {con.disconnect();}
                if(writer!=null){try {writer.close();} catch (IOException e) {Log.e("Tag", "Explanation of what was being attempted when the exception was thrown", e);}}
                if (reader != null) {try {reader.close();} catch (IOException e) {Log.e("Tag", "Explanation of what was being attempted when the exception was thrown", e);}}
            }


            return outer;
        }
    }

/*
* modified from http://stackoverflow.com/questions/30740359/namevaluepair-httpparams-httpconnection-params-deprecated-on-server-request-cl,
* author Gagandeep Singh- modified to take in ContentValues
 */

    private String getEncodedData(ContentValues data) {
        StringBuilder sb = new StringBuilder();
        for(String key : data.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(String.valueOf(data.get(key)), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if(sb.length()>0)
                sb.append("&");

            sb.append(key).append("=").append(value);
        }
        return sb.toString();
    }

}

