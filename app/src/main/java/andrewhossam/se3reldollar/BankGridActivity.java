package andrewhossam.se3reldollar;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import static andrewhossam.se3reldollar.Constants.bankDrawables;
import static andrewhossam.se3reldollar.Constants.sharingIntent;

public class BankGridActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Bundle bundle = null;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_grid);
        MobileAds.initialize(this, getString(R.string.app_ad_id));

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        final Bundle analyticsBundle = new Bundle();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getSupportActionBar().setTitle(R.string.banks);
        GridView bankGridview = (GridView) findViewById(R.id.bank_gridview);
        bankGridview.setAdapter(new ImageAdapter(this, bankDrawables));
        bankGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                analyticsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(position));
                analyticsBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getResources().getStringArray(R.array.banks_short_name)[position]);
                analyticsBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, analyticsBundle);

                //Transition
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(BankGridActivity.this, v, v.getTransitionName()).toBundle();
                    startActivity(new Intent(getApplicationContext(), BankDetailsActivity.class).putExtra("imageRid", position), bundle);

                } else {

                    startActivity(new Intent(getApplicationContext(), BankDetailsActivity.class).putExtra("imageRid", position));
                }
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_bank_grid);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_bank_grid);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        }
        if (id == R.id.nav_banks) {
        } else if (id == R.id.nav_summery) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(new Intent(this, MainActivity.class), bundle);
                finish();
            } else {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }

        } else if (id == R.id.nav_gold) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(new Intent(this, GoldActivity.class), bundle);
                finish();
            } else {
                startActivity(new Intent(this, GoldActivity.class));
                finish();
            }
        } else if (id == R.id.nav_currency) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(new Intent(this, CurrencyActivity.class), bundle);
                finish();
            } else {
                startActivity(new Intent(this, CurrencyActivity.class));
                finish();
            }
        } else if (id == R.id.nav_share) {
            sharingIntent("https://fj8n4.app.goo.gl/ukph", BankGridActivity.this);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_bank_grid);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
