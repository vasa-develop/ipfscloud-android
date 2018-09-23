package com.tbc.vasa.ipfscloud;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Documents extends AppCompatActivity {

    private String TAG = "FIREBASE: ";
    private SharedPreferences settings;
    private FirebaseFirestore firestore;
    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        //TODO: Add a navigation drawer using the menu file in the res/menu


        //TODO: Below I am fetching some data. Add a grid recycler view for it(with 2 items in each row) NO NEED TO POPULATE THE DATA. JUST ADD THE FIELDS ACCORDING TO THE DATA FIELDS GIVEN BELOW IN THE ADAPTER.
        //Use cloud.gif as the loader image while fetching the data.

        //Format of data:
        /*

        QmVJcQM9GT45a1dNwzMKCEZRDyT8bw7Nap6xZGvFRGDcSo=
        {
        name=blockchian.png,
        isSavedOnBlockchain=false,
        contentType=png, size=66 KB,
        ipfsHash=QmVJcQM9GT45a1dNwzMKCEZRDyT8bw7Nap6xZGvFRGDcSo
        }

          */

        settings = getSharedPreferences("account", MODE_PRIVATE);
        // Access a Cloud Firestore instance from your Activity

        FirebaseApp.initializeApp(this);

        firestore = FirebaseFirestore.getInstance();


        

        docRef = firestore.collection("users").document(settings.getString("pubKey1", ""));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }
}
