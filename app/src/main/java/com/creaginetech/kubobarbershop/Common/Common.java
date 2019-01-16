package com.creaginetech.kubobarbershop.Common;

import com.creaginetech.kubobarbershop.Remote.APIService;
import com.creaginetech.kubobarbershop.Remote.RetrofitClient;

public class Common {

    public static final int PICK_IMAGE_REQUEST = 71;

    public static String orderSelected = "";
    public static String barbermanSelected = "";
    public static String serviceSelected = "";
    public static String currentUser = "";

    private static final String BASE_URL = "https://fcm.googleapis.com/";

    public static APIService getFCMService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

}
