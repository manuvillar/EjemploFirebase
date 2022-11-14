package es.iesoretania.ejemplofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import es.iesoretania.ejemplofirebase.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        setTitle("Pantalla Home");

        Bundle datos = getIntent().getExtras();
        String mail = datos.getString("email");
        String proveedor = datos.getString("proveedor");

        binding.EditTextEmailHome.setText(mail);
        binding.TextViewProveedor.setText(proveedor);

        binding.botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                onBackPressed();
            }
        });

        binding.botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Nos preparamos los datos clave-valor que vamos a guardar.
                Map<String, Object> usuario = new HashMap<>();
                usuario.put("proveedor", proveedor);
                usuario.put("ciudad", binding.EditTextCiudad.getText().toString());
                //Lo guardaremos como int
                usuario.put("cp", Integer.parseInt(binding.EditTextCP.getText().toString()));

                //Los guardamos en la base de datos.
                db.collection("usuarios").document(mail).set(usuario);
                Toast.makeText(HomeActivity.this, "Registro guardado correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        binding.botonRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recuperamos el registro cuyo email sea el que pongamos en el EditText.
                //El método onComplete se ejecutará si lo encuentra
                db.collection("usuarios")
                        .document(binding.EditTextEmailHome.getText().toString()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                //En documentSnapshot se nos pasa el documento a recuperar de tipo Map.
                                binding.TextViewProveedor.setText((String) documentSnapshot.get("proveedor"));
                                binding.EditTextCiudad.setText((String) documentSnapshot.get("ciudad"));
                                binding.EditTextCP.setText(String.valueOf((Long) documentSnapshot.get("cp")));
                            }
                        });
            }
        });

        binding.botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Borramos el documento seleccionado en el EditText mail.
                db.collection("usuarios")
                        .document(binding.EditTextEmailHome.getText().toString()).delete();
            }
        });
    }
}