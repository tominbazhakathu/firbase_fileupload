package com.collegeproject.mysocialmedia.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.collegeproject.mysocialmedia.Beans.FileDBEntry;
import com.collegeproject.mysocialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class FileUploadActivity extends AppCompatActivity {


    private static final int PICK_FILE_REQUEST = 1001;
    private RelativeLayout btnFilePickUp;
    private Uri filePath;
    private String fileName;
    private boolean isPrivate;
    private TextView textFileName;
    private EditText textFileDescription;
    private Button fileUploadButton;
    private RadioGroup radioGroup;
    private RadioButton radioPrivate, radioPublic;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private DatabaseReference dbReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);

        btnFilePickUp = findViewById(R.id.file_upload_layout);
        textFileName = findViewById(R.id.text_file_upload);
        textFileDescription = findViewById(R.id.edit_file_description);
        fileUploadButton = findViewById(R.id.button_file_upload);

        radioGroup = findViewById(R.id.radio_group_file_privacy);
        radioPrivate = findViewById(R.id.radio_private_file);
        radioPublic = findViewById(R.id.radio_public_file);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        dbReference = FirebaseDatabase.getInstance().getReference();

        btnFilePickUp.setOnClickListener(view -> showFileChooser());
        fileUploadButton.setOnClickListener(view -> uploadFile());

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> isPrivate = getPrivacy(checkedId));

    }

    private boolean getPrivacy(int checkedId) {
        if (checkedId == radioPrivate.getId()) {
            return true;
        } else {
            return false;
        }
    }

    private void uploadFile() {
        if (filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference fileRef = storageReference.child(fileName);
//            StorageReference riversRef = storageReference.child(firebaseUser.getUid()+ File.pathSeparator+getFileName(filePath));
            fileRef.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        //if the upload is successfull
                        //hiding the progress dialog
                        progressDialog.dismiss();

                        //and displaying a success toast
                        Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        addDataToDatabase();
                    })
                    .addOnFailureListener(exception -> {
                        //if the upload is not successfull
                        //hiding the progress dialog
                        progressDialog.dismiss();

                        //and displaying error message
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        //calculating progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

    private void addDataToDatabase() {

        isPrivate = getPrivacy(radioGroup.getCheckedRadioButtonId());



        FileDBEntry fileDBEntry = new FileDBEntry();
        fileDBEntry.setEntryId(dbReference.child("files").push().getKey());
        fileDBEntry.setDateOfAddition(Calendar.getInstance().getTime());
        fileDBEntry.setPrivate(isPrivate);
        fileDBEntry.setDeleted(false);
        fileDBEntry.setFileDescription(textFileDescription.getText().toString().trim());
        fileDBEntry.setUrlPath(fileName);
        fileDBEntry.setFileName(getFileName(filePath));
        fileDBEntry.setUserID(firebaseUser.getUid());
        fileDBEntry.setUserName(firebaseUser.getDisplayName());


        dbReference.child("files").child(fileDBEntry.getEntryId()).setValue(fileDBEntry);

        finish();



    }

    private void showFileChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            tex = data.getData();
            filePath = data.getData();
            fileName = firebaseUser.getUid() + "/" + getFileName(filePath);
            textFileName.setText(getFileName(filePath));

        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}
