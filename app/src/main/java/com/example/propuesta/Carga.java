package com.example.propuesta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.propuesta.ui.profile.ProfileFragment;

public class Carga extends AppCompatActivity {

    TextView app_name, dev;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carga);

        app_name = findViewById(R.id.app_name);
        dev = findViewById(R.id.dev);

        final  int DURACION = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent( Carga.this, MainActivity.class );
                startActivity(intent);
                finish();
            }
        }, DURACION);
}
}
