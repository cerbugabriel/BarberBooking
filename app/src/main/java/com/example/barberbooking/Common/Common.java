package com.example.barberbooking.Common;

import com.example.barberbooking.Model.Salon;
import com.example.barberbooking.Model.User;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXY = "ENABLE_BUTTON_NEXT";
    public static final String KEY_SALON_STORE = "SALON_SAVE";
    public static final String KEY_BARBER_LOAD_DONE = "BARBER_LOAD_DONE";
    public static String IS_LOGIN = "IsLogin";

    public static User currentUser;
    public static Salon currentSalon;
    public static int step = 0; //Init first step is 0
    public static String city="";
}
