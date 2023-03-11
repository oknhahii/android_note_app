package com.example.note_app.activity

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.note_app.R
import java.text.SimpleDateFormat

class SplashScreen : AppCompatActivity() {
    //    Bien quan ly database
    lateinit var db: SQLiteDatabase;
    //    con tro quan ly truy van trong db;
    lateinit var rs: Cursor;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//      hide bar
        supportActionBar?.hide();

//      Go to splash screen in 2000 s
        Handler(Looper.getMainLooper()!!).postDelayed({
            val preference = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
            val ranVar: String = preference.getString("ran","").toString();

            if(ranVar.equals("Yes")){
                startActivity(Intent(this, MainActivity::class.java))
            }else{
                val editor = preference.edit();
                editor.putString("ran","Yes");
                editor.apply();
                startActivity(Intent(this, onboardingScreen::class.java))
            }
        },5000)

    }
}