package com.malfofa.malfofa;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.smartreply.SmartReply;
import com.google.mlkit.nl.smartreply.SmartReplyGenerator;
import com.google.mlkit.nl.smartreply.SmartReplySuggestion;
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult;
import com.google.mlkit.nl.smartreply.TextMessage;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.util.ArrayList;
import java.util.List;

public class MalfofaViewModel extends ViewModel
{

    private MutableLiveData<Boolean> booleanMutableArabic, booleanMutableKorean, booleanMutableReply, booleanMutableDetection;
    private MutableLiveData<String> stringMutableArabic, stringMutableKorean, stringMutableReply, stringMutableLanguage, stringMutableDetection;

    private com.google.mlkit.nl.translate.Translator translatorArabic, translatorKorean;
    private TranslatorOptions optionArabic, optionKorean;
    private DownloadConditions conditionArabic, conditionKorean;

    private List<TextMessage> conversation;
    private SmartReplyGenerator replyGenerator;

    private LanguageIdentifier identifier;

    private ImageLabeler labeler;
    private String result = "";



    public MalfofaViewModel()
    {
        booleanMutableArabic = new MutableLiveData<>();
        booleanMutableKorean = new MutableLiveData<>();
        booleanMutableReply = new MutableLiveData<>();
        booleanMutableDetection = new MutableLiveData<>();
        stringMutableArabic = new MutableLiveData<>();
        stringMutableKorean = new MutableLiveData<>();
        stringMutableReply = new MutableLiveData<>();
        stringMutableLanguage = new MutableLiveData<>();
        stringMutableDetection = new MutableLiveData<>();

        optionArabic = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.ARABIC)
                .build();

        optionKorean = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.KOREAN)
                .build();

        translatorArabic = Translation.getClient(optionArabic);
        translatorKorean = Translation.getClient(optionKorean);

        conditionArabic = new DownloadConditions.Builder().requireWifi().build();
        conditionKorean = new DownloadConditions.Builder().requireWifi().build();

        conversation = new ArrayList<>();
        replyGenerator = SmartReply.getClient();

        identifier = LanguageIdentification.getClient();

        labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);


    }


    public LiveData<String> translateArabic(String textArabic)
    {

        translatorArabic
                .downloadModelIfNeeded(conditionArabic)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void unused)
                    {
                        booleanMutableArabic.postValue(true);
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        stringMutableArabic.setValue(e.getMessage());
                        booleanMutableArabic.postValue(false);
                    }
                });

        translatorArabic
                .translate(textArabic)
                .addOnSuccessListener(new OnSuccessListener<String>()
                {
                    @Override
                    public void onSuccess(String s)
                    {
                        stringMutableArabic.setValue(s);
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        stringMutableArabic.setValue(e.getMessage());
                        booleanMutableArabic.postValue(false);
                    }
                });

        return stringMutableArabic;
    }

    public LiveData<String> translateKorean(String textKorean)
    {
        translatorKorean
                .downloadModelIfNeeded(conditionKorean)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void unused)
                    {
                        booleanMutableKorean.postValue(true);
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        stringMutableKorean.setValue(e.getMessage());
                        booleanMutableKorean.postValue(false);
                    }
                });

        translatorKorean
                .translate(textKorean)
                .addOnSuccessListener(new OnSuccessListener<String>()
                {
                    @Override
                    public void onSuccess(String s)
                    {
                        stringMutableKorean.setValue(s);
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        stringMutableKorean.setValue(e.getMessage());
                        booleanMutableKorean.postValue(false);
                    }
                });

        return stringMutableKorean;
    }

    public LiveData<String> smartReply(String textSmart)
    {
        conversation.add(TextMessage.createForRemoteUser(textSmart, System.currentTimeMillis(), "123456"));

        replyGenerator
                .suggestReplies(conversation)
                .addOnSuccessListener(new OnSuccessListener<SmartReplySuggestionResult>()
                {
                    @Override
                    public void onSuccess(SmartReplySuggestionResult smartReplySuggestionResult)
                    {
                        if (smartReplySuggestionResult.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS)
                        {

                            for (SmartReplySuggestion suggestion : smartReplySuggestionResult.getSuggestions())
                            {
                                stringMutableReply.setValue(suggestion.getText());
                            }

                            booleanMutableReply.postValue(true);
                        }

                        else
                        {
                            stringMutableReply.setValue("Error");
                            booleanMutableReply.postValue(false);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        stringMutableReply.setValue(e.getMessage());
                        booleanMutableReply.postValue(false);
                    }
                });

        return stringMutableReply;
    }

    public LiveData<String> checkLanguage(String textIdentifyLanguage)
    {

        identifier
                .identifyLanguage(textIdentifyLanguage)
                .addOnSuccessListener(new OnSuccessListener<String>()
                {
                    @Override
                    public void onSuccess(String s)
                    {
                        stringMutableLanguage.setValue(s);
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        stringMutableReply.setValue(e.getMessage());
                        booleanMutableReply.postValue(false);
                    }
                });

        return stringMutableLanguage;
    }


    public LiveData<String> imageDetection(InputImage inputImage)
    {
        labeler
                .process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>()
                {
                    @Override
                    public void onSuccess(List<ImageLabel> imageLabels)
                    {
                        for (ImageLabel label : imageLabels)
                        {
                            result = label.getText();
                        }

                        booleanMutableDetection.postValue(true);
                        stringMutableDetection.setValue(result);
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        booleanMutableDetection.postValue(false);
                        stringMutableDetection.setValue(e.getMessage());
                    }
                });

        return stringMutableDetection;
    }
}