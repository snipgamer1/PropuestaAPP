package com.example.propuesta;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * A simple {@link-Fragment} subclass.
 * Use the {@link-RegistrarseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrarseFragment extends Fragment {

    EditText Usuario, Email, Password;
    Button btn_registrarse;

    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrarse, container, false);
        Usuario = view.findViewById(R.id.Usuario);
        Email = view.findViewById(R.id.Email);
        Password = view.findViewById(R.id.Password);
        btn_registrarse = view.findViewById(R.id.btn_registrarse);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = Usuario.getText().toString();
                String email = Email.getText().toString();
                String password = Password.getText().toString();

                if (usuario.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), "Por favor llene los campos solicitados", Toast.LENGTH_SHORT).show();
                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Email.setError("Correo no válido");
                        Email.setFocusable(true);
                    } else if (password.length() < 6) {
                        Password.setError("La contraseña debe ser mayor a 6 caracteres");
                        Password.setFocusable(true);
                    } else {
                        registrarCliente(email, password, usuario);
                    }
                }
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registrando, espere por favor");
        progressDialog.setCancelable(false);
        return view;
    }

    private void registrarCliente(String email, String password, String usuario) {
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        String UID = user.getUid();
                        String usuario = Usuario.getText().toString();
                        String email = Email.getText().toString();
                        String password = Password.getText().toString();

                        HashMap<String, Object> userData = new HashMap<>();
                        userData.put("UID", UID);
                        userData.put("USUARIO", usuario);
                        userData.put("EMAIL", email);
                        userData.put("PASSWORD" ,password);
                        userData.put("ROL", "Cliente");

                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference reference = db.getReference("Usuarios");
                        reference.child(UID).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Fallo al guardar en la base de datos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Registro fallido: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}