package com.tanxinjialan.wisunproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    EditText serverIPAddress;
    EditText serverPort;
    Spinner spinnerDisplayMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences sharedPref = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        serverIPAddress = (EditText) findViewById(R.id.editTextIPAddress);
        serverIPAddress.setText(sharedPref.getString("server_ip_address", "192.168.1.96"));

        serverPort = (EditText) findViewById(R.id.editTextServerPort);
        serverPort.setText(sharedPref.getString("server_port", "8001"));

        spinnerDisplayMap = (Spinner) findViewById(R.id.spinnerMap);
        for (int i = 0; i < spinnerDisplayMap.getCount(); i++) {
            if (spinnerDisplayMap.getItemAtPosition(i).toString().equals(sharedPref.getString("display_map", "Aubergine"))) {
                spinnerDisplayMap.setSelection(i);
                break;
            }
        }

        Button save_button = (Button) findViewById(R.id.buttonSave);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingsActivity.this, FirstActivity.class);
                storeSettings();
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void storeSettings() {
        SharedPreferences sharedPref = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sharedPref.edit();
        sharedEditor.putString("server_ip_address", serverIPAddress.getText().toString());
        sharedEditor.putString("server_port", serverPort.getText().toString());
        sharedEditor.putString("display_map", String.valueOf(spinnerDisplayMap.getSelectedItem()));
        sharedEditor.apply();

        Toast.makeText(SettingsActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
    }

}
