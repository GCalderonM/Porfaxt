package me.guillermocalderon.porfaxtfinal.ui.xml;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class XMLViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public XMLViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("NOT IMPLEMENTED YET");
    }

    public LiveData<String> getText() {
        return mText;
    }
}