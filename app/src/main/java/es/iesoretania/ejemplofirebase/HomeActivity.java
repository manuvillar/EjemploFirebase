package es.iesoretania.ejemplofirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import es.iesoretania.ejemplofirebase.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("Pantalla Home");

        Bundle datos = getIntent().getExtras();
        String mail = datos.getString("email");
        String proveedor = datos.getString("proveedor");

        binding.TextViewEmail.setText(mail);
        binding.TextViewProveedor.setText(proveedor);

        binding.botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                onBackPressed();
            }
        });
    }
}