package hu.unideb.inf.mobilh12.chucknorrisjokes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

import java.util.Locale;

import hu.unideb.inf.mobilh12.chucknorrisjokes.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;

    private TextToSpeech mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        binding.getJokeButton.setOnClickListener(button -> getNewJoke());

        mTTS = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = mTTS.setLanguage(Locale.ENGLISH);

                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported");
                    Toast toast = Toast.makeText(MainActivity.this, "Text to Speech language not supported", Toast.LENGTH_LONG);
                    toast.show();
                }
            } else {
                Log.e("TTS", "Initialization failed");
            }
        });

        //getNewJoke();
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
                        String joke = response.get("value").toString();
                        binding.jokeTextView.setText(joke);
                        speak(joke);
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

    private void speak(String text) {
        mTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
    }

    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }

        super.onDestroy();
    }
}