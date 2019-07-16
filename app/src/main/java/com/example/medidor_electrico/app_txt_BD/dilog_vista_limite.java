package com.example.medidor_electrico.app_txt_BD;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.medidor_electrico.R;

public class dilog_vista_limite extends AppCompatDialogFragment {
    private EditText porsentaje;
    private SeekBar seekBar;
    private consumo listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.limite_de_tarifa, null);
        builder.setView(view)
                .setTitle("Limite de consumo ?")
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        listener.mostar_limite(val);
                    }
                });
        porsentaje = view.findViewById(R.id.porsentaje);
        seekBar = view.findViewById(R.id.seekBarporsentaje);
        return builder.create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seekBar.setProgress(0);
        seekBar.setMax(500);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                porsentaje.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Error vista tarifa");
        }
    }

    public interface consumo {
        void mostar_limite(int porsentaje);
    }
}
