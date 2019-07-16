package com.example.medidor_electrico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
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
    String tarifas = "";

    private BarChart mBarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo);
        setFechaActual();
        // server acceso
        ejecutarServices("http://www.orthodentalnic.com/arduino/potencia.php");

        tarifas = tarifa_datos();
        if (tarifas.equals("")) {
            dialog_vista dialog_vista = new dialog_vista();
            dialog_vista.show(getSupportFragmentManager(), "ejemplo1");
        }
        HorizontalBarChart chart = (HorizontalBarChart) findViewById(R.id.grafica1);
        BarDataSet set1;
        set1 = new BarDataSet(getDataSet(), "Año 2019");

        set1.setColors(ColorTemplate.MATERIAL_COLORS);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        // hide Y-axis
        YAxis left = chart.getAxisLeft();
        left.setDrawLabels(false);

        // custom X-axis labels
        String[] values = new String[]{"Marzo", "Mayo", "Junio", "Julio", "Agosto", "Septiembre"};
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new Inicio.MyXAxisValueFormatter(values));

        chart.setData(data);

        // custom description
        Description description = new Description();
        description.setText("Consumo total Kwh mensula");
        chart.setDescription(description);

        // hide legend
        chart.getLegend().setEnabled(false);
        chart.animateY(1000);
        chart.invalidate();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemsub1:
                dialog_vista dialog_vista = new dialog_vista();
                dialog_vista.show(getSupportFragmentManager(), "ejemplo1");
                return true;
            case R.id.itemsub2:
                Intent intent4 = new Intent(consumo.this, configure_user_datos.class);
                intent4.putExtra("medidor", indmedidor);
                startActivity(intent4);
                return true;
            case R.id.itemsub4:
                Intent intent5 = new Intent(consumo.this,Inicio.class);
                startActivity(intent5);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void applyTexts(String tarifa) {
        grabar_tarifa(tarifa);
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
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }
    }
    private ArrayList<BarEntry> getDataSet() {
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e2 = new BarEntry(1, 95f);
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(2, 98f);
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(3, 100f);
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(4, 95f);
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(5, 0f);
        valueSet1.add(v1e6);
        return valueSet1;
    }
}
