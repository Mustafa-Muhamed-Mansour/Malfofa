package com.malfofa.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.malfofa.R;
import com.malfofa.databinding.FragmentSplashBinding;

public class SplashFragment extends Fragment
{

    private FragmentSplashBinding binding;
    private SplashViewModel splashViewModel;

    private MediaPlayer player;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        initViewModel();
        retriveViewModel(view);

    }


    private void initViewModel()
    {
        splashViewModel = new ViewModelProvider(this).get(com.malfofa.ui.SplashViewModel.class);
    }

    private void retriveViewModel(View view)
    {
        splashViewModel
                .initHandler()
                .observe(getViewLifecycleOwner(), new Observer<Boolean>()
                {
                    @Override
                    public void onChanged(Boolean aBoolean)
                    {
                        if (aBoolean)
                        {
                            Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_translateFragment);
                        }
                    }
                });
    }

}