package es.iesoretania.ejemplofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.iesoretania.ejemplofirebase.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("Pantalla de Autenticación");

        binding.botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.EditTextEmailLogin.getText().toString().isEmpty()
                        && !binding.EditTextPassword.getText().toString().isEmpty()){
                    FirebaseAuth.getInstance()
                            .createUserWithEmailAndPassword(binding.EditTextEmailLogin.getText().toString(),
                                    binding.EditTextPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Intent intentHome = new Intent(view.getContext(), HomeActivity.class);
                                        intentHome.putExtra("email", binding.EditTextEmailLogin.getText().toString());
                                        intentHome.putExtra("proveedor", "EMAIL-PASSWORD");

                                        startActivity(intentHome);
                                    }else{
                                        muestraAlerta("Error en la creación del usuario");
                                    }
                                }
                            });
                } else{
                    muestraAlerta("Introduce valores en todos los campos");
                }
            }
        });

        binding.botonAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.EditTextEmailLogin.getText().toString().isEmpty()
                        && !binding.EditTextPassword.getText().toString().isEmpty()){
                    FirebaseAuth.getInstance()
                            .signInWithEmailAndPassword(binding.EditTextEmailLogin.getText().toString(),
                                    binding.EditTextPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Intent intentHome = new Intent(view.getContext(), HomeActivity.class);
                                        intentHome.putExtra("email", binding.EditTextEmailLogin.getText().toString());
                                        intentHome.putExtra("proveedor", "EMAIL-PASSWORD");

                                        startActivity(intentHome);
                                    }else{
                                        muestraAlerta("Error en la autenticación");
                                    }
                                }
                            });
                } else{
                    muestraAlerta("Introduce valores en todos los campos");
                }
            }
        });
    }

    private void muestraAlerta(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(s);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialogo = builder.create();
        dialogo.show();
    }
}