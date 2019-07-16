package com.example.medidor_electrico;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.medidor_electrico.app_txt_BD.dialog_vista;
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
import java.util.ArrayList;


public class Inicio extends AppCompatActivity {
    private TextView mostrarPorcentaje;
    private SeekBar seekBar;
    int limite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        mostrarPorcentaje = (TextView) findViewById(R.id.txtCargar);
        limite=limites();
        // SeekBar
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        // Valor Inicial
        seekBar.setProgress(limite);
        // Valot Final
        seekBar.setMax(1500);
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    //hace un llamado a la perilla cuando se arrastra
                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        mostrarPorcentaje.setText(String.valueOf(progress) + " Cordobas");

                        try {
                            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(
                                    "limite.dll", MainActivity.MODE_PRIVATE));
                            archivo.write(String.valueOf(progress));
                            archivo.flush();
                            archivo.close();
                        } catch (IOException e) {
                        }
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

    public int limites() {
        String todo = "";
        String[] archivos = fileList();
        if (existe(archivos, "limite.dll")) {
            try {
                InputStreamReader archivo = new InputStreamReader(
                        openFileInput("limite.dll"));
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                while (linea != null) {
                    todo = linea;
                    linea = br.readLine();
                }
                br.close();
                archivo.close();

                if(todo.equals("")){
                    limite=0;
                }else{
                    limite=Integer.parseInt(todo);
                }
            } catch (IOException e) {

            }
        }
        return Integer.parseInt(todo);
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
                Intent intent3 = new Intent(Inicio.this, consumo.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
}
