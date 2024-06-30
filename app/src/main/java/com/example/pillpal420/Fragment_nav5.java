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

import com.example.pillpal420.documentation.LogTag;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Diese Klasse wird verwendet um mit einem eigens trainiertem AI model zu kommunizieren das die Funktion eines HelpDesks hat.
 * Für 1st level customer support. Es wird über ein Python Server ein LLM angesprochen die speziell darauf trainiert ist.
 */
public class Fragment_nav5 extends Fragment {
    private String serverAddress = "192.168.0.2:8000";
    private TextView botTextView;
    private EditText botEditText;
    private Button botBtn;
    private OkHttpClient client;

    /**
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return view null or
     */
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

    /**
     * Sendet die Nachricht die vom User eingegeben wurde zu einem Python/Flask server.
     */
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
                .url("http://" + serverAddress + "/chat")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LogTag.CHAT_BOT.getTag(), "Error sending message: " + e.getMessage());
                getActivity().runOnUiThread(() -> botTextView.setText("Failed to send message."));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(LogTag.CHAT_BOT.getTag(), "Server returned error: " + response.message());
                    getActivity().runOnUiThread(() -> botTextView.setText("Error: " + response.message()));
                } else {
                    String responseBody = response.body().string();
                    Log.d(LogTag.CHAT_BOT.getTag(), responseBody);
                    responseBody = responseBody.replace("\\", "");
                    responseBody = responseBody.replace("\n", "");
                    responseBody = responseBody.replace("\"", "");
                    String finalResponseBody = responseBody;
                    getActivity().runOnUiThread(() -> botTextView.setText(finalResponseBody));
                }
            }
        });
    }

}