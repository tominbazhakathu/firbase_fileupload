package com.collegeproject.mysocialmedia.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.collegeproject.mysocialmedia.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    private DrawerLayout drawer;
    private NavController navController;
    private NavigationView navigationView;

    private String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};

    private int permsRequestCode = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab_file_upload);
        fab.setOnClickListener(view -> startActivity(new Intent(HomePageActivity.this, FileUploadActivity.class)));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, permsRequestCode);
        }
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_myfiles)
//                .setDrawerLayout(drawer)
//                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, drawer);
        NavigationUI.setupWithNavController(navigationView, navController);
        Navigation.setViewNavController(navigationView,navController);

        navigationView.setNavigationItemSelectedListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        if (permsRequestCode == this.permsRequestCode) {
            boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            checkPermmsionDenied(storageAccepted);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermmsionDenied(boolean storageAccepted) {
        if (!storageAccepted) {
            shouldShowRequestPermissionRationale(perms[0]);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, drawer)
                || super.onSupportNavigateUp();
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.onNavDestinationSelected(item, navController)
//                || super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);

        drawer.closeDrawers();

        int id = menuItem.getItemId();

        Log.d("NAV", "onNavigationItemSelected: " + id);

        switch (id) {

            case R.id.nav_home:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_home);
                break;

            case R.id.nav_myfiles:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_myfiles);
                break;

        }
        return true;
    }
}
