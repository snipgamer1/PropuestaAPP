package com.example.propuesta.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.propuesta.R;
import com.example.propuesta.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    Button btnLogin;
    Button btnRegister;


    private FragmentProfileBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el diseño usando ViewBinding
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar los botones
        Button btnLogin = binding.btnLogin;
        Button btnRegister = binding.btnRegister;

        // Obtener el NavController
        NavController navController = Navigation.findNavController(view);

        // Configurar el botón de Login
        btnLogin.setOnClickListener(v -> navController.navigate(R.id.action_profileFragment_to_loginFragment));

        // Configurar el botón de Register
        btnRegister.setOnClickListener(v -> navController.navigate(R.id.action_profileFragment_to_registerFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}