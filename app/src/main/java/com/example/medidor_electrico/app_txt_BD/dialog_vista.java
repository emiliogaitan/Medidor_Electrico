package com.example.medidor_electrico.app_txt_BD;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.medidor_electrico.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class dialog_vista extends AppCompatDialogFragment {
    private EditText tarifa;
    private consumo listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.tarifa_vista, null);
        builder.setView(view)
                .setTitle("Tarifa por KWH")
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tarifastotal = tarifa.getText().toString();
                        listener.applyTexts(tarifastotal);
                    }
                });
        datos_tarifa("http://192.168.43.153/arduino/limte_mostar.php");
        tarifa = view.findViewById(R.id.texttarifa);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (consumo) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Error Vista tarifa");
        }
    }

    public interface consumo {
        void applyTexts(String tarifa);
    }
    private void datos_tarifa(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        String d= jsonObject.getString("tarifa");
                        tarifa.setText(d);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = (RequestQueue) Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }
}
