package me.guillermocalderon.porfaxtfinal.ui.pdf;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PDFViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PDFViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("NOT IMPLEMENTED YET");
    }

    public LiveData<String> getText() {
        return mText;
    }
}