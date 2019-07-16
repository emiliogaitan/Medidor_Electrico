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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class configure_user_datos extends AppCompatActivity {
    RequestQueue requestQueue;
    String medidor;

    public EditText nombre;
    public String valor1 = "";
    public EditText nmedidor;
    public String valor2 = "";
    public EditText telefono;
    public String valor3 = "";
    public EditText direccion;
    public String valor4 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_user_datos);
        if(savedInstanceState==null){
            Bundle extras=getIntent().getExtras();
            if(extras==null){
                medidor=null;
            }else{
                medidor=extras.getString("medidor");
            }
        }else{
            medidor=(String)savedInstanceState.getSerializable("medidor");
        }
        datos_totales("http://orthodentalni.com/arduino/todo_generales.php");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_regresar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemregresar:
                Intent intent3 = new Intent(this, consumo.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void enviardatos(View view) {
        nombre = findViewById(R.id.Editext202);
        valor1 = nombre.getText().toString();
        nmedidor = findViewById(R.id.Editext203);
        valor2 = nmedidor.getText().toString();
        telefono = findViewById(R.id.Editext204);
        valor3 = telefono.getText().toString();
        direccion = findViewById(R.id.Editext205);
        valor4 = direccion.getText().toString();
        if (valor1.equals("")) {
            nombre.setError("Ingresar Nombre");
        }

        if (valor2.equals("")) {
            nmedidor.setError("Ingresar No medidor");
        }
        if (valor3.equals("")) {
            telefono.setError("Ingresar Telefono");
        }
        if (valor4.equals("")) {
            direccion.setError("Ingresar Direccion");
        }

        if (valor1.equals("") && valor2.equals("") && valor3.equals("") && valor4.equals("")) {
            Toast.makeText(getApplicationContext(), "Revisar datos Formulario", Toast.LENGTH_SHORT).show();
        } else {
            ejecutarServices("http://www.orthodentalnic.com/arduino/actualizardatos.php");
            Toast.makeText(getApplicationContext(), "Datos Enviados", Toast.LENGTH_SHORT).show();
        }

    }

    private void ejecutarServices(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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
                parametross.put("nombre", valor1);
                parametross.put("medidor", medidor);
                parametross.put("telefono", valor3);
                parametross.put("direccion", valor4);
                return parametross;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void datos_totales(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        nombre.setText(jsonObject.getString("nombre"));
                        telefono.setText(jsonObject.getString("telefono"));
                        direccion.setText(jsonObject.getString("direccion"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getApplicationContext(), "valo"+jsonObject, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error de conexión",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue =Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}
