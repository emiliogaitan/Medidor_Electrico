package com.example.medidor_electrico;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

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

import java.util.ArrayList;


public class Inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
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
        String[] values = new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio"};
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
        BarEntry v1e2 = new BarEntry(1, 4341f);
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(2, 3121f);
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(3, 5521f);
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(4, 10421f);
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(5, 27934f);
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

}
