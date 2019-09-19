package com.example.medidor_electrico;

import android.content.Intent;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.medidor_electrico.vista_temporales.numero_medidor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class configure_user_datos extends AppCompatActivity implements numero_medidor.configure_user_datos {
    RequestQueue requestQueue;
    String medidor;

    public EditText nombre;
    public String valor1 = "";
    public EditText telefono;
    public String valor2 = "";
    public EditText departamento;
    public String valor3 = "";
    public EditText direccion;
    public String valor4 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_user_datos);
        nombre = findViewById(R.id.pass2);
        telefono = findViewById(R.id.Editext203);
        departamento= findViewById(R.id.Editext204);
        direccion = findViewById(R.id.Editext205);

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
        datos_totales("https://www.orthodentalnic.com/arduino/todo_generales.php");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_datos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.regresar:
                Intent intent3 = new Intent(this, consumo.class);
                startActivity(intent3);
                return true;
            case R.id.itemsub1:
                Intent intent5 = new Intent(this, contrasena.class);
                startActivity(intent5);
                return true;
            case R.id.itemsub2:
                numero_medidor dialog=new numero_medidor();
                dialog.show(getSupportFragmentManager(), "ejemplo2");
                return true;
            case R.id.itemsub3:
                Intent intent4 = new Intent(this, version.class);
                startActivity(intent4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void applyTexts2(String numeromedidor) {

    }
    public void enviardatos(View view) {
        valor1 = nombre.getText().toString();
        valor2 = telefono.getText().toString();
        valor3 = departamento.getText().toString();
        valor4 = direccion.getText().toString();

        if (valor1.equals("")) {
            nombre.setError("Ingresar Nombre");
        }

        if (valor2.equals("")) {
            telefono.setError("Ingresar Telefono");
        }
        if (valor3.equals("")) {
            departamento.setError("Ingresar Departemento");
        }

        if (valor4.equals("")) {
            direccion.setError("Ingresar Direccion");
        }

        if (valor1.equals(" ") && valor2.equals(" ") && valor3.equals(" ") && valor4.equals(" ")) {
            Toast.makeText(getApplicationContext(), "Revisar datos Formulario", Toast.LENGTH_SHORT).show();
        } else {
            ejecutarServices("https://www.orthodentalnic.com/arduino/actualizardatos.php");
            Toast.makeText(getApplicationContext(), "Datos Guardados Exito", Toast.LENGTH_SHORT).show();
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
                parametross.put("telefono", valor2);
                parametross.put("departamento", valor3);
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
                        departamento.setText(jsonObject.getString("departamento"));
                        direccion.setText(jsonObject.getString("direccion"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
