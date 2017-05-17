package andrewhossam.se3reldollar;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import static andrewhossam.se3reldollar.Constants.BANK;
import static andrewhossam.se3reldollar.Constants.EMPTY;
import static andrewhossam.se3reldollar.Constants.G18;
import static andrewhossam.se3reldollar.Constants.MODIFIED_AT;
import static andrewhossam.se3reldollar.Constants.USD_BUY;
import static andrewhossam.se3reldollar.Constants.USD_BUY_AVG;
import static andrewhossam.se3reldollar.Constants.USD_SELL;
import static andrewhossam.se3reldollar.Constants.USD_SELL_AVG;
import static andrewhossam.se3reldollar.Constants.VALUE;
import static andrewhossam.se3reldollar.Constants.getLastUpdate;
import static andrewhossam.se3reldollar.Constants.isConnectedToInternet;
import static andrewhossam.se3reldollar.Constants.roundingDouble;
import static andrewhossam.se3reldollar.Constants.sharingIntent;
import static andrewhossam.se3reldollar.R.id.lastUpdateMain;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String SUMMARY = "summary";
    public Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Bundle bundle = null;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        //tranastion


        MobileAds.initialize(this, getString(R.string.app_ad_id));
        ((AdView) findViewById(R.id.adView)).loadAd(new AdRequest.Builder().build());
        if (!isConnectedToInternet(MainActivity.this)) {
            Toast.makeText(MainActivity.this, R.string.InternetError, Toast.LENGTH_LONG).show();
            //    startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            //   finish();
        }
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //  Toast.makeText(mContext, "aaaaaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
                }
            });
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        final SharedPreferences summary = getSharedPreferences(SUMMARY, 0);
        final SharedPreferences.Editor editor = summary.edit();


        final String[] bankShortName = getResources().getStringArray(R.array.banks_short_name);

        myRef.child("main").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                editor.putString("usd_max_buying", roundingDouble(dataSnapshot.child(USD_BUY).child(VALUE).getValue().toString()));
                editor.putString("usd_max_buying_bank", getResources().getStringArray(R.array.banks_long_name)[Arrays.asList(bankShortName).indexOf(dataSnapshot.child(USD_BUY).child(BANK).getValue().toString())]);
                editor.putString("usd_min_selling", roundingDouble(dataSnapshot.child(USD_SELL).child(VALUE).getValue().toString()));
                editor.putString("usd_min_selling_bank", getResources().getStringArray(R.array.banks_long_name)[Arrays.asList(bankShortName).indexOf(dataSnapshot.child(USD_SELL).child(BANK).getValue().toString())]);
                editor.putString("usd_buying_avg", roundingDouble(dataSnapshot.child(USD_BUY_AVG).getValue().toString()));
                editor.putString("usd_selling_avg", roundingDouble(dataSnapshot.child(USD_SELL_AVG).getValue().toString()));
                editor.putString("lastUpdateMain", getLastUpdate(Long.parseLong(dataSnapshot.child(MODIFIED_AT).getValue().toString()), context));
                editor.apply();

                ((TextView) findViewById(R.id.usd_max_buying)).setText(" " + summary.getString("usd_max_buying", EMPTY) + " ");
                ((TextView) findViewById(R.id.usd_max_buying_bank)).setText(" " + summary.getString("usd_max_buying", EMPTY) + " ");
                ((TextView) findViewById(R.id.usd_min_selling)).setText(" " + summary.getString("usd_max_buying", EMPTY) + " ");
                ((TextView) findViewById(R.id.usd_min_selling_bank)).setText(" " + summary.getString("usd_max_buying", EMPTY) + " ");
                ((TextView) findViewById(R.id.usd_buying_avg)).setText(" " + summary.getString("usd_max_buying", EMPTY) + " ");
                ((TextView) findViewById(R.id.usd_selling_avg)).setText(" " + summary.getString("usd_max_buying", EMPTY) + " ");
                ((TextView) findViewById(lastUpdateMain)).setText(" " + summary.getString("usd_max_buying", EMPTY) + " ");
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarMain);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.child("gold").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                editor.putString("g18_value", dataSnapshot.child(G18).getValue().toString());
                editor.apply();
                ((TextView) findViewById(R.id.g18_value)).setText(summary.getString("g18_value", EMPTY));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        super.onStart();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        if (item.getItemId() == android.R.id.home && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(new Intent(this, BankGridActivity.class), bundle);
            } else {
                startActivity(new Intent(this, BankGridActivity.class));

            }
        } else if (id == R.id.nav_summery) {
        } else if (id == R.id.nav_gold) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(new Intent(this, GoldActivity.class), bundle);
            } else {
                startActivity(new Intent(this, GoldActivity.class));

            }
        } else if (id == R.id.nav_currency) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(new Intent(this, CurrencyActivity.class), bundle);
            } else {
                startActivity(new Intent(this, CurrencyActivity.class));

            }
        } else if (id == R.id.nav_share) {
            String s = getString(R.string.highest_buying_price) +
                    ((TextView) findViewById(R.id.usd_max_buying)).getText() + "\n" +
                    getString(R.string.lowest_selling_price) +
                    ((TextView) findViewById(R.id.usd_min_selling)).getText() + "\n" +
                    getString(R.string.avg_selling) +
                    ((TextView) findViewById(R.id.usd_buying_avg)).getText() + "\n" +
                    getString(R.string.website);
            sharingIntent(s, MainActivity.this);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
