package com.example.medidor_electrico;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.google.zxing.Result;

import java.io.IOException;
import java.io.OutputStreamWriter;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView msZXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        //quitar el navbar temporalmente
        ActionBar bar = getSupportActionBar();
        if (getSupportActionBar() == null) {

        } else {
            getSupportActionBar().hide();
        }
//        habilitando en navbar automaticamente
        msZXingScannerView = new ZXingScannerView(this);
        setContentView(msZXingScannerView);
        msZXingScannerView.setResultHandler(this);
        msZXingScannerView.startCamera();

    }

    private boolean existe(String[] archivos, String archbusca) {
        for (int f = 0; f < archivos.length; f++) {
            if (archbusca.equals(archivos[f])) {
                return true;
            }
        }
        return false;
    }

    public void grabar(String v) {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(
                    "datos.dll", MainActivity.MODE_PRIVATE));
            archivo.write(v);
            archivo.flush();
            archivo.close();
        } catch (IOException e) {
        }
    }

    @Override
    public void handleResult(final Result result) {
        final Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title

        alertDialogBuilder.setTitle("Respuesta");
        alertDialogBuilder.setIcon(R.mipmap.scanner);

        // set dialog message
        alertDialogBuilder
                .setMessage("This is the message")
                .setCancelable(false)
                .setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        String res=result.getText().toString();
                        grabar(res);
                        Intent intent = new Intent(scanner.this, intronext.class);
                        startActivity(intent);
                        scanner.this.finish();
                    }
                })
                .setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing

                        grabar("");
                        scanner.this.finish();
                        Intent intent = new Intent(scanner.this, MainActivity.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
