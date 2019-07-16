package com.example.medidor_electrico;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.medidor_electrico.app_txt_BD.dialog_vista;
import com.example.medidor_electrico.app_txt_BD.dilog_vista_limite;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class consumo extends AppCompatActivity implements dialog_vista.consumo {
    TextView fecha;
    String fecha_valor;
    String indmedidor;
    String tarifas="";
    private BarChart mBarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo);
        setFechaActual();
        // server acceso
        ejecutarServices("http://192.168.43.153/arduino/potencia.php");

        mBarChart = findViewById(R.id.grafica1);
        mBarChart.getDescription().setEnabled(false);
        setData(10);
        mBarChart.setFitBars(true);

        tarifas = tarifa_datos();
        if (tarifas.equals("")) {
            dialog_vista dialog_vista = new dialog_vista();
            dialog_vista.show(getSupportFragmentManager(), "ejemplo1");
        }
    }

    public String tarifa_datos() {
        String todo = "";
        String[] archivos = fileList();
        if (existe(archivos, "tarifa.dll")) {
            try {
                InputStreamReader archivo = new InputStreamReader(
                        openFileInput("tarifa.dll"));
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                while (linea != null) {
                    todo = linea;
                    linea = br.readLine();
                }
                br.close();
                archivo.close();
            } catch (IOException e) {

            }
        }
        return todo;
    }

    public void setFechaActual() {
        fecha = findViewById(R.id.textfecha);
        final Calendar c = Calendar.getInstance();
        int año = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        String s = formatter.format(c.getTime());


        Format forma = new SimpleDateFormat("yyyy/MM/dd");
        String ss = forma.format(c.getTime());
        fecha.setText(s);
        fecha_valor = ss;
    }

    private void setData(int count) {
        ArrayList<BarEntry> yVals = new ArrayList<>();

        yVals.add(new BarEntry(1, (int) 100));
        yVals.add(new BarEntry(2, (int) 200));
        yVals.add(new BarEntry(3, (int) 300));
        yVals.add(new BarEntry(4, (int) 400));
        yVals.add(new BarEntry(5, (int) 500));

        BarDataSet set = new BarDataSet(yVals, "Consumo Mensual");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setDrawValues(true);

        BarData data = new BarData(set);
        mBarChart.setData(data);
        mBarChart.invalidate();
        mBarChart.animateY(500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item3:
                Intent intent3 = new Intent(consumo.this, Inicio.class);
                startActivity(intent3);
                return true;
            case R.id.itemsub1:
                dialog_vista dialog_vista = new dialog_vista();
                dialog_vista.show(getSupportFragmentManager(), "ejemplo1");
                return true;
            case R.id.itemsub2:
                Intent intent4 = new Intent(consumo.this, configure_user_datos.class);
                intent4.putExtra("medidor",indmedidor);
                startActivity(intent4);
                return true;
            case R.id.itemsub3:
                dilog_vista_limite dilog_vista_limite = new dilog_vista_limite();
                dilog_vista_limite.show(getSupportFragmentManager(), "ejemplo2");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void applyTexts(String tarifa) {
        grabar_tarifa(tarifa);
        Intent intent3 = new Intent(consumo.this, consumo.class);
        startActivity(intent3);
    }

    private void ejecutarServices(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                TextView vald = findViewById(R.id.KWH);
                vald.setText(response);

                TextView total = findViewById(R.id.totalTEXT);
                int val = Integer.parseInt(response);
                int val2 = Integer.parseInt(tarifas);
                float respuesta = (float) (val * val2);
                total.setText(String.valueOf(respuesta) + " Cordobas");
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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

    public void grabar_tarifa(String v) {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(
                    "tarifa.dll", MainActivity.MODE_PRIVATE));
            archivo.write(v);
            archivo.flush();
            archivo.close();
        } catch (IOException e) {
        }
        finish();
    }
}
