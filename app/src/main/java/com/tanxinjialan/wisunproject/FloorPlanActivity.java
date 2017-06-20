package com.tanxinjialan.wisunproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FloorPlanActivity extends AppCompatActivity {

    private int case_no;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_plan);

        case_no = getIntent().getExtras().getInt("Case_no");
        status = getIntent().getExtras().getString("status");
        String block_name = getIntent().getExtras().getString("BlockName");

        Button done_button = (Button) findViewById(R.id.doneButton);

        String image_name = block_name.replace(" ", "");

        int id = getResources().getIdentifier(image_name.toLowerCase() + "fp", "drawable", getPackageName());
        ImageView imageView = (ImageView) findViewById(R.id.imageViewFloorPlan);
        imageView.setImageResource(id);

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // If user is done with fire fight
                // Show alert message to reassure to a hardware reset is pressed on the HEMS
                AlertDialog alert = new AlertDialog.Builder(FloorPlanActivity.this).create();
                if (status.equals("REACHED")) {
                    alert.setTitle("RESET");
                    alert.setMessage("Have you reset the Hems Controller?");
                    status = "DONE";
                }
                alert.setTitle("RESET");
                alert.setMessage("Have you reset the Hems Controller?");
                // "YES" Button on alert message
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        status = "DONE";
                        AsyncTaskRunner postReq = new AsyncTaskRunner();
                        postReq.execute();
                        Intent i_floor_plan = new Intent(FloorPlanActivity.this, FirstActivity.class);
                        i_floor_plan.putExtra("Case_no", case_no);
                        i_floor_plan.putExtra("status", status);
                        startActivity(i_floor_plan);
                        //Log.i("Tag", "OK button clicked!");
                    }
                });
                // "NO" Button on alert message
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Log.i("Tag", "Cancel button clicked!");
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            SharedPreferences sharedPref = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://" + sharedPref.getString("server_ip_address", "") + ":" + sharedPref.getString("server_port", "") + "/caseupdate");

            try {
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                nameValuePair.add(new BasicNameValuePair("case_no", String.valueOf(case_no)));
                //Log.i("Test", status);
                nameValuePair.add(new BasicNameValuePair("status", status));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                // Execute HTTP Post Request
                httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}
