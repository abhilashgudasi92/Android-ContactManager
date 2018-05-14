package tumblrr.utd.com.abg160130_asg5;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * AddContact class is used to add a new contact.
 * Here the new contact is added in the local database in the form of text file (/ContactInfo/contact.txt)
 * Created by Abhilash Gudasi on 3/29/2018.
 * Abhilash Gudasi : abg160130
 * Paras Bansal : pxb162530
 */

public class AddContact extends AppCompatActivity{

    public EditText txtFirstName,txtLastName, txtPhoneNumber,txtEmail;
    public Button Save;
    String [] saveText = new String[4];
    public String path  = Environment.getExternalStorageDirectory() + "/ContactInfo";
    File myDirs = new File(path);

    private TextView mTextMessage;
    File file =new File(myDirs+"/contacts.txt");

    /*
    Method to display blank text field for adding a new contact.
    Author: Abhilash Gudasi
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addcontact);
        Save = (Button) findViewById(R.id.btnSave);
    }

    /*
    Method to take the user entered contact details and save in to database
    Author : Abhilash Gudasi
     */
    public void saveToContacts(android.view.View view) throws IOException {
        txtFirstName= (EditText) findViewById(R.id.txtFirstName);
        txtLastName= (EditText) findViewById(R.id.txtLastName);
        txtPhoneNumber= (EditText) findViewById(R.id.txtPhoneNumber);
        txtEmail= (EditText) findViewById(R.id.txtEmail);

        saveText[0] = String.valueOf(txtFirstName.getText());

        saveText[1] = String.valueOf(txtLastName.getText());

        saveText[2] = String.valueOf(txtPhoneNumber.getText());

        saveText[3] = String.valueOf(txtEmail.getText());


        //All fields except First name are optional
        if(saveText[0].length()==0)
        {
            txtFirstName.setBackgroundColor(Color.MAGENTA);
            Toast.makeText(getApplicationContext(),"First Name is Mandatory", Toast.LENGTH_LONG).show();
        }
        else{
            txtFirstName.setBackgroundColor(Color.WHITE);
            //Phone number field check
            if(saveText[2].length()!=0 && !saveText[2].matches("[-+]?\\d*\\.?\\d+"))
            {
                txtPhoneNumber.setBackgroundColor(Color.MAGENTA);
                Toast.makeText(getApplicationContext(),"Phone number should be number", Toast.LENGTH_LONG).show();
            }
            else{
                txtPhoneNumber.setBackgroundColor(Color.WHITE);
                if(saveText[1].length()==0)
                    saveText[1]=" ";
                if(saveText[2].length()==0)
                    saveText[2]=" ";
                if(saveText[3].length()==0)
                    saveText[3]=" ";
                Toast.makeText(getApplicationContext(),"Saved", Toast.LENGTH_LONG).show();
                Save(file,saveText);
            }
        }

        txtFirstName.setText("");
        txtLastName.setText("");
        txtPhoneNumber.setText("");
        txtEmail.setText("");
    }

    /*
    Method to save the contact in to the local database(text file)
    Author: Abhilash Gudasi
     */
    private void Save(File myfile,String[] data) throws IOException {
        if(!myDirs.exists()){
            myDirs.mkdirs();
        }
        if(!myfile.exists()){
            myfile.createNewFile();
        }
        FileOutputStream fos = null;
        OutputStreamWriter myOutWriter = null;
        try
        {
            fos = new FileOutputStream(file,true);
            myOutWriter = new OutputStreamWriter(fos);
        }
        catch (FileNotFoundException e)
        {e.printStackTrace();}
        try
        {
            for(int i=0;i<4;i++) {
                myOutWriter.append(data[i]);
                myOutWriter.append("\t");
            }
            myOutWriter.append("\n");

        }
        finally
        {
            myOutWriter.close();
            fos.close();

            Intent returnIntent = new Intent(this,MainActivity.class);
            String b = saveText.toString();
            returnIntent.putExtra("result",b.substring(1,b.length()-2).replace(",","\t"));
            setResult(AddContact.RESULT_OK,returnIntent);
            finish();
        }
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
                Intent ToAdd = new Intent(AddContact.this,AddContact.class);
                ToAdd.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(ToAdd);
                return true;
            case R.id.search:
                Intent ToSearch = new Intent(AddContact.this,MainActivity.class);
                ToSearch.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(ToSearch);
                return true;
            case R.id.contacts:
                Intent ToHome = new Intent(AddContact.this,MainActivity.class);
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
}
