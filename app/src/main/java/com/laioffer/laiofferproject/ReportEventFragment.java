package com.laioffer.laiofferproject;


import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MEDIA_PROJECTION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportEventFragment extends Fragment {

    private final static String TAG = ReportEventFragment.class.getSimpleName();
    private EditText mTextViewLocation;
    private EditText getmTextViewDest;
    private Button mReportButton;
    private DatabaseReference database;
    private String username;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private static int RESULT_LOAD_IMAGE = 1;
    private Button mSelectButton;
    private ImageView mImageView;
    private String mPicturePath = "";
    private EditText mTextViewTitle;

    public ReportEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_event, container, false);
        mTextViewLocation = (EditText) view.findViewById(R.id.text_event_location);
        mTextViewTitle = (EditText) view.findViewById(R.id.text_event_title);

        checkPermission();
        mImageView = (ImageView) view.findViewById(R.id.img_event_pic);
        mSelectButton = (Button) view.findViewById(R.id.button_select);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        getmTextViewDest = (EditText) view.findViewById(R.id.text_event_description);
        mReportButton = (Button) view.findViewById(R.id.button_report);

        username = ((EventActivity)getActivity()).getUsername();

        database = FirebaseDatabase.getInstance().getReference();

        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = uploadEvent();
                if (!mPicturePath.equals("")) {
                    uploadImage(mPicturePath, key);
                    mPicturePath="";
                }
            }
        });

        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI //a class used to search URI
                );
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void  onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: sign_in: " + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged: sign_out");
                }
            }
        };

        mAuth.signInAnonymously().addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInAnonymously", task.getException());
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();                                             // find corresponding file and database
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(selectedImage,filePathColumn,null,null,null);  // cursor is a reference pointing to a row in database
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]); // get column(URI path) in specific row.
            String picturePath = cursor.getString(columnIndex); //returns the value of the requested column as a String.
            cursor.close();
            Log.e(TAG, picturePath);
            mPicturePath = picturePath;
            mImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath)); //in android, image has to transfer to bitmap to show
            mImageView.setVisibility(View.VISIBLE); // show image view.
        }
    }

    private String uploadEvent() {
        String location = mTextViewLocation.getText().toString();
        String description = getmTextViewDest.getText().toString();
        String title = mTextViewTitle.getText().toString();

        if (location.equals("") || description.equals("")) {
            return "";
        }
        //creat event instance
        Event event = new Event();
        event.setDescription(description);
        event.setLocation(location);
        event.setTime(System.currentTimeMillis());
        event.setUsername(username);
        event.setTitle(title);

        String key = database.child("events").push().getKey();
        event.setId(key);
        database.child("events").child(key).setValue(event, new DatabaseReference.CompletionListener() {   //callback function indicate the actions after complete
                                                                                                            // database operation
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(getContext(), "The events is failed, please check your network status.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "The event is reported", Toast.LENGTH_SHORT).show();
                    mTextViewLocation.setText("");
                    getmTextViewDest.setText("");
                    mImageView.setVisibility(View.GONE);
                }
            }
        });
        return key;
    }

    // If your app needs a dangerous permission, you must check whether you have that permission every time you perform an operation that requires that permission.
    // The user is always free to revoke the permission, so even if the app used the camera yesterday, it can't assume it still has that permission today.
    // To check if you have a permission, call the ContextCompat.checkSelfPermission() method.
    //If the app has the permission, the method returns PackageManager.PERMISSION_GRANTED, and the app can proceed with the operation.
    // If the app does not have the permission, the method returns PERMISSION_DENIED, and the app has to explicitly ask the user for permission.

    // this a security mechanism

    private void checkPermission() {      // check whether have the permission to check the hard disk;
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
    }

    private void uploadImage(final String imgPath, final String eventId) {
        Uri file = Uri.fromFile(new File(imgPath));
        StorageReference imgRef = storageRef.child("images/" + System.currentTimeMillis() + "_" + file.getLastPathSegment());
        UploadTask uploadTask = imgRef.putFile(file);

        //Register observers to listen when the download is completed or if it is failed;
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Handle unsuccessful uploads;
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUri = taskSnapshot.getDownloadUrl();
                Log.i(TAG, "upload successful");
                database.child("events").child(eventId).child("imgUri").setValue(downloadUri.toString());
            }
        });
    }

}
