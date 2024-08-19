package com.example.propuesta;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link-Fragment} subclass.
 * Use the {@link-LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    EditText Email, Password;
    Button btn_iniciarSesion;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);



        Email = view.findViewById(R.id.Email);
        Password = view.findViewById(R.id.Password);
        btn_iniciarSesion = view.findViewById(R.id.btn_iniciarSesion);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(); // Inicializa databaseReference aquí
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Ingresando, espere por favor");
        progressDialog.setCancelable(false);





        btn_iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                String password = Password.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Email.setError("Correo no válido");
                        Email.setFocusable(true);
                    } else if (password.length() < 6) {
                        Password.setError("Contraseña no válida");
                        Password.setFocusable(true);
                    } else {
                        LoginAdministrador(email, password);
                    }
                }
            }
        });
        return view;
    }

    private void LoginAdministrador(String correo, String password) {
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(correo, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();
                                Log.d("Login", "UID del usuario: " + uid);
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(uid);

                                userRef.child("ROL").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    String rol = task.getResult().getValue(String.class);
                                                    Log.d("Login", "Rol del usuario: " + rol);
                                                    if ("Admin".equals(rol)) {
                                                        Intent intent = new Intent(getActivity(), MainActivityAdmin.class);
                                                       startActivity(intent);
                                                        getActivity().finish();
                                                    } else if ("Cliente".equals(rol)) {
                                                        Intent intent = new Intent(getActivity(), ClienteDetailsActivity.class);
                                                        startActivity(intent);
                                                        getActivity().finish();
                                                    } else {
                                                        Toast.makeText(getActivity(), "Rol no reconocido", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    // Manejo de errores
                                                    Exception exception = task.getException();
                                                    Log.e("FirebaseError", "Error al obtener el rol: " + exception.getMessage());
                                                    Toast.makeText(getActivity(), "Error al obtener el rol", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            UsuarioInvalido();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        UsuarioInvalido();
                    }
                });
    }


    private void UsuarioInvalido() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("Ha ocurrido un error");
        builder.setMessage("Verifique si el correo o contraseña son correctos")
                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }



}