package com.example.medidor_electrico;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class restablecer_user extends AppCompatActivity {
    public static EditText uno;
    public static EditText dos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restablecer_user);
        ActionBar bar = getSupportActionBar();
        if (getSupportActionBar() == null) {

        } else {
            getSupportActionBar().hide();
        }
    }

    public void btnrecuperar(View view) {
        uno = findViewById(R.id.editext111);
        dos = findViewById(R.id.editext112);
        String varString1 = uno.getText().toString();
        String varString2 = dos.getText().toString();
        if (varString1.equals("")) {
            uno.setError("Ingresar Opcion");
        } else if (varString2.equals("")) {
            dos.setError("Ingresar Opcion");
        } else {
            ejecutarServices("http://www.orthodentalnic.com/arduino/recuperarcuenta.php");
        }

    }

    private void ejecutarServices(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("si")) {
                    Toast.makeText(getApplicationContext(), "Datos enviado al correo", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Datos no Existe", Toast.LENGTH_SHORT).show();
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
                uno = findViewById(R.id.editext111);
                dos = findViewById(R.id.editext112);
                parametross.put("indmedidor", uno.getText().toString());
                parametross.put("user", dos.getText().toString());
                return parametross;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onclick(View view) {
        Intent intent1 = new Intent(restablecer_user.this, MainActivity.class);
        startActivity(intent1);
    }
}
