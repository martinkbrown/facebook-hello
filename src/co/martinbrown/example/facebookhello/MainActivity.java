package co.martinbrown.example.facebookhello;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class MainActivity extends Activity {

    public Facebook facebook = new Facebook("386944074698248");
    AsyncFacebookRunner mAsyncRunner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAsyncRunner = new AsyncFacebookRunner(facebook);

        facebook.authorize(this, new String[] {"email", "user_work_history", "user_education_history","friends_photos"}, new DialogListener() {

            @Override
            public void onComplete(Bundle values) { }

            @Override
            public void onFacebookError(FacebookError error) { }

            @Override
            public void onError(DialogError e) { }

            @Override
            public void onCancel() { }
        });
    }

    private void getUsername() {

        mAsyncRunner.request("me", new RequestListener() {

            @Override
            public void onMalformedURLException(MalformedURLException e, Object state) {
                Log.i("facebook","malformed");
            }

            @Override
            public void onIOException(IOException e, Object state) {
                Log.i("facebook","IO");
            }

            @Override
            public void onFileNotFoundException(FileNotFoundException e, Object state) {
            }

            @Override
            public void onFacebookError(FacebookError e, Object state) {
                Log.i("facebook","error");
            }

            @Override
            public void onComplete(final String responseString, Object state) {

                sayHello(responseString);

            }

        });
    }

    private void sayHello(String responseString) {

        try {
            final TextView textHello = (TextView) findViewById(R.id.textHello);

            JSONObject response = new JSONObject(responseString);
            final String name = response.getString("name");

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    textHello.setText("Hello, " + name);
                }
            });
        }
        catch(JSONException e) {}

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
}
