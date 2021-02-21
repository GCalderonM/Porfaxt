package me.guillermocalderon.porfaxtfinal.ui.zip;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ZIPViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ZIPViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Comprime todo tipo de archivos! (PDF, XML, DOCX, WORD, etc...)");
    }

    public LiveData<String> getText() {
        return mText;
    }
}