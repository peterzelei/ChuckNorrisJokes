package hu.unideb.inf.mobilh12.chucknorrisjokes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

import hu.unideb.inf.mobilh12.chucknorrisjokes.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        binding.getJokeButton.setOnClickListener(button -> getNewJoke());

        getNewJoke();
    }

    private void getNewJoke() {
        String url = "https://api.chucknorris.io/jokes/random";

        String selectedCategory = binding.categoriesSpinner.getSelectedItem().toString();
        if (!selectedCategory.equals("any")) {
            url = url + "?category=" + selectedCategory;
        }

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, response -> {
                    try {
                        binding.jokeTextView.setText(response.get("value").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Toast toast = Toast.makeText(this,"An error occured...", Toast.LENGTH_LONG);
                    toast.show();
            Log.e("getNewJoke", "Error while fetching JSON data from API");
        }
        );

        queue.add(jsonObjectRequest);
    }
}