package tumblrr.utd.com.abg160130_asg5;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * MainActivity is used to display the list of contacts the user phone has.
 * Here we read the contacts stored in local database from text file (/ContactInfo/contact.txt)
 * Created by Abhilash Gudasi on 3/29/2018.
 * Abhilash Gudasi : abg160130
 * Paras Bansal : pxb162530
 */

public class MainActivity extends AppCompatActivity {

    public ListView ContactlistView;
    public String path  = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ContactInfo"; //Path to the folder where the file is stored
    File myDirs = new File(path);
    File file =new File(path+"/contacts.txt");
    private static final int REQUEST_WRITE_STORAGE = 112;

    /*
    Method to display when the app starts. Here we display list of contacts along with menu option
    for user to add contact, view contact and search for a particular contact
    Author: Abhilash Gudasi
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
        loadContacts();
    }
    /*
    Method to load contacts from local database.
    This function also handles display of each contact when selected seperately
    Author: Abhilash Gudasi
     */
    protected void loadContacts(){
        ContactlistView = (ListView) findViewById(R.id.listViewContact);
        if(!myDirs.exists()){
            myDirs.mkdirs();
        }
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedReader readContacts = null;
            readContacts = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"), 100); //Open the file to read in ANSI format

            //declaring data structures to read contacts from text file
            String line;
            String[] lineDetail = new String[3];
            ArrayList<String> lines = new ArrayList<String>(); //Array list to store each line from the file

            //Read each Line from the file
            while ((line = readContacts.readLine()) != null){
                lineDetail=line.split("\t");        //Split the line by tab seperator
                lines.add(lineDetail[0]+"\t"+lineDetail[1]+"\t"+lineDetail[2]); //First Name, Last name, Phone Number
            }

            readContacts.close();//Close the Buffer reader

            Collections.sort(lines);//Sort all the lines
            Adapter contactAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lines); //Adapter for making view for each contact

            ContactlistView.setAdapter((ListAdapter)contactAdapter);

            //ClickListener: if any contact is clicked
            ContactlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String value=(String) ContactlistView.getItemAtPosition(position); //Get the contents of the contact clicked
                    Intent contactIntent = new Intent(view.getContext(),ViewContact.class); //Go to another activity to show contact detail
                    contactIntent.putExtra("key",value);//Send contact details
                    startActivity(contactIntent);
                }
            });
            readContacts.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadContacts();
    }

    /*
    Method for Menu options on Action bar
    Author : Paras Bansal
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
    Method to handle selected item in menu
    Author : Paras Bansal
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent ToAdd = new Intent(MainActivity.this,AddContact.class);
                ToAdd.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(ToAdd,1);
                return true;
            case R.id.search:
                Intent ToSearch = new Intent(MainActivity.this,MainActivity.class);
                ToSearch.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                //startActivity(ToSearch);
                btnSearch(findViewById(R.id.container));
                return true;
            case R.id.contacts:
                Intent ToHome = new Intent(MainActivity.this,MainActivity.class);
                ToHome.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(ToHome);
                return true;
            case R.id.exit:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return(true);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    Method to handle startActivityonResult
    Author : Abhilash Gudasi
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                Toast toast= Toast.makeText(getApplicationContext(),"[Returned]: "+result,Toast.LENGTH_SHORT);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast toast= Toast.makeText(getApplicationContext(),"[Contact] No changes",Toast.LENGTH_SHORT);
                toast.setMargin(50,50);
                toast.show();
            }
        }
    }

    public EditText txtSearch;
    public ListView SearchlistView;

    /*
    Method to handle search button to display corresponding contacts based on search text
    Author : Abhilash Gudasi
     */
    public void btnSearch(View view) {
        txtSearch= (EditText) findViewById(R.id.textLive);
        SearchlistView = (ListView) findViewById(R.id.listViewContact);
        SearchlistView.setAdapter(null);
        String keyword=String.valueOf(txtSearch.getText());
        try {
            BufferedReader readContacts = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"), 100);
            String line;
            String[] lineDetail = new String[4];
            ArrayList<String> lines = new ArrayList<String>();
            Adapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lines);
            while ((line = readContacts.readLine()) != null) {
                lineDetail=line.split("\t");
                if (keyword.toLowerCase().equals(lineDetail[0].toLowerCase())){
                    lines.add(lineDetail[0]+"\t"+lineDetail[1]+"\t"+lineDetail[2]);
                }
            }
            readContacts.close();
            SearchlistView.setVisibility(View.VISIBLE);
            if(lines.isEmpty())
            {
                lines.add("No Contacts Found");
            }
            Collections.sort(lines);
            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lines);
            SearchlistView.setAdapter((ListAdapter)adapter); //Show Contact List
            SearchlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int itemposition=position;
                    String value=(String) SearchlistView.getItemAtPosition(position);
                    Intent ToView = new Intent(view.getContext(),ViewContact.class);
                    ToView.putExtra("key",value);
                    startActivity(ToView);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    Method to handle permission requests
    Author : Paras Bansal
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //reload my activity with permission granted or use the features what required the permission
                    Toast.makeText(MainActivity.this, "The app was allowed to access storage", Toast.LENGTH_LONG).show();
                } else
                {
                    Toast.makeText(MainActivity.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}
