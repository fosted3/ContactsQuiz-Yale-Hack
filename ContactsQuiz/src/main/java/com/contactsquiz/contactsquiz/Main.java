package com.contactsquiz.contactsquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Activity {

    List<Person> a;
    int i;
    String rand;
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
        public String getRand() {
        int maximum = 999999999;
        int rand = (int)(Math.random()*maximum);
        String str = Integer.toString(rand);
        while (str.length() < 10)
        { str = "0" + str; }
        return "+1 " + str.substring(0, 3) + "-" + str.substring(3, 6) + " - " + str.substring(6);
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
    public void nextPage(View view)
    {
        setContentView(R.layout.activity_main);

        displaycontacts();
    }
    public void displaycontacts(){

        a = getContactList();
        i=0;

        RecursiveNancy();


    }

    public void RecursiveNancy(){
        i++;
        new AlertDialog.Builder(this)
                .setTitle(a.get(i).getName())
                .setMessage("Which one is correct?")
                .setPositiveButton(a.get(i).getPhoneNum(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(i < a.size()){
                        RecursiveNancy();

                        }
                    }
                })

                .setNegativeButton(getRand(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (i < a.size()) {
                            RecursiveNancy();

                        }
                    }
                })
                .show();


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
                            contactList.add(aContact);

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
}

