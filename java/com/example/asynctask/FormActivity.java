package com.example.asynctask;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;

public class FormActivity extends AppCompatActivity {
    private ExecutorService executorService;
    private EditText imieInput, opisInput, cenaInput;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form);

        imieInput = findViewById(R.id.inputName);
        opisInput = findViewById(R.id.inputDescription);
        cenaInput = findViewById(R.id.inputPrice);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject newEntry = new JSONObject();
                    newEntry.put("name", imieInput.getText().toString());
                    newEntry.put("desc", opisInput.getText().toString());
                    newEntry.put("price", cenaInput.getText().toString());

                    updateJsonArray(newEntry);
                    Log.d("TAG", "onClick: " + "dodano" + newEntry);
                    finish();
                } catch (Exception e) {
                    Log.e("TAG", "onClick: Błąd formularza" + e.getMessage());
                }
            }
        });

    }

    private void updateJsonArray(JSONObject newEntry) {
        try {


            JSONArray jsonArray = loadJSONArray();

            jsonArray.put(newEntry);
            saveJsonArray(jsonArray);
        } catch (Exception e) {
            Log.e("TAG", "updateJsonArray: " + e.getMessage());

        }
    }

    private JSONArray loadJSONArray() {
        try {
            InputStream inputStream = openFileInput("data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer);
            return new JSONArray(json);
        } catch (Exception e) {
            Log.e("FormActivity", "Error loading JSON: " + e.getMessage());
            return null;
        }
    }

    private void saveJsonArray(JSONArray jsonArray) {
        try (FileOutputStream fos = openFileOutput("data.json", Context.MODE_PRIVATE)) {
            fos.write(jsonArray.toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            Log.e("FormActivity", "Error saving JSON: " + e.getMessage());
        }
    }
}