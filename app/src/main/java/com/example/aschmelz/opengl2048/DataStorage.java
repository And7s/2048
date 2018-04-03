package com.example.aschmelz.opengl2048;

/**
 * Created by aschmelz on 01/04/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by aschmelz on 12/03/2018.
 */


/**
 * Created by aschmelz on 21/12/2017.
 */

public class DataStorage {
    private static String KEY = "com.example.aschmelz.opengl2048";

    private SharedPreferences sharedPref;

    public DataStorage(Context context) {
        sharedPref = context.getSharedPreferences( KEY + ".A", Context.MODE_PRIVATE);
        read();
    }

    public void write() {
        write(false);
    }

    public void write(boolean delay) {

        SharedPreferences.Editor editor = sharedPref.edit();

       // editor.putLong("money", Double.doubleToRawLongBits(gs.money));



    }

    public void read() {
        //double money = Double.longBitsToDouble(sharedPref.getLong("money", Double.doubleToLongBits(0)));

        //Log.d("dedid read", money + "");
    }
}
