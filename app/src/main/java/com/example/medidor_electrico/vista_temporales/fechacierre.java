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

import com.example.medidor_electrico.R;
import com.example.medidor_electrico.grafica_consumo;

public class fechacierre extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.calendariocierre, null);
        builder.setView(view)
                .setTitle("Cierre del mes")
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fechas = "ddd";
                    }
                });
//        datos_tarifa("https://www.orthodentalnic.com/arduino/limte_mostar.php");
//        tarifa = view.findViewById(R.id.texttarifa);

        return builder.create();
    }

}
