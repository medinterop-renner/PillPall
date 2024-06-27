package com.example.pillpal420.testingNotForRealUse;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Requests {
    public interface FHIRRequestCallback {
        void onResponseReceived(String response);
        void onFailure(Exception e);
    }

    public void getRequest(String getUrl, FHIRRequestCallback callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(getUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure(new IOException("Unexpected code " + response));
                } else {
                    String responseBody = response.body().string();
                    Log.d("Testing","Message sent: "+ responseBody);
                    callback.onResponseReceived(responseBody);
                }
            }
        });
    }
}
