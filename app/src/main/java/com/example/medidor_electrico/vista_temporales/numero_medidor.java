package com.example.medidor_electrico.vista_temporales;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.medidor_electrico.R;
import com.example.medidor_electrico.configure_user_datos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class numero_medidor extends AppCompatDialogFragment {
    private EditText numero;
    private configure_user_datos listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.numero_medidor, null);
        builder.setView(view)
                .setTitle("Numero de Medidor")
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String numeromedidor = numero.getText().toString();
                        listener.applyTexts2(numeromedidor);
                    }
                });
        datos_tarifa("https://www.orthodentalnic.com/arduino/limte_mostar.php");
        numero = view.findViewById(R.id.textmedidor);
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (configure_user_datos) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Error numero");
        }
    }
    public interface configure_user_datos {
        void applyTexts2(String numeromedidor);
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
                        numero.setText(d);
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
