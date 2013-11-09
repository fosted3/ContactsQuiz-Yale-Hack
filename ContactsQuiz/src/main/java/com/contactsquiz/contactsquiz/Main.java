package com.contactsquiz.contactsquiz;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;
//import android.app.LoaderManager.LoaderCallbacks;
import android.provider.ContactsContract;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;
import android.view.View;
import android.content.Intent;
import android.widget.SimpleCursorAdapter;
import android.net.Uri;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.telephony.TelephonyManager;


public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    public void check(View view)
    {
        //Intent intent = new Intent(this, DisplayMessageActivity.class);

    }
    public void nextPage(View view)
    {
        setContentView(R.layout.activity_main);
        displaycontacts();
    }
    public void displaycontacts(){

        List<Person> a = getContactList();
        for (int i=0; i < a.size(); i++){
            //allert
            Log.v("value:", a.get(i).getName());
            //prompt
            //wait for user
            //on response continue
            //return correct/false in background
        }

    }
    public List<Person> getContactList(){
        ArrayList<Person> contactList = new ArrayList<Person>();

        Uri contactUri = ContactsContract.Contacts.CONTENT_STREQUENT_URI;
        String[] PROJECTION = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
        };
        String SELECTION = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";
        Cursor contacts = getContentResolver().query(contactUri, PROJECTION, SELECTION, null, null);


        if (contacts.getCount() > 0)
        {
            while(contacts.moveToNext()) {
                Person aContact = new Person();
                int idFieldColumnIndex = 0;
                int nameFieldColumnIndex = 0;
                int numberFieldColumnIndex = 0;

                String contactId = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts._ID));

                nameFieldColumnIndex = contacts.getColumnIndex(PhoneLookup.DISPLAY_NAME);
                if (nameFieldColumnIndex > -1)
                {
                    aContact.setName(contacts.getString(nameFieldColumnIndex));
                }

                PROJECTION = new String[] {Phone.NUMBER};
                //noinspection deprecation
                final Cursor phone = managedQuery(Phone.CONTENT_URI, PROJECTION, Data.CONTACT_ID + "=?", new String[]{String.valueOf(contactId)}, null);
                if(phone.moveToFirst()) {
                    while(!phone.isAfterLast())
                    {
                        numberFieldColumnIndex = phone.getColumnIndex(Phone.NUMBER);
                        if (numberFieldColumnIndex > -1)
                        {
                            aContact.setPhoneNum(phone.getString(numberFieldColumnIndex));
                            phone.moveToNext();
                            TelephonyManager mTelephonyMgr;
                            mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                            //if (!mTelephonyMgr.getLine1Number().contains(aContact.getPhoneNum()))
                        //    {
                                contactList.add(aContact);
                        //    }
                        }
                    }
                }
                phone.close();
            }

            contacts.close();
        }

        return contactList;
    }
    public class Person {
        String myName = "";
        String myNumber = "";

        public String getName() {
            return myName;
        }

        public void setName(String name) {
            myName = name;
        }

        public String getPhoneNum() {
            return myNumber;
        }

        public void setPhoneNum(String number) {
            myNumber = number;
        }
    }
/*    public void displaycontacts()
    {
    Cursor cur = getContacts();
    cur.moveToFirst();
     if(!cur.isAfterLast())
    {
        do{
            String entry = cur.getString(cur.getColumnIndex(KEY_ENTRY));
            Log.v("value:", entry);
            cur.moveToNext();
        }while(!cur.isAfterLast());
    }

  //  list;
}

    private Cursor getContacts() {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_STREQUENT_URI;

        String[] projection =
                new String[]{ ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME +
                " COLLATE LOCALIZED ASC";
        //noinspection deprecation
        return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
    }*/
}
