package com.example.medidor_electrico;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class categoria_electrodomestico extends AppCompatActivity {
    //    ListView listItemView;
//    // Define string array.
//    String[] listItemsValue = new String[]{"Grandes electrodomésticos", "Pequeños electrodomésticos",
//            "Equipoa informáticos y telecomunicaciones",
//            "Aparatos de alumbrado", "Herramientas eléctricas y eléctronicas",
//            "Juguetes y equipos deportivos y de tiempo libre",
//            "Aparatos médicos",
//            "Intrumentos de medida y control",
//            "Maquina expendedoras"};
// Array of strings for ListView Title

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_electrodomestico);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 5000);

    }
}