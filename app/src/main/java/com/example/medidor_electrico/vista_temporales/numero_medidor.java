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
import com.example.medidor_electrico.configure_user_datos;

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
}
