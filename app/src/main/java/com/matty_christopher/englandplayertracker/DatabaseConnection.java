package com.matty_christopher.englandplayertracker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
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

import dmax.dialog.SpotsDialog;

class DatabaseConnection {

    private static final String address="http://eziochrist.com/";
    private AlertDialog dialog;

    public DatabaseConnection(Context context){
        dialog=new SpotsDialog(context,R.style.Custom_dialog);
        dialog.show();
        dialog.setCancelable(false);
    }


    public void getFixtures(Db_response<ArrayList<ArrayList>> response){
        new getFixturesAsync(response).execute();
    }

    public void getSquads(Db_response<JSONArray> response){
        Log.e("tag","test before");
        new getSquadsAsync(response).execute();
    }

    public void getMatches(Db_response<JSONArray> response){
        new getMatchesAsync(response).execute();
    }




    public class getFixturesAsync extends AsyncTask<Void,Void,ArrayList<ArrayList>> {

        final Db_response<ArrayList<ArrayList>> response;

        public getFixturesAsync(Db_response<ArrayList<ArrayList>> response){

            this.response=response;
        }



        @Override
        protected void onPostExecute(ArrayList<ArrayList> arrayLists) {
            dialog.dismiss();
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

    public class getSquadsAsync extends AsyncTask<Void,Void,JSONArray> {

        final Db_response<JSONArray> response;

        public getSquadsAsync(Db_response<JSONArray> response){

            this.response=response;
        }



        @Override
        protected void onPostExecute(JSONArray arrayLists) {
            dialog.dismiss();
            response.processFinish(arrayLists);
        }

        @Override
        protected JSONArray doInBackground(Void... params) {



            JSONArray outer=new JSONArray();

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
                    outer=jsonArray;
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

    private class getMatchesAsync extends AsyncTask<Void,Void,JSONArray> {

        private Db_response<JSONArray> response;

        public getMatchesAsync(Db_response<JSONArray> response) {
            this.response=response;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            dialog.dismiss();
            response.processFinish(jsonArray);
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            JSONArray results=new JSONArray();
            BufferedReader reader = null;
            OutputStreamWriter writer=null;

            HttpURLConnection con = null;
            try {
                URL u = new URL(address+"results.php");
                con = (HttpURLConnection) u.openConnection();
                con.setRequestMethod("GET");
                //con.setConnectTimeout(1000);
                //con.setReadTimeout(1000);
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                if ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                    line = sb.toString();
                    JSONArray jsonArray = new JSONArray(line);
                    results=jsonArray;
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
            return  results;

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

