package com.app.precared.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.precared.R;
import com.app.precared.utils.PrecaredSharePreferences;
import com.app.precared.utils.StringUtils;

public class HomeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Menu mMenu;
    private RelativeLayout mHeaderView;
    private Button mChatWithAdmin, mAddProduct, mProductList, mKnowMore;
    private PrecaredSharePreferences mPrecaredSharePreferences;
    private TextView mUserNameTextView;
    private TextView mUserEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mPrecaredSharePreferences = new PrecaredSharePreferences(this);
        setToolbar();
        findIDs();
        initializeNavigationDrawer();
        setUpHeaderView();
    }

    private void findIDs() {
        mChatWithAdmin = (Button) findViewById(R.id.chatWithAdmin);
        mAddProduct = (Button) findViewById(R.id.addProductBtn);
        mProductList = (Button) findViewById(R.id.productListBtn);
        mKnowMore = (Button) findViewById(R.id.knowMoreBtn);

        mChatWithAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ChatActivity.class));
            }
        });

        mAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SellerActivity.class));
            }
        });
        mProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProductListing.class));
            }
        });

        mKnowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Set Toolbar : Add app name as title and app logo as toolbar logo
     */
    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("");
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * initialize navigation drawer and set up navigation content data
     */
    private void initializeNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        if (mNavigationView != null) {
            setUpDrawerContent(mNavigationView);
        }
    }

    /**
     * set up navigation content data
     */
    private void setUpDrawerContent(NavigationView mNavigationView) {
        mNavigationView.setVerticalScrollBarEnabled(false);
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_home:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.item_seller:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(HomeActivity.this, SellerActivity.class));
                        break;
                    case R.id.item_chat:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(HomeActivity.this, ChatActivity.class));
                        break;
                    case R.id.item_logout:
                        mDrawerLayout.closeDrawers();
                        showLogoutDialog();
                        break;
                    case R.id.item_refer_and_earn:
                        mDrawerLayout.closeDrawers();
                        final Intent sendIntent = new Intent();
                        sendIntent.setType("text/plain");
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Refer a Friend");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Refer body come here");
                        startActivity(Intent.createChooser(sendIntent, "Refer"));
                        break;
                }
                return true;
            }
        });
    }

    /**
     * set User Layout
     */
    private void setUpHeaderView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mHeaderView = (RelativeLayout) inflater.inflate(R.layout.layout_drawer_header, null, false);
        // get widgets
        mUserNameTextView = (TextView) mHeaderView.findViewById(R.id.userNameTextView);
        mUserEmailTextView = (TextView) mHeaderView.findViewById(R.id.userEmailTextView);

        mNavigationView.addHeaderView(mHeaderView);
        setUpHeaderViewData();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.addDrawerListener(toggle);
    }

    /**
     * set header view data
     */
    private void setUpHeaderViewData() {
        if (mPrecaredSharePreferences.isLoggedIn()) {
            String userName = mPrecaredSharePreferences.getName();
            String email = mPrecaredSharePreferences.getEmail();
            mUserNameTextView.setText(StringUtils.isNotEmpty(userName) ? userName : getString(R.string.sign_in_text));
            mUserEmailTextView.setText(StringUtils.isNotEmpty(email) ? email : "");
        }
    }

    /**
     * show logout dialog
     */
    private void showLogoutDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(getString(R.string.logout_title)).setMessage(getString(R.string.logout_msg)).setPositiveButton(getString(R.string.yes_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPrecaredSharePreferences.clearPreferences();
                finish();

            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.secondary_text));
    }

    public void onClickKnowMore(View view) {

    }
}