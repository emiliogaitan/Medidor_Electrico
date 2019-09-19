package com.example.medidor_electrico;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.medidor_electrico.vista_temporales.dialog_vista;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class consumo extends AppCompatActivity implements dialog_vista.consumo {

    TextView fecha;
    String fecha_valor;
    String indmedidor;
    private static int potencia;
    private static int limite;
    NotificationCompat.Builder notificacion;
    private static final int idUnica = 006;

    public static double tarifas;
    RequestQueue requestQueue;

    WebView grafica1;

//fechas
    int dia,mes,ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo);

        setFechaActual();
        peticiones_https();

        grafica1 = findViewById(R.id.graficaweb1);
        grafica1.loadUrl("eeeee.com");


    }

    public void setFechaActual() {
        fecha = findViewById(R.id.textfecha);
        final Calendar c = Calendar.getInstance();
        ano = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        String s = formatter.format(c.getTime());


        Format forma = new SimpleDateFormat("yyyy/MM/dd");
        String ss = forma.format(c.getTime());
        fecha.setText(s);
        fecha_valor = ss;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    public void peticiones_https() {
        // server acceso
        datos_totales("https://www.orthodentalnic.com/arduino/limte_mostar.php");
        ejecutarServices("https://www.orthodentalnic.com/arduino/potencia.php");
        datos_tarifa("https://www.orthodentalnic.com/arduino/limte_mostar.php");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                peticiones_https();
                return true;
            case R.id.itemsub1:
                dialog_vista dialog_vista = new dialog_vista();
                dialog_vista.show(getSupportFragmentManager(), "ejemplo1");
                return true;
            case R.id.itemsub2:
                consumo.this.finish();
                Intent intent4 = new Intent(consumo.this, configure_user_datos.class);
                intent4.putExtra("medidor", indmedidor);
                startActivity(intent4);
                return true;
            case R.id.itemsub4:
                consumo.this.finish();
                Intent intent5 = new Intent(consumo.this, grafica_consumo.class);
                startActivity(intent5);
                return true;
            case R.id.itemsub3:
                consumo.this.finish();
                Intent intent6 = new Intent(consumo.this, MainActivity.class);
                startActivity(intent6);
                cerra_app("");
                cerra_app2("");
                return true;
            case R.id.itemsub6:
                consumo.this.finish();
                Intent intent7 = new Intent(consumo.this, categoria_electrodomestico.class);
                startActivity(intent7);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void cerra_app(String v) {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(
                    "datos.dll", MainActivity.MODE_PRIVATE));
            archivo.write(v);
            archivo.flush();
            archivo.close();
        } catch (IOException e) {
        }
    }

    public void cerra_app2(String v) {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(
                    "mensaje.dll", MainActivity.MODE_PRIVATE));
            archivo.write(v);
            archivo.flush();
            archivo.close();
        } catch (IOException e) {
        }
    }

    @Override
    public void applyTexts(String tarifa) {
        guardar_tarifa("https://www.orthodentalnic.com/arduino/tarifa_insertar.php");
        tarifas = Double.parseDouble(tarifa);
        finish();
        Intent intent5 = new Intent(consumo.this, consumo.class);
        startActivity(intent5);
    }


    private void ejecutarServices(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                TextView vald = findViewById(R.id.KWH);
                vald.setText(response);

                TextView total = findViewById(R.id.totalTEXT);
                int val = Integer.parseInt(response);
                potencia = val;
                float respuesta = (float) (val * tarifas);
                potencia = (int) respuesta;
                total.setText(String.valueOf(respuesta) + " Cordobas");


                //hilo de notificacion de respuesta de arduino mini
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notificacion();
                    }
                }, 9000);


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
                indmedidor = indmedidor();
                parametross.put("date", fecha_valor);
                parametross.put("indmedidor", indmedidor);
                return parametross;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String indmedidor() {
        String[] archivos = fileList();
        String todo = "";
        if (existe(archivos, "datos.dll")) {
            try {
                InputStreamReader archivo = new InputStreamReader(
                        openFileInput("datos.dll"));
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                while (linea != null) {
                    todo = todo + linea + "\n";
                    linea = br.readLine();
                }
                br.close();
                archivo.close();
                if (todo.equals("")) {
                    finish();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            } catch (IOException e) {

            }
        }
        return todo;
    }

    private boolean existe(String[] archivos, String archbusca) {
        for (int f = 0; f < archivos.length; f++) {
            if (archbusca.equals(archivos[f])) {
                return true;
            }
        }
        return false;
    }

    private void datos_tarifa(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {

                        jsonObject = response.getJSONObject(i);
                        tarifas = Double.parseDouble(jsonObject.getString("tarifa"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Error Conexion", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void guardar_tarifa(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getApplicationContext(), "Error Conexion", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametross = new HashMap<String, String>();
                indmedidor = indmedidor();
                parametross.put("tarifa", String.valueOf(tarifas));
                return parametross;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void notificacion() {

        // notificacon de mostracion de la imagenes de lsp datos
        notificacion = new NotificationCompat.Builder(this);
        notificacion.setAutoCancel(true);
        int calculo=potencia*5;

        if (calculo >= limite) {
            notificacion.setSmallIcon(R.mipmap.bombilla);
            notificacion.setTicker("Limite de consumo");
            notificacion.setPriority(Notification.PRIORITY_HIGH);
            notificacion.setWhen(System.currentTimeMillis());
            notificacion.setContentTitle("Ahorro");
            notificacion.setContentText("Limite de consumo al Maximo");

            Intent intent = new Intent(this, grafica_consumo.class);
            PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);
            notificacion.setContentIntent(pendingIntent);

            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(idUnica, notificacion.build());
        }

    }

    private void datos_totales(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        limite = Integer.parseInt((jsonObject.getString("limite")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getApplicationContext(), "Error Conexion", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void Cierre(final View view) {
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(consumo.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fecha = findViewById(R.id.textfecha);
                fecha.setText(dayOfMonth + "/" + month +"/"+year);
                fecha_valor=year+ "/" + month +"/"+dayOfMonth;
            }
        }, ano, mes, dia);
        datePickerDialog.show();
    }
}
