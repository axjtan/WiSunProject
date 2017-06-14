package com.tanxinjialan.wisunproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
                AsyncTaskRunner postReq = new AsyncTaskRunner();
                postReq.execute();
                Intent i_floor_plan = new Intent(FloorPlanActivity.this, FirstActivity.class);

                i_floor_plan.putExtra("Case_no", case_no);
                status = "DONE";
                i_floor_plan.putExtra("status", status);
                startActivity(i_floor_plan);
            }
        });

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.1.96:8001/caseupdate");

            try {
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                nameValuePair.add(new BasicNameValuePair("case_no", String.valueOf(case_no)));
                Log.i("Test", status);
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
