package andrewhossam.se3reldollar;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static andrewhossam.se3reldollar.Constants.G18;
import static andrewhossam.se3reldollar.Constants.G21;
import static andrewhossam.se3reldollar.Constants.G24;
import static andrewhossam.se3reldollar.Constants.GOLDEN_LE;
import static andrewhossam.se3reldollar.Constants.MODIFIED_AT;
import static andrewhossam.se3reldollar.Constants.SOURCE;
import static andrewhossam.se3reldollar.Constants.getLastUpdate;
import static andrewhossam.se3reldollar.Constants.isConnectedToInternet;
import static andrewhossam.se3reldollar.Constants.sharingIntent;

public class GoldActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("gold");
    Context context;
    Bundle bundle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold);
        context = this;
        MobileAds.initialize(this, getString(R.string.app_ad_id));
        ((AdView) findViewById(R.id.adView)).loadAd(new AdRequest.Builder().build());
        getSupportActionBar().setTitle(R.string.the_gold);
        FloatingActionButton sharingFab = (FloatingActionButton) findViewById(R.id.share_action);
        sharingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharingIntent(getString(R.string.carat_18) + ((TextView) findViewById(R.id.g18_value)).getText() + getString(R.string.carat_21) + ((TextView) findViewById(R.id.g21_value)).getText() + getString(R.string.carat_24) + ((TextView) findViewById(R.id.g24_value)).getText() + getString(R.string.golden_pound) + ((TextView) findViewById(R.id.golden_le)).getText(), GoldActivity.this);

            }
        });

        if (!isConnectedToInternet(GoldActivity.this)) {
            Toast.makeText(GoldActivity.this, R.string.InternetError, Toast.LENGTH_LONG).show();
        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((TextView) findViewById(R.id.g18_value)).setText(dataSnapshot.child(G18).getValue().toString());
                ((TextView) findViewById(R.id.g21_value)).setText(dataSnapshot.child(G21).getValue().toString());
                ((TextView) findViewById(R.id.g24_value)).setText(dataSnapshot.child(G24).getValue().toString());
                ((TextView) findViewById(R.id.golden_le)).setText(dataSnapshot.child(GOLDEN_LE).getValue().toString());
                ((TextView) findViewById(R.id.lastUpdateGold)).setText(getLastUpdate(Long.parseLong(dataSnapshot.child(MODIFIED_AT).getValue().toString()), context));
                ((TextView) findViewById(R.id.gold_source_link)).setText(dataSnapshot.child(SOURCE).getValue().toString());
                findViewById(R.id.progressBarGold).setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_gold);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_gold);
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
                finish();
            } else {
                startActivity(new Intent(this, BankGridActivity.class));
                finish();
            }
        } else if (id == R.id.nav_summery) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(new Intent(this, MainActivity.class), bundle);
                finish();
            } else {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }

        } else if (id == R.id.nav_gold) {
        } else if (id == R.id.nav_currency) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(new Intent(this, CurrencyActivity.class), bundle);
                finish();
            } else {
                startActivity(new Intent(this, CurrencyActivity.class));
                finish();
            }
        } else if (id == R.id.nav_share) {
            sharingIntent("https://fj8n4.app.goo.gl/ukph", GoldActivity.this);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_gold);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
