package com.example.social_gift.dao;

import android.view.textclassifier.ConversationActions;

import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DaoImage {
    private final String url = "https://balandrau.salle.url.edu/i3/repositoryimages/uploadfile";

    public DaoImage() {
    }

    public interface DaoRepositoryImagesListener {
        void onSuccess(String url);
        void onError(String errorMessage);
    }
    public void uploadFile(File image, DaoRepositoryImagesListener listener) {
        String urlFinal;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("myFile", image.getName(),
                                RequestBody.create(new File(image.getAbsolutePath()),
                                        MediaType.parse("image/png")))
                        .build();
                try {
                    Request request = new Request.Builder()
                            .url(url)
                            .method("POST", body)
                            .build();
                    Response response = client.newCall(request).execute();
                    JSONObject jsonObject =new JSONObject(response.body().string());
                    JSONObject data = jsonObject.getJSONObject("data");
                    if(data.getString("type").equals("success")){
                        String urlImage = data.getString("url");
                        listener.onSuccess(urlImage);
                    }
                } catch (Exception e) {
                    listener.onError(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
}
