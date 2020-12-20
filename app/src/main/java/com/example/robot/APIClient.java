package com.example.robot;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient extends Application {

    private static APIInterface umoriliApi;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://korolevmalyshevmp.ddns.net:10055/") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        umoriliApi = retrofit.create(APIInterface.class); //Создаем объект, при помощи которого будем выполнять запросы
    }

    public static APIInterface getApi() {
        return umoriliApi;
    }
}