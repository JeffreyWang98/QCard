package com.meow.qcard.loginandregistration;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;

import java.util.List;

public class ContactLiveData extends LiveData<List<String>> {
    private final Context context;

    public ContactLiveData(Context context) {
        this.context = context;
        loadData();
    }

    private void loadData() {
        new CursorLoader(context,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ResetPasswordActivity.ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        @SuppressWarnings("unused")
        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}
