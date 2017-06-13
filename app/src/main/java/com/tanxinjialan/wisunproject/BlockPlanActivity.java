package com.tanxinjialan.wisunproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlockPlanActivity extends AppCompatActivity {

    private int case_no;
    private String status;
    private String block_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_plan);

        case_no = getIntent().getExtras().getInt("Case_no");
        status = getIntent().getExtras().getString("status");
        block_name = getIntent().getExtras().getString("BlockName");
        String address = getIntent().getExtras().getString("Address");
        String unit_no = getIntent().getExtras().getString("Unit_no");
        String postal_code = getIntent().getExtras().getString("Postal_code");
        int contact = getIntent().getExtras().getInt("Contact");

        final TextView tvBlockName = (TextView) findViewById(R.id.textViewBlockName);
        final TextView tvAddress = (TextView) findViewById(R.id.textViewAddress);
        final TextView tvContact = (TextView) findViewById(R.id.textViewContactNo);
        Button reached_button = (Button) findViewById(R.id.reachButton);

        tvBlockName.setText(block_name);
        tvAddress.setText(address + ", " + unit_no + ", " + postal_code);
        tvContact.setText(String.valueOf(contact));

        String image_name = block_name.replace(" ", "");
        //image_name = block_name.replace("@","");
        Log.i("tag", image_name.toLowerCase() + "bp");

        int id = getResources().getIdentifier(image_name.toLowerCase() + "bp", "drawable", getPackageName());

        ImageView imageView = (ImageView) findViewById(R.id.imageViewFloorPlan);

        Log.i("tag", String.valueOf(id));
        imageView.setImageResource(id);
        //Log.i("tag",String.valueOf(R.drawable.goldenjasminebp));

        reached_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskRunner postReq = new AsyncTaskRunner();
                postReq.execute();
                Intent i_block_plan = new Intent(BlockPlanActivity.this, FloorPlanActivity.class);

                i_block_plan.putExtra("Case_no", case_no);
                status = "REACHED";
                i_block_plan.putExtra("status", status);
                i_block_plan.putExtra("BlockName", block_name);
                startActivity(i_block_plan);
            }
        });

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.1.95:8001/caseupdate");

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
