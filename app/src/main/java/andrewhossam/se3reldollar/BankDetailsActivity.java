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
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static andrewhossam.se3reldollar.Constants.FIRST_COLUMN;
import static andrewhossam.se3reldollar.Constants.MODIFIED_AT;
import static andrewhossam.se3reldollar.Constants.SECOND_COLUMN;
import static andrewhossam.se3reldollar.Constants.SOURCE;
import static andrewhossam.se3reldollar.Constants.THIRD_COLUMN;
import static andrewhossam.se3reldollar.Constants.bankDrawables;
import static andrewhossam.se3reldollar.Constants.bankShortName;
import static andrewhossam.se3reldollar.Constants.getLastUpdate;
import static andrewhossam.se3reldollar.Constants.isConnectedToInternet;
import static andrewhossam.se3reldollar.Constants.roundingDouble;
import static andrewhossam.se3reldollar.Constants.sharingIntent;

public class BankDetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("banks");
    ListView listView;
    RowListViewAdapter adapter;
    Context context;
    String[] currenciesSymbol;
    Bundle bundle = null;
    private ArrayList<HashMap<String, String>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);
        context = this;
        currenciesSymbol = getResources().getStringArray(R.array.currencies_symbol);

        MobileAds.initialize(this, getString(R.string.app_ad_id));
        ((AdView) findViewById(R.id.adViewTop)).loadAd(new AdRequest.Builder().build());
        ((AdView) findViewById(R.id.adViewBottom)).loadAd(new AdRequest.Builder().build());

        if (!isConnectedToInternet(BankDetailsActivity.this)) {
            Toast.makeText(BankDetailsActivity.this, R.string.InternetError, Toast.LENGTH_LONG).show();
        }
        final int bankid = getIntent().getIntExtra("imageRid", 0);
        getSupportActionBar().setTitle(getResources().getStringArray(R.array.banks_long_name)[bankid]);

        ImageView app_bar_image = (ImageView) findViewById(R.id.app_bar_image);
        app_bar_image.setImageResource(bankDrawables[bankid]);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) app_bar_image.getLayoutParams();
        params.topMargin = (int) ((280 - (app_bar_image.getDrawable().getIntrinsicHeight() / getResources().getDisplayMetrics().density)) / 2);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        app_bar_image.setLayoutParams(params);

        listView = (ListView) findViewById(R.id.mobile_list);
        adapter = new RowListViewAdapter(this, list);
        adapter.add(addToListView(getString(R.string.the_currancy), getString(R.string.buy), getString(R.string.sell)));
        listView.setAdapter(adapter);

        myRef.child(bankShortName[bankid]).child("currency").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                adapter.add(addToListView(dataSnapshot.getKey(), roundingDouble(dataSnapshot.child(SECOND_COLUMN).getValue().toString()), roundingDouble(dataSnapshot.child(THIRD_COLUMN).getValue().toString())));
                if (dataSnapshot.getKey().equals("USD")) {
                    ((TextView) findViewById(R.id.usd_buying_value)).setText(roundingDouble(dataSnapshot.child(SECOND_COLUMN).getValue().toString()));
                    ((TextView) findViewById(R.id.usd_selling_value)).setText(roundingDouble(dataSnapshot.child(THIRD_COLUMN).getValue().toString()));
                }
                setListViewHeightBasedOnChildren(listView);
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarBank);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.child(bankShortName[bankid]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((TextView) findViewById(R.id.lastUpdateBank)).setText(" " + getLastUpdate(Long.parseLong(dataSnapshot.child(MODIFIED_AT).getValue().toString()), context) + " ");
                ((TextView) findViewById(R.id.bank_source_link)).setText(" " + dataSnapshot.child(SOURCE).getValue() + " ");
                if (!dataSnapshot.child(SOURCE).getValue().equals(getString(R.string.other_sources))) {
                    findViewById(R.id.verified).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton sharingFab = (FloatingActionButton) findViewById(R.id.share_action);
        sharingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharingIntent(getString(R.string.dollar_price_in) + getResources().getStringArray(R.array.banks_long_name)[bankid] + getString(R.string.for_selling) + ((TextView) findViewById(R.id.usd_selling_value)).getText() + getString(R.string.for_buy) + ((TextView) findViewById(R.id.usd_buying_value)).getText(), BankDetailsActivity.this);
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_bank_details);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    HashMap<String, String> addToListView(String currency, String sell, String buy) {
        HashMap<String, String> temp = new HashMap<>();
        if ((Arrays.asList(currenciesSymbol).contains(currency))) {
            currency = getResources().getStringArray(R.array.currencies_name)[Arrays.asList(currenciesSymbol).indexOf(currency)];

        }
        temp.put(FIRST_COLUMN, currency);
        temp.put(SECOND_COLUMN, sell);
        temp.put(THIRD_COLUMN, buy);
        return temp;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_bank_details);
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
            sharingIntent("https://fj8n4.app.goo.gl/ukph", BankDetailsActivity.this);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_bank_details);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
