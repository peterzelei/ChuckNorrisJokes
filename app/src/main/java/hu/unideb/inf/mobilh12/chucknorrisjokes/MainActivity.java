package hu.unideb.inf.mobilh12.chucknorrisjokes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

import hu.unideb.inf.mobilh12.chucknorrisjokes.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;

    private ObservableList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categories = new ObservableArrayList<String>();
        
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
                    binding.jokeTextView.setText("An error occured...");
            Log.e("getNewJoke", "Error while fetching JSON data from API");
        }
        );

        queue.add(jsonObjectRequest);
    }
}