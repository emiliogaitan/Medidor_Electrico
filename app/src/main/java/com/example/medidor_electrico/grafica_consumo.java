package com.example.medidor_electrico;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class grafica_consumo extends AppCompatActivity {
    private TextView mostrarPorcentaje;
    private SeekBar seekBar;
    private static int limite;
    RequestQueue requestQueue;
    NotificationCompat.Builder notificacion;
    private static final int idUnica = 006;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        datos_totales("http://192.168.43.153/arduino/limte_mostar.php");

        mostrarPorcentaje = (TextView) findViewById(R.id.txtCargar);
        // SeekBar
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setProgress(limite);
        mostrarPorcentaje.setText(limite + " Cordobas");
        // Valot Final
        seekBar.setMax(1500);

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    //hace un llamado a la perilla cuando se arrastra
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        mostrarPorcentaje.setText(String.valueOf(progress) + " Cordobas");
                        limite=progress;
                        ejecutarServices("http://192.168.43.153/arduino/limite_insertar.php");
                    }

                    //hace un llamado  cuando se toca la perilla
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    //hace un llamado  cuando se detiene la perilla
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

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
        xAxis.setValueFormatter(new MyXAxisValueFormatter(values));

        chart.setData(data);

        // custom description
        Description description = new Description();
        description.setText("Consumo total Kwh mensula");
        chart.setDescription(description);

        // hide legend
        chart.getLegend().setEnabled(false);
        chart.animateY(1000);
        chart.invalidate();


        // notificacon de mostracion de la imagenes de lsp datos
        notificacion=new NotificationCompat.Builder(this);
        notificacion.setAutoCancel(true);

        if((limite*5)>900){
            notificacion.setSmallIcon(R.mipmap.bombilla);
            notificacion.setTicker("Limite de consumo");
            notificacion.setPriority(Notification.PRIORITY_HIGH);
            notificacion.setWhen(System.currentTimeMillis());
            notificacion.setContentTitle("Ahorro");
            notificacion.setContentText("Limite de consumo al Maximo");

            Intent intent=new Intent(grafica_consumo.this,grafica_consumo.class);
            PendingIntent pendingIntent=PendingIntent.getActivities(grafica_consumo.this,0, new Intent[]{intent},PendingIntent.FLAG_UPDATE_CURRENT);
            notificacion.setContentIntent(pendingIntent);

            NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            nm.notify(idUnica,notificacion.build());
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
                parametross.put("limite", String.valueOf(limite));
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
                        limite= Integer.parseInt((jsonObject.getString("limite")));
                        mostrarPorcentaje.setText(limite + " Cordobas");
                        seekBar.setProgress(limite);
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
    public void nn1(View view) {

        HorizontalBarChart chart = (HorizontalBarChart) findViewById(R.id.grafica1);
        BarDataSet set1;
        set1 = new BarDataSet(getDataSetDias(), "Año 2019");

        set1.setColors(ColorTemplate.MATERIAL_COLORS);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        // hide Y-axis
        YAxis left = chart.getAxisLeft();
        left.setDrawLabels(false);

        // custom X-axis labels
        String[] values = new String[]{"", "Lunes", "Martes", "Jueves", "Viernes", "Sabado"};
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(values));

        chart.setData(data);

        // custom description
        Description description = new Description();
        description.setText("Consumo total Kwh Diario");
        chart.setDescription(description);

        // hide legend
        chart.getLegend().setEnabled(false);
        chart.animateY(1000);
        chart.invalidate();

    }

    public void nn2(View view) {

        HorizontalBarChart chart = (HorizontalBarChart) findViewById(R.id.grafica1);
        BarDataSet set1;
        set1 = new BarDataSet(getDataSetSemana(), "Año 2019");

        set1.setColors(ColorTemplate.MATERIAL_COLORS);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        // hide Y-axis
        YAxis left = chart.getAxisLeft();
        left.setDrawLabels(false);

        // custom X-axis labels
        String[] values = new String[]{"", "Lunes", "Martes", "Jueves", "Viernes", "Sabado"};
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(values));

        chart.setData(data);

        // custom description
        Description description = new Description();
        description.setText("Consumo total Kwh Diario");
        chart.setDescription(description);

        // hide legend
        chart.getLegend().setEnabled(false);
        chart.animateY(1000);
        chart.invalidate();
    }

    public void nn3(View view) {
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
        xAxis.setValueFormatter(new MyXAxisValueFormatter(values));

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

    public static class MyXAxisValueFormatter implements IAxisValueFormatter {

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

    private ArrayList<BarEntry> getDataSetDias() {
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e2 = new BarEntry(1, 3f);
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(2, 3.6f);
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(3, 0f);
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(4, 0f);
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(5, 0f);
        valueSet1.add(v1e6);
        return valueSet1;
    }

    private ArrayList<BarEntry> getDataSetSemana() {
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e2 = new BarEntry(1, 23.75f);
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(2, 23f);
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(3, 22f);
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(4, 10f);
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(5, 0f);
        valueSet1.add(v1e6);
        return valueSet1;
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
                Intent intent3 = new Intent(grafica_consumo.this, consumo.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
