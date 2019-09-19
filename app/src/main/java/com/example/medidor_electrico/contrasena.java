package com.example.medidor_electrico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class contrasena extends AppCompatActivity {
    EditText pass1;
    EditText pass2;
    String uno, dos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrasena);
    }

    public void enviardatos(View view) {
        pass1 = findViewById(R.id.pass1);
        pass2 = findViewById(R.id.pass2);
        uno = pass1.getText().toString();
        dos = pass2.getText().toString();
        if (uno.equals("")) {
            pass1.setError("Ingresar Campo");
        }
        if (dos.equals("")) {
            pass2.setError("Ingresar Campo");
        }
        if(uno.equals("") && dos.equals("")){

        }else if(uno.equals(dos)){
            Toast.makeText(getApplicationContext(), "Guardado Exito", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Contraseña no son Iguales", Toast.LENGTH_SHORT).show();
        }
    }
}
