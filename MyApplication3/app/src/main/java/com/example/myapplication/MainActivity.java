package com.example.myapplication;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText location;
    TextView textResult;
    ImageView imageView;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String id = "f442a168deb9982284e09f15fd71071b";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        location =findViewById(R.id.etCity);
        textResult=findViewById(R.id.tvResult);
        imageView=findViewById(R.id.imageView3);
    }

    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = location.getText().toString().trim();
        if (city.equals("")) {
            textResult.setText("City field can not be empty!");
        } else {
            tempUrl = url + "?q=" + city + "&appid=" + id;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;

                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");
                        textResult.setTextColor(Color.rgb(68,134,199));

                        output += "Current weather of " + cityName + " (" + countryName + ")"
                                + "\n Temp: " + df.format(temp) + " °C"
                                + "\n Description: " + description;
                        textResult.setText(output);

                        Resources res = getResources();
                        int resID = res.getIdentifier(description.split(" ")[0], "drawable", getPackageName());
                        Drawable drawable = res.getDrawable(resID );
                        imageView.setImageDrawable(drawable );
                       // int hola= getResources().getIdentifier(description,"drawable",getPackageName());

                        //imageView.setImageResource(hola);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                    textResult.setText("incorrect city");
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

        }


    }

}