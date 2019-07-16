package com.example.medidor_electrico;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class intronext extends AppCompatActivity {
    ImageView uno;
    TextView dos;
    TextView tres;
    public static int contador=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intronext);
        ActionBar bar = getSupportActionBar();
        if (getSupportActionBar() == null) {

        } else {
            getSupportActionBar().hide();
        }
        String[] archivos = fileList();
        if (existe(archivos, "mensaje.dll")) {
            try {
                InputStreamReader archivo = new InputStreamReader(
                        openFileInput("mensaje.dll"));
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                String todo = "";
                while (linea != null) {
                    todo = todo + linea + "\n";
                    linea = br.readLine();
                }
                br.close();
                archivo.close();
                if (todo.equals("")) {

                } else {
                    Intent intent = new Intent(this, consumo.class);
                    startActivity(intent);
                }
            } catch (IOException e) {

            }
        }
    }
    private boolean existe(String[] archivos, String archbusca) {
        for (int f = 0; f < archivos.length; f++) {
            if (archbusca.equals(archivos[f])) {
                return true;
            }
        }
        return false;
    }

    public void grabar(String v) {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(
                    "mensaje.dll", MainActivity.MODE_PRIVATE));
            archivo.write(v);
            archivo.flush();
            archivo.close();
        } catch (IOException e) {
        }
        finish();
    }

    public void btnclick(View view) {
        contador++;
        uno=findViewById(R.id.imagen101);
        dos=findViewById(R.id.titulo101);
        tres=findViewById(R.id.mensaje101);

        if(contador==0){

        }else if(contador==1){
            uno.setImageResource(R.mipmap.banner2);
            dos.setText("Conectado a Internet");
            tres.setText("Consulta desde cualquier sitio toda la información " +
                    "de tu consumo eléctrico. Mirubee vigila que todo vaya bien.");
        }else if(contador==2){
            uno.setImageResource(R.mipmap.banner4);
            dos.setText("Actúa y ahorra");
            tres.setText("Descubre oportunidades de ahorro reales y optimiza el uso" +
                    " de electricidad en tu casa. Elige la tarifa eléctrica más adecuada a tu perfil.");
        }else{
            grabar("si");
            Intent intent = new Intent(this, consumo.class);
            finish();
            startActivity(intent);
        }
    }
}
