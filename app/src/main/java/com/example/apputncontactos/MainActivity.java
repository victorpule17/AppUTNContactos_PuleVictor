package com.example.apputncontactos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText txtNombres, txtTelefono, txtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtNombres = findViewById(R.id.txtNombres);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtEmail = findViewById(R.id.txtEmail);

    }
    public void cmdGuardar_onClick(View v) {
        String nombres = txtNombres.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();

        if (nombres.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!telefono.matches("\\d{10}")) {
            Toast.makeText(this, "Por favor, ingresa un número de teléfono válido de 10 dígitos.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Por favor, ingresa un correo electrónico válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String idContacto = UUID.randomUUID().toString();
        editor.putString("nombres_" + idContacto, nombres);
        editor.putString("telefono_" + idContacto, telefono);
        editor.putString("email_" + idContacto, email);
        editor.commit();
        Toast.makeText(this, "Datos Guardados Correctamente!!", Toast.LENGTH_SHORT).show();
        txtNombres.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
    }

    public void cmdBuscar_onClick(View v) {
        String nombreABuscar = txtNombres.getText().toString().trim().toLowerCase();

        if (nombreABuscar.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa el nombre que deseas buscar.", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();
        boolean contactoEncontrado = false;
        String nombreEncontrado = "";
        String telefonoEncontrado = "";
        String emailEncontrado = "";

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("nombres_")) {
                String idContacto = entry.getKey().substring("nombres_".length());
                String nombresGuardados = preferences.getString("nombres_" + idContacto, "").toLowerCase();
                String telefonoGuardado = preferences.getString("telefono_" + idContacto, "");
                String emailGuardado = preferences.getString("email_" + idContacto, "");
                if (nombresGuardados.contains(nombreABuscar)) {
                    nombreEncontrado = nombresGuardados;
                    telefonoEncontrado = telefonoGuardado;
                    emailEncontrado = emailGuardado;
                    contactoEncontrado = true;
                    break;
                }
            }
        }
        if (contactoEncontrado) {
            txtNombres.setText(nombreEncontrado);
            txtTelefono.setText(telefonoEncontrado);
            txtEmail.setText(emailEncontrado);
            Toast.makeText(this, "Contacto encontrado", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No se encontraron contactos con ese nombre.", Toast.LENGTH_SHORT).show();
        }
    }

    public  void cmdLimpiar_onClick(View v){
        txtNombres.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
    }
}