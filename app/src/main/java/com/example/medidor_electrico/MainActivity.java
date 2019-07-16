package com.example.medidor_electrico;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static String var_user;
    public static String var_pass;
    EditText user;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar bar = getSupportActionBar();
        if (getSupportActionBar() == null) {

        } else {
            getSupportActionBar().hide();
        }

        String[] archivos = fileList();
        if (existe(archivos, "datos.dll")) {
            try {
                InputStreamReader archivo = new InputStreamReader(
                        openFileInput("datos.dll"));
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
                    Intent intent = new Intent(MainActivity.this, intronext.class);
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
                    "datos.dll", MainActivity.MODE_PRIVATE));
            archivo.write(v);
            archivo.flush();
            archivo.close();
        } catch (IOException e) {
        }
        finish();
    }

    public void Clickbtn(View view) {
        user = findViewById(R.id.textusuario);
        String valor_uno = user.getText().toString();

        pass = findViewById(R.id.textpass);
        String valor_dos = pass.getText().toString();

        if (valor_uno.equals("")) {
            user.setError("Ingresar Usuario");
        } else if (valor_dos.equals("")) {
            pass.setError("Ingresar Contraseña");
        } else {
            //Quitar eso arriba para verificar
            var_user = valor_uno;
            var_pass = valor_dos;
            ejecutarServices("http://192.168.43.153/arduino/ingresar.php");
        }
    }

    private void ejecutarServices(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("no")) {
                    Toast.makeText(getApplicationContext(), "Usuario y Contraseña? No valido", Toast.LENGTH_SHORT).show();
                } else {
                    grabar(response);
                    Intent intent = new Intent(MainActivity.this, intronext.class);
                    finish();
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Conexion", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametross = new HashMap<String, String>();
                parametross.put("user", var_user.toString());
                parametross.put("pass", var_pass.toString());
                return parametross;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void btnrestablecerOnclik(View view) {
        Intent intent = new Intent(this, restablecer_user.class);
        startActivity(intent);
    }
}
