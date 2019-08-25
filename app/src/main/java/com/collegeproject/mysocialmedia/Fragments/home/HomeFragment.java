package com.collegeproject.mysocialmedia.Fragments.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.collegeproject.mysocialmedia.Adapter.HomeRecyclerAdapter;
import com.collegeproject.mysocialmedia.Beans.FileDBEntry;
import com.collegeproject.mysocialmedia.Interface.HomeFileClickInterface;
import com.collegeproject.mysocialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeFileClickInterface {

    private List<FileDBEntry> dataFromFirebase;
    private RecyclerView recyclerView;
    private HomeRecyclerAdapter homeRecyclerAdapter;
    private SwipeRefreshLayout refreshLayout;
    private FirebaseUser firebaseUser;
    private DatabaseReference dbReference;
    private StorageReference storageReference;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataFromFirebase = new ArrayList<>();
        recyclerView = view.findViewById(R.id.home_recycler);
        refreshLayout = view.findViewById(R.id.home_swipe_refresh);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        homeRecyclerAdapter = new HomeRecyclerAdapter(HomeFragment.this, firebaseUser.getUid(), dataFromFirebase);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(homeRecyclerAdapter);

        getDataFromDB();
        refreshLayout.setOnRefreshListener(this::getDataFromDB);

    }

    private void getDataFromDB() {

        dbReference.child("files").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataFromFirebase.clear();
                for (DataSnapshot filesDataSnapshot : dataSnapshot.getChildren()) {
                    FileDBEntry file = filesDataSnapshot.getValue(FileDBEntry.class);
                    if (file.isPrivate() && !file.getUserID().equals(firebaseUser.getUid())) {
                        continue;
                    }
                    dataFromFirebase.add(file);
                }
                homeRecyclerAdapter.notifyDataSetChanged();

                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //do nothing
            }
        });

    }

    @Override
    public void fileClickedForView(FileDBEntry fileDBEntry) {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Decrypting...");
        progressDialog.show();

        StorageReference fileRef = storageReference.child(fileDBEntry.getUrlPath());

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MyAppData"
                + File.separator + fileDBEntry.getUrlPath();

//        if (!filePath.exists()) {
//            if (parentDir.mkdirs()) {
//                Log.d(TAG, "Successfully created the parent dir:" + parentDir.getName());
//            } else {
//                Log.d(TAG, "Failed to create the parent dir:" + parentDir.getName());
//            }
//        }

        File directories = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MyAppData"
                + File.separator + fileDBEntry.getUserID());

        if (!directories.exists()) {
            directories.mkdirs();
        }

        File fileNameOnDevice = new File(filePath);

        fileRef.getFile(fileNameOnDevice).addOnSuccessListener(taskSnapshot -> {
            openFile(fileNameOnDevice);
            progressDialog.dismiss();
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Some Error Occured", Toast.LENGTH_SHORT).show();
            Log.d("Exception", "onFailure: " + e);
            Log.d("FILE PATH", "fileClickedForView: ");
        });
    }

    private void openFile(File fileNameOnDevice) {

        Uri uri;

        if (Build.VERSION.SDK_INT < 24) {
            uri = Uri.fromFile(fileNameOnDevice);
        } else {
            uri = Uri.parse(fileNameOnDevice.getPath());
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(Intent.createChooser(intent, "Open File"));
//        startActivity(intent);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}