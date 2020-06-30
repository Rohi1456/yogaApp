package appliedsyntax.io.yoga.acivity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import java.util.Objects;

import appliedsyntax.io.yoga.common.LoadDimensions;
import appliedsyntax.io.yoga.common.MySharedPreference;
import appliedsyntax.io.yoga.fragment.FragmentClasses;
import appliedsyntax.io.yoga.fragment.FragmentHome;
import appliedsyntax.io.yoga.fragment.FragmentLocations;
import appliedsyntax.io.yoga.fragment.FragmentStudents;
import appliedsyntax.io.yoga.R;

public class ActivityHome extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout drawerLayout ;
    Toolbar toolbar;
    TextView students, locations, batches,home;
    TextView App_title;
    String currentFragment="";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__home);

        //Hiding the Action Bar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        actionBar.hide();
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);

        setUpNavigationMenu();
        App_title = findViewById(R.id.app_title);
        //custom menu accsessing

        students = findViewById(R.id.StudentsTVID); students.setOnClickListener(this);
        locations = findViewById(R.id.LocationsTVID); locations.setOnClickListener(this);
        batches = findViewById(R.id.BatchesTVID); batches.setOnClickListener(this);
        home = findViewById(R.id.homeTVID); home.setOnClickListener(this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = getIntent().getExtras();
        if(getIntent()!=null&& bundle!=null && bundle.get("fragment")!=null)
        {

            if(Objects.requireNonNull(bundle.get("fragment")).toString().equals("students"))
            {
                //Setting Student Fragment
                fragmentTransaction.replace(R.id.frame_layout, new FragmentStudents());
                currentFragment = "student";
            }
        }
        else {
            //Setting Home Fragment
            fragmentTransaction.replace(R.id.frame_layout, new FragmentHome());
            currentFragment = "home";
        }
        fragmentTransaction.commit();

        //check first time open
        String first_Open = MySharedPreference.getPreferences(this,"firstOpen");
        if(first_Open.equals(""))
        {
            LoadDimensions.calculate(this);
            MySharedPreference.setPreference(this,"firstOpen" , "Yes");
        }
      /*  else
        {
            Toast.makeText(this, "Already Opened", Toast.LENGTH_SHORT).show();
        }*/

    }
    private void setUpNavigationMenu() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();

    }

    @Override
    public void onClick(View view) {

        drawerLayout.closeDrawer(GravityCompat.START);
        Fragment fragment = null;
        switch (view.getId())
        {
            case R.id.StudentsTVID:
            {
                if(!currentFragment.equals("student")) {
                    currentFragment="student";
                    App_title.setText(R.string.students_title);
                    students.setTextColor(getResources().getColor(R.color.colorAccent));
                    locations.setTextColor(getResources().getColor(R.color.white));
                    batches.setTextColor(getResources().getColor(R.color.white));
                    home.setTextColor(getResources().getColor(R.color.white));
                    fragment = new FragmentStudents();
                }
                break;
            }
            case R.id.LocationsTVID: {
                if (!currentFragment.equals("locations"))
                {
                    currentFragment="locations";
                    locations.setTextColor(getResources().getColor(R.color.colorAccent)); //red
                students.setTextColor(getResources().getColor(R.color.white));//blue
                batches.setTextColor(getResources().getColor(R.color.white));
                home.setTextColor(getResources().getColor(R.color.white));
                fragment = new FragmentLocations();
                App_title.setText(R.string.locations_title);
            }
                break;
            }
            case R.id.BatchesTVID:
            {
                if(!currentFragment.equals("classes")) {
                    currentFragment="classes";
                    fragment = new FragmentClasses();
                    locations.setTextColor(getResources().getColor(R.color.white));
                    students.setTextColor(getResources().getColor(R.color.white));
                    batches.setTextColor(getResources().getColor(R.color.colorAccent));
                    home.setTextColor(getResources().getColor(R.color.white));
                    App_title.setText(R.string.classes_title);
                }
                break;
            }

            case R.id.homeTVID:
            {
                if(!currentFragment.equals("home")) {
                    currentFragment = "home";
                    App_title.setText(R.string.home_title);
                    locations.setTextColor(getResources().getColor(R.color.white));
                    students.setTextColor(getResources().getColor(R.color.white));
                    batches.setTextColor(getResources().getColor(R.color.white));
                    home.setTextColor(getResources().getColor(R.color.colorAccent));
                    fragment = new FragmentHome();
                }
                break;
            }

        }
        if(fragment!=null)
        {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,fragment);
            fragmentTransaction.commit();
        }

    }

}


