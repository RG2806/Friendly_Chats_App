package com.google.firebase.udacity.friendlychat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditorActivity extends AppCompatActivity {
        public static final int DEFAULT_MSG_LENGTH_LIMIT = 100;
        private static final int RC_PHOTO_PICKER = 2;
        private List<String> images=new ArrayList<>();
        private List<String> reviews=new ArrayList<>();
    private static final int RC_SIGN_IN = 1;
        EditText mNameEditText;
        Button mSendButton2;
        ImageButton mPhotoPickerButton2;
    private ListView mRestaurantListView;
    private Uri downloadUri;
    private MessageAdapter mMessageAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReferenece;
    private Uri selectedImageUri;
    private StorageReference photoRef;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent=getIntent();
        final String mUsername=intent.getExtras().getString("username");
        setTitle("Add a Restaurant");
        mRestaurantListView = (ListView) findViewById(R.id.restaurant_ListView);
        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        final EditText mReviewEditText= (EditText) findViewById(R.id.edit_restaurant_review);
        mSendButton2 = (Button) findViewById(R.id.sendButton2);
        mPhotoPickerButton2 = (ImageButton) findViewById(R.id.photoPickerButton2);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        mFirebaseStorage= FirebaseStorage.getInstance();
        mChatPhotosStorageReferenece = mFirebaseStorage.getReference().child("chat_photos");
        List<FriendlyMessage> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton2.setEnabled(true);
                } else {
                    mSendButton2.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mNameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
        mSendButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click
                if (downloadUri.toString() != null && mReviewEditText.getText()!=null && mUsername!=null) {
                    mNameEditText.getText().toString();
                    FriendlyMessage friendlyMessage = new FriendlyMessage(mNameEditText.getText().toString(), images, mReviewEditText.getText().toString(), mUsername);
                    mMessagesDatabaseReference.push().setValue(friendlyMessage);
                    mMessageAdapter.add(friendlyMessage);
                    // Clear input box
                    mNameEditText.setText("");
                    finish();
                }
            }
        });

        mPhotoPickerButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case RC_PHOTO_PICKER:
                if (resultCode == RESULT_OK) {
                    if(data.getClipData()!=null)
                    {for(int i=0; i<data.getClipData().getItemCount(); i++) {
                        selectedImageUri = data.getData();
                        assert selectedImageUri != null;
                        photoRef =  mChatPhotosStorageReferenece.child(selectedImageUri.getLastPathSegment());
                        // Upload file to Firebase Storage
                        photoRef.putFile(selectedImageUri)
                                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw Objects.requireNonNull(task.getException());
                                        }

                                        // Continue with the task to get the download URL
                                        return photoRef.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    downloadUri = task.getResult();
                                    images.add(downloadUri.toString());
                                    Toast.makeText(EditorActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Handle failures
                                    // ...
                                }
                            }
                        });
                    }}
                    else if(data.getData()!=null)
                    {
                        Uri selectedImageUri = data.getData();
                        photoRef = mChatPhotosStorageReferenece.child(selectedImageUri.getLastPathSegment());

                        // Upload file to Firebase Storage
                        photoRef.putFile(selectedImageUri)
                                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw task.getException();
                                        }

                                        // Continue with the task to get the download URL
                                        return photoRef.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    downloadUri = task.getResult();
                                    images.add(downloadUri.toString());
                                    Toast.makeText(EditorActivity.this, "Upload Sucessful", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Handle failures
                                    // ...
                                }
                            }
                        });
                    }
                    // Get a reference to store file at chat_photos/<FILENAME>

                }
                break;

            default:
                Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
