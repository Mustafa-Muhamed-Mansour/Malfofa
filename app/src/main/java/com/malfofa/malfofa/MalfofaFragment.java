package com.malfofa.malfofa;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.mlkit.vision.common.InputImage;
import com.malfofa.R;
import com.malfofa.databinding.FragmentMalfofaBinding;

import java.io.IOException;


public class MalfofaFragment extends Fragment
{

    private FragmentMalfofaBinding binding;
    private MalfofaViewModel malfofaViewModel;

    private ActivityResultLauncher<Intent> someResultLauncher;
    private String imageDetection;
    private Uri resultURI;
    private InputImage inputImage;

    private MediaPlayer playerEmpty, playerFull;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = FragmentMalfofaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);



        initViews();
        initViewModel();
        retriveViewModel();
        backgroundProcess();


    }

    private void initViews()
    {
        playerEmpty = MediaPlayer.create(requireActivity(), R.raw.message_empty);
        playerFull = MediaPlayer.create(requireActivity(), R.raw.message_full);
    }

    private void initViewModel()
    {
        malfofaViewModel = new ViewModelProvider(this).get(MalfofaViewModel.class);
    }

    private void retriveViewModel()
    {
        clickedViews();
    }

    private void clickedViews()
    {
        binding.btnArabic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String textArabic = binding.editTxtHere.getText().toString();

                if (TextUtils.isEmpty(textArabic))
                {
                    playerEmpty.start();
                    binding.editTxtHere.requestFocus();
                    return;
                }

                else
                {
                    malfofaViewModel
                            .translateArabic(textArabic)
                            .observe(getViewLifecycleOwner(), new Observer<String>()
                            {
                                @Override
                                public void onChanged(String s)
                                {
                                    if (s.equals(s))
                                    {
                                        binding.txtHere.setText(s);
                                        playerFull.start();
                                    }

                                    else
                                    {
                                        Toast.makeText(requireActivity(), "يوجد مشكلة", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    binding.txtHere.setText(null);
                }
            }
        });

        binding.btnKorean.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String textKorean = binding.editTxtHere.getText().toString();

                if (TextUtils.isEmpty(textKorean))
                {
                    playerEmpty.start();
                    binding.editTxtHere.requestFocus();
                    return;
                }

                else
                {
                    malfofaViewModel
                            .translateKorean(textKorean)
                            .observe(getViewLifecycleOwner(), new Observer<String>()
                            {
                                @Override
                                public void onChanged(String s)
                                {
                                    if (s.equals(s))
                                    {
                                        binding.txtHere.setText(s);
                                        playerFull.start();
                                    }

                                    else
                                    {
                                        Toast.makeText(requireActivity(), "يوجد مشكلة", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    binding.txtHere.setText(null);
                }
            }
        });

        binding.btnSmart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String textSmart = binding.editTxtHere.getText().toString();

                if (TextUtils.isEmpty(textSmart))
                {
                    playerEmpty.start();
                    binding.editTxtHere.requestFocus();
                    return;
                }

                else
                {
                    malfofaViewModel
                            .smartReply(textSmart)
                            .observe(getViewLifecycleOwner(), new Observer<String>()
                            {
                                @Override
                                public void onChanged(String s)
                                {
                                    if (s.equals(s))
                                    {
                                        binding.txtHere.setText(s);
                                        playerFull.start();
                                    }

                                    else
                                    {
                                        Toast.makeText(requireActivity(), "يوجد مشكلة", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        binding.btnLanguage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String textIdentifyLanguage = binding.editTxtHere.getText().toString();

                if (TextUtils.isEmpty(textIdentifyLanguage))
                {
                    playerEmpty.start();
                    binding.editTxtHere.requestFocus();
                    return;
                }

                else
                {
                    malfofaViewModel
                            .checkLanguage(textIdentifyLanguage)
                            .observe(getViewLifecycleOwner(), new Observer<String>()
                            {
                                @Override
                                public void onChanged(String s)
                                {
                                    if (s.equals(s))
                                    {
                                        binding.txtHere.setText(s);
                                        playerFull.start();
                                    }

                                    else
                                    {
                                        Toast.makeText(requireActivity(), "يوجد مشكلة", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        binding.imgDetection.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openGallery();
            }
        });

        binding.btnDetection.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (imageDetection == null)
                {
                    playerEmpty.start();
                    return;
                }

                else
                {
                    malfofaViewModel
                            .imageDetection(inputImage)
                            .observe(getViewLifecycleOwner(), new Observer<String>()
                            {
                                @Override
                                public void onChanged(String s)
                                {
                                    if (s.equals(s))
                                    {
                                        binding.txtHere.setText(s);
                                    }

                                    else
                                    {
                                        Toast.makeText(requireActivity(), "يوجد مشكلة", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void backgroundProcess()
    {
        someResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>()
        {
            @Override
            public void onActivityResult(ActivityResult result)
            {
                Intent data = result.getData();

                if (result.getResultCode() == Activity.RESULT_OK && data != null && data.getData() != null)
                {
                    resultURI = data.getData();
                    imageDetection = resultURI.toString();
                    binding.imgDetection.setImageURI(resultURI);

                    try
                    {
                        inputImage = InputImage.fromFilePath(requireActivity(), resultURI);
                    }
                    catch (IOException e)
                    {
                        Toast.makeText(requireActivity(), "يوجد مشكلة", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void openGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        someResultLauncher.launch(intent);
    }
}