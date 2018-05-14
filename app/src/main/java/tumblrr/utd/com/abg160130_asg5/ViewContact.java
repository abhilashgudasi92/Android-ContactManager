package tumblrr.utd.com.abg160130_asg5;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * ViewContact class is used to display the user selected contact.
 * User is also given option to modify and delete the contact through buttons on screen.
 * Created by Abhilash Gudasi on 3/29/2018.
 * Abhilash Gudasi : abg160130
 * Paras Bansal : pxb162530
 */

public class ViewContact extends AppCompatActivity {

    public String path  = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ContactInfo";
    File myDirs = new File(path);
    public TextView txtView1,txtView2,txtView3,txtView4;
    public Button Modify,Delete,Save;
    AlertDialog dialog;
    int selectTextView=0;

    String line;
    String[] lineDetail = new String[5];
    String [] saveText = new String[5];
    EditText editText;
    File file =new File(path+"/contacts.txt");
    File file2 =new File(path+"/temp.txt");

    /*
    Method to display single contact which user selected either to modify or delete.
    Here user can modify the contact details or delete the contact.
    Author: Paras Bansal
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontact);
        txtView1=(TextView)findViewById(R.id.txtFirstName);
        txtView2=(TextView)findViewById(R.id.txtLastName);
        txtView3=(TextView)findViewById(R.id.txtPhoneNumber);
        txtView4=(TextView)findViewById(R.id.txtEmail);
        Modify = (Button) findViewById(R.id.btnModify);
        Delete = (Button) findViewById(R.id.btnDelete);
        Save = (Button) findViewById(R.id.btnSave);

        //Read file to compare intent extra key value and display corresponding contact
        BufferedReader contactread = null;
        Bundle extras = getIntent().getExtras();
        String value = extras.getString("key");
        String[] valueDetail = new String[4];
        valueDetail=value.split("\t");
        Modify.setVisibility(View.VISIBLE);
        Delete.setVisibility(View.VISIBLE);
        Save.setVisibility(View.INVISIBLE);
        try {
            contactread = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"), 100);
            while ((line = contactread.readLine()) != null) {
                lineDetail = line.split("\t");
                if (valueDetail[0].equals(lineDetail[0])&&valueDetail[1].equals(lineDetail[1])&&valueDetail[2].equals(lineDetail[2])) {
                    txtView1.setText(lineDetail[0]);
                    txtView2.setText(lineDetail[1]);
                    txtView3.setText(lineDetail[2]);
                    txtView4.setText(lineDetail[3]);
                    break;
                }
            }
            contactread.close();

            dialog = new AlertDialog.Builder(this).create();
            editText=new EditText(this);
            dialog.setTitle("Edit The text");
            dialog.setView(editText);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE,"Save", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int i) {
                    if(selectTextView==1)
                        txtView1.setText(editText.getText());
                    if(selectTextView==2)
                        txtView2.setText(editText.getText());
                    if(selectTextView==3)
                        txtView3.setText(editText.getText());
                    if(selectTextView==4)
                        txtView4.setText(editText.getText());
                }
            });
            txtView1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    editText.setText(txtView1.getText());
                    dialog.show();
                    selectTextView=1;
                }
            });
            txtView2.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    editText.setText(txtView2.getText());
                    dialog.show();
                    selectTextView=2;
                }
            });
            txtView3.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    editText.setText(txtView3.getText());
                    dialog.show();
                    selectTextView=3;
                }
            });
            txtView4.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    editText.setText(txtView4.getText());
                    dialog.show();
                    selectTextView=4;
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    Method to delete the contact from database
    Author: Paras Bansal
     */
    public void btnDeleteContact(android.view.View view) {
        String selectedLine = line;
        if(file2.exists())
        {
            file2.delete();
        }
        BufferedReader contactread = null;
        try {
            contactread = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"), 2);
            while ((line = contactread.readLine()) != null) {
                lineDetail = line.split("\t");
                if(selectedLine.equals(line))
                {
                    continue;
                }
                else{
                    Save(file2, lineDetail,true);
                }
            }
            contactread.close();
        } catch(UnsupportedEncodingException e){
            e.printStackTrace();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
            file.delete();
            file2.renameTo(file);
            Intent ToHome = new Intent(ViewContact.this,MainActivity.class);
            startActivity(ToHome);
        }
    }

    /*
    Method to modify the contact and update the corresponding new contact data by replacinf old
    contact in the database
    Author: Abhilash Gudasi
     */
    public void btnModifyContact(android.view.View view) throws IOException {
        String selectedLine = line;
        if(file2.exists())
        {
            file2.delete();
        }
        BufferedReader contactread = null;
        try {
            contactread = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"), 2);
            while ((line = contactread.readLine()) != null) {
                lineDetail = line.split("\t");
                if(selectedLine.equals(line))
                {
                    continue;
                }
                else{
                    Save(file2, lineDetail,true);
                }
            }
            contactread.close();
        } catch(UnsupportedEncodingException e){
            e.printStackTrace();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            Toast.makeText(getApplicationContext(), "Modified contact", Toast.LENGTH_LONG).show();
            file.delete();
            file2.renameTo(file);
        }

        saveText[0] = String.valueOf(txtView1.getText());
        saveText[1] = String.valueOf(txtView2.getText());
        if(saveText[1].length()==0)
            saveText[1]=" ";
        saveText[2] = String.valueOf(txtView3.getText());
        if(saveText[2].length()==0)
            saveText[2]=" ";
        saveText[3] = String.valueOf(txtView4.getText());
        if(saveText[3].length()==0)
            saveText[3]=" ";

        //All fields except First name are optional
        if(saveText[0].length()==0)
        {
            txtView1.setBackgroundColor(Color.MAGENTA);
            Toast.makeText(getApplicationContext(),"First Name is Mandatory", Toast.LENGTH_LONG).show();
        }
        else {
            txtView1.setBackgroundColor(Color.WHITE);
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            Save(file, saveText,true);
        }
    }
    /*
    Method to save the contact in to the local database(text file)
    Author: Abhilash Gudasi
     */
    private void Save(File file, String[] data,boolean home) throws IOException {
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
        FileOutputStream fos = null;
        OutputStreamWriter myOutWriter = null;
        try
        {
            fos = new FileOutputStream(file,true);
            myOutWriter = new OutputStreamWriter(fos);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
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
                Intent ToAdd = new Intent(ViewContact.this,AddContact.class);
                ToAdd.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(ToAdd);
                return true;
            case R.id.search:
                Intent ToSearch = new Intent(ViewContact.this,MainActivity.class);
                ToSearch.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(ToSearch);
                return true;
            case R.id.contacts:
                Intent ToHome = new Intent(ViewContact.this,MainActivity.class);
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

