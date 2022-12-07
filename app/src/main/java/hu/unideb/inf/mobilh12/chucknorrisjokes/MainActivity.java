package hu.unideb.inf.mobilh12.chucknorrisjokes;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import hu.unideb.inf.mobilh12.chucknorrisjokes.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        binding.getJokeButton.setOnClickListener(button -> getNewJoke());
    }

    private void getNewJoke() {
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = "https://api.chucknorris.io/jokes/random";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                binding.jokeTextView.setText("Response: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
            }
        }
        );

        queue.add(jsonObjectRequest);
    }
}