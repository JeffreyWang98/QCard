package com.meow.qcard.loginandregistration;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class ContactModel extends AndroidViewModel {
    private final ContactLiveData data;

    public ContactModel(Application application) {
        super(application);
        data = new ContactLiveData(application);
    }

    public LiveData<List<String>> getData() {
        return data;
    }
}
