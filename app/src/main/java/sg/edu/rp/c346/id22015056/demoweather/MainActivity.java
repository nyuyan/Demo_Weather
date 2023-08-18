package sg.edu.rp.c346.id22015056.demoweather;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
    private AsyncHttpClient client;
    private ListView lvWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvWeather = findViewById(R.id.lvWeather);
        client = new AsyncHttpClient();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Weather> alWeather = new ArrayList<>();
        client.get("https://api.data.gov.sg/v1/environment/2-hour-weather-forecast", new JsonHttpResponseHandler() {
            String area;
            String forecast;

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray jsonArrItems = response.getJSONArray("items");
                    JSONObject firstObj = jsonArrItems.getJSONObject(0);
                    JSONArray jsonArrForecasts = firstObj.getJSONArray("forecasts");
                    for (int i = 0; i < jsonArrForecasts.length(); i++) {
                        JSONObject jsonObjForecast = jsonArrForecasts.getJSONObject(i);
                        area = jsonObjForecast.getString("area");
                        forecast = jsonObjForecast.getString("forecast");
                        Weather weather = new Weather(area, forecast);
                        alWeather.add(weather);
                    }
                    // POINT X – Code to display List View
                    ArrayAdapter<Weather> adapter = new ArrayAdapter<>(
                            MainActivity.this,
                            android.R.layout.simple_list_item_1,
                            alWeather
                    );
                    lvWeather.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
