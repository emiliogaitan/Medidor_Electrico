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

import com.example.medidor_electrico.R;

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
        tarifa = view.findViewById(R.id.texttarifa);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener=(consumo) context;
        }catch (ClassCastException e){
           throw  new ClassCastException(context.toString()+"Error Vista tarifa");
        }
    }

    public interface consumo {
        void applyTexts(String tarifa);
    }
}