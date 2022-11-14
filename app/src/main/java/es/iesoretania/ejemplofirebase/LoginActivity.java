package es.iesoretania.ejemplofirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import es.iesoretania.ejemplofirebase.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    final Integer GOOGLE_SIGN_IN = 100;

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

        binding.botonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions
                        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                GoogleSignInClient googleSignInClient = GoogleSignIn
                        .getClient(LoginActivity.this, gso);
                googleSignInClient.signOut();

                Intent siginIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(siginIntent, GOOGLE_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null){
                    AuthCredential credenciales = GoogleAuthProvider
                            .getCredential(account.getIdToken(), null);
                    FirebaseAuth.getInstance().signInWithCredential(credenciales)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Intent intentHome = new Intent(LoginActivity.this,
                                                HomeActivity.class);
                                        intentHome.putExtra("email", account.getEmail());
                                        intentHome.putExtra("proveedor", "CUENTA-GOOGLE");

                                        startActivity(intentHome);
                                    }else {
                                        muestraAlerta("Error de autenticación");
                                    }
                                }
                            });
                }
            }catch (ApiException e){
                muestraAlerta("Error en login con Google");
            }
        }
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