package com.example.leeseungchan.chulbalhama;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;

import com.example.leeseungchan.chulbalhama.UI.habit_list.HabitListFragment;
import com.example.leeseungchan.chulbalhama.UI.personal_info.PersonalInfoFragment;
import com.example.leeseungchan.chulbalhama.UI.test.TestFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    private FragmentManager fragmentManager;
    private HabitListFragment habitListFragment;
    private PersonalInfoFragment personalInfoFragment;
    private FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up toolbar on top
        Toolbar toolbarMain = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbarMain);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // drawable
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
               this, drawerLayout, toolbarMain, R.string.openDrawerNavigation,R.string.closeDrawerNavigation);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        habitListFragment = new HabitListFragment();
        personalInfoFragment = new PersonalInfoFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, habitListFragment).commitAllowingStateLoss();

        setTitle(R.string.app_name);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_main) {
            if(!habitListFragment.isAdded()){
                replaceFragment(habitListFragment);
                setTitle(R.string.app_name);
            }
        } else if (id == R.id.nav_info) {
            if(!personalInfoFragment.isAdded()){
                replaceFragment(personalInfoFragment);
                setTitle(R.string.title_personal);
            }
        }else if (id == R.id.nav_test) {
            if(!personalInfoFragment.isAdded()){
                replaceFragment(TestFragment.newInstance());
                setTitle(R.string.title_test);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment){
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment)
                .commitNowAllowingStateLoss();
    }

    @Override
    public void setTitle(int id){
        TextView title = (TextView)findViewById(R.id.toolbar_title);
        title.setText(id);
    }

}
