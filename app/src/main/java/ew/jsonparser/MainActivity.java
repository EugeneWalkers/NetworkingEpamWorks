package ew.jsonparser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {


    class MyAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                final URL url = new URL("http://ip.jsontest.com/?callback=showMyIP");
                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(5000);
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int resonseCode = connection.getResponseCode();

                if (resonseCode != HttpsURLConnection.HTTP_OK) {

                }
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    StringBuffer stringBuffer = new StringBuffer();
                    try {
                        inputStream = new BufferedInputStream(connection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        String inputLine = "";
                        while ((inputLine = reader.readLine()) != null) {
                            stringBuffer.append(inputLine);
                        }
                       return stringBuffer.toString();
                    } catch (Exception e) {
//handle an exception
                    } finally {  //close inputStream }
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ((TextView) findViewById(R.id.textView)).setText(result);
            StringBuffer buffer = new StringBuffer(result);
            buffer.replace(0,
                    buffer.length(),
                    buffer.substring(
                    result.indexOf("{"),
                    result.indexOf("}") + 1)
            );
            try {
                final JSONObject object = new JSONObject(buffer.toString());
                ((TextView) findViewById(R.id.textView2)).setText("Our IP: " + object.getString("ip"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MyAsyncTask().execute();
    }
}
