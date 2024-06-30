package com.example.pillpal420;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Fragment_nav5 extends Fragment {

    private static final String TAG = "Fragment_nav5";
    private TextView botTextView;
    private EditText botEditText;
    private Button botBtn;
    private OkHttpClient client;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View f5View = inflater.inflate(R.layout.fragment_nav5, container, false);

        botTextView = f5View.findViewById(R.id.f5TextView);
        botEditText = f5View.findViewById(R.id.f5editText);
        botBtn = f5View.findViewById(R.id.f5Button);
        client = new OkHttpClient();

        botBtn.setOnClickListener(v -> sendMessage());

        return f5View;
    }

    private void sendMessage() {
        String message = botEditText.getText().toString();
        botEditText.setText("");
        if (message.isEmpty()) {
            botTextView.setText("Please enter a question.");
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url("http://192.168.0.2:8000/chat")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error sending message: " + e.getMessage());
                getActivity().runOnUiThread(() -> botTextView.setText("Failed to send message."));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Server returned error: " + response.message());
                    getActivity().runOnUiThread(() -> botTextView.setText("Error: " + response.message()));
                } else {
                    String responseBody = response.body().string();


                    Log.d("Testing",responseBody);

                    // Removing escape characters and decoding Unicode characters
                    responseBody = responseBody.replace("\\", "");
                    responseBody = responseBody.replace("\n", "");
                    responseBody = responseBody.replace("\"","");

                    // Decoding Unicode escape sequences

                    String finalResponseBody = responseBody;
                    getActivity().runOnUiThread(() -> botTextView.setText(finalResponseBody));


                   /* try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String gptReply = jsonResponse.getString("response");
                        getActivity().runOnUiThread(() -> botTextView.setText(gptReply));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        getActivity().runOnUiThread(() -> botTextView.setText("Failed to parse response."));
                    }*/
                }
            }
        });
    }

}