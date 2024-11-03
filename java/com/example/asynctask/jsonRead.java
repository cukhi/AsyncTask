package com.example.asynctask;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class jsonRead {

    public String textValue;
    public int max;
    private ExecutorService executorService;


    Context context;
    jsonRead(Context context){
    this.context = context;
    this.executorService = Executors.newSingleThreadExecutor();


    }


    public void  loadJson( Context context, LinearLayout lin) {

    Runnable runnableTask = () -> {


        try {
            //sleep na 2 sekundy
            TimeUnit.MILLISECONDS.sleep(2000);


            InputStream inputStream = context.getAssets().open("data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            //fetching
            String json, name, desc, suma = "";
            int maxLocal;
            float price;

            json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);
            maxLocal = jsonArray.length();
            lin.removeAllViews();
            max = maxLocal;
            //iteracja i pobranie wartosci
            lin.removeAllViews();
            for (int i = 0; i < maxLocal; i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                name = jsonObject.getString("name");
                desc = jsonObject.getString("desc");
                price = jsonObject.getInt("price");

                String res = ("name: " + name + " " + '\n' + "Price: " + price + " " + '\n' + "Description: " + desc + " " + '\n');
                Log.d("TAG", "res: " + res);
                // Tworzenie dynamiczne TextView
                Log.d("TAG", "Adding TextView for item " + i + ": " + res);
                TextView myText = new TextView(context);
                myText.setText(res);
                myText.setTextSize(16);
                myText.setPadding(16, 16, 16, 16);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                lin.addView(myText);

            }


        } catch (Exception e) {
            Log.e("TAG", "Error loading JSON: " + e.getMessage());
        }

    };
    //wywolanie executora
        executorService.execute(runnableTask);
        Future<String> result = executorService.submit(runnableTask, "DONE");
    //sprawdzanie poprawnosci (error handling)
        while(!result.isDone()){
            try{
                System.out.println("zwrocono: " + result.get());
                break;
            }
            catch(InterruptedException | ExecutionException e){
                e.printStackTrace();

            }
            try{
                Thread.sleep(1000L);

            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        //zamkniecie wielowątkowści
        executorService.shutdown();
    }
    }















