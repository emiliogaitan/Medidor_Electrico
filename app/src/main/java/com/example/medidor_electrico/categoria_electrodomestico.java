package com.example.medidor_electrico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class categoria_electrodomestico extends AppCompatActivity {
    ListView listItemView;

    // Define string array.
    String[] listItemsValue = new String[]{"Grandes electrodomésticos", "Pequeños electrodomésticos",
            "Equipoa informáticos y telecomunicaciones",
            "Aparatos de alumbrado", "Herramientas eléctricas y eléctronicas",
            "Juguetes y equipos deportivos y de tiempo libre",
            "Aparatos médicos",
            "Intrumentos de medida y control",
            "Maquina expendedoras"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_electrodomestico);

        listItemView = (ListView) findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, listItemsValue);
        listItemView.setAdapter(adapter);

        // ListView setOnItemClickListener function apply here.
        listItemView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), listItemsValue[position], Toast.LENGTH_SHORT).show();
            }
        });

    }
}