package cn.solarmoon.toy_home.core.data.serializer;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FortuneQuote {

    public static String[] QUOTES = new String[] {};

    @SerializedName("quotes")
    public String[] quotes;

    public static String[] get() {
        return QUOTES;
    }

}
