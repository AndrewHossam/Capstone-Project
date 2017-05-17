package andrewhossam.se3reldollar;

import android.app.ActivityOptions;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import andrewhossam.se3reldollar.Objects.BankNameComparator;
import andrewhossam.se3reldollar.Objects.BuyComparator;
import andrewhossam.se3reldollar.Objects.CurrencyObject;
import andrewhossam.se3reldollar.Objects.SellComparator;

import static andrewhossam.se3reldollar.Constants.FIRST_COLUMN;
import static andrewhossam.se3reldollar.Constants.FOURTH_COLUMN;
import static andrewhossam.se3reldollar.Constants.SECOND_COLUMN;
import static andrewhossam.se3reldollar.Constants.THIRD_COLUMN;
import static andrewhossam.se3reldollar.Constants.isConnectedToInternet;
import static andrewhossam.se3reldollar.Constants.roundingDouble;
import static andrewhossam.se3reldollar.Constants.sharingIntent;

public class CurrencyActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("currency");
    ListView listView;
    RowListViewAdapter adapter;
    boolean sortedByName, sortedBySell, sortedByBuy;
    List<CurrencyObject> currencyObjectList;
    String[] currenciesSymbol;
    String[] bankShortName;
    Bundle bundle = null;
    private ArrayList<HashMap<String, String>> listUSDOnly = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        MobileAds.initialize(this, getString(R.string.app_ad_id));
        currenciesSymbol = getResources().getStringArray(R.array.currencies_symbol);
        bankShortName = getResources().getStringArray(R.array.banks_short_name);

        ((AdView) findViewById(R.id.adViewTop)).loadAd(new AdRequest.Builder().build());
        ((AdView) findViewById(R.id.adViewBottom)).loadAd(new AdRequest.Builder().build());
        final TextView bankColumn = (TextView) findViewById(R.id.bank_name_column);
        final TextView sellColumn = (TextView) findViewById(R.id.sell_column);
        final TextView buyColumn = (TextView) findViewById(R.id.buy_column);
        bankColumn.setEnabled(false);
        sellColumn.setEnabled(false);
        buyColumn.setEnabled(false);
        bankColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                sortedByBuy = false;
                sortedBySell = false;
                sellColumn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sorting_arrows_16, 0);
                buyColumn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sorting_arrows_16, 0);
                Collections.sort(currencyObjectList, new BankNameComparator());
                if (sortedByName) {
                    bankColumn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.reversed_alphabetical_sorting_16, 0);
                    Collections.reverse(currencyObjectList);
                    sortedByName = false;
                } else {
                    sortedByName = true;
                    bankColumn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.alphabetical_sorting_24, 0);
                }

                for (CurrencyObject currencyObject : currencyObjectList) {
                    adapter.add(addToListView(currencyObject.getName(), currencyObject.getSell(), currencyObject.getBuy(), currencyObject.getLastUpDate()));
                }
            }
        });
        sellColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                sortedByBuy = false;
                sortedByName = false;
                bankColumn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sorting_arrows_24, 0);
                buyColumn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sorting_arrows_16, 0);
                Collections.sort(currencyObjectList, new SellComparator());
                if (sortedBySell) {
                    sortedBySell = false;
                    Collections.reverse(currencyObjectList);
                    sellColumn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.reversed_numerical_sorting_16, 0);

                } else {
                    sortedBySell = true;
                    sellColumn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.numerical_sorting_16, 0);
                }
                for (CurrencyObject currencyObject : currencyObjectList) {
                    adapter.add(addToListView(currencyObject.getName(), currencyObject.getSell(), currencyObject.getBuy(), currencyObject.getLastUpDate()));
                }

            }
        });
        buyColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                sortedByName = false;
                sortedBySell = false;
                bankColumn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sorting_arrows_24, 0);
                sellColumn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sorting_arrows_16, 0);
                Collections.sort(currencyObjectList, new BuyComparator());
                if (sortedByBuy) {
                    sortedByBuy = false;
                    Collections.reverse(currencyObjectList);
                    buyColumn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.reversed_numerical_sorting_16, 0);

                } else {
                    sortedByBuy = true;
                    buyColumn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.numerical_sorting_16, 0);
                }
                for (CurrencyObject currencyObject : currencyObjectList) {
                    adapter.add(addToListView(currencyObject.getName(), currencyObject.getSell(), currencyObject.getBuy(), currencyObject.getLastUpDate()));
                }
            }
        });
        if (!isConnectedToInternet(CurrencyActivity.this)) {
            Toast.makeText(CurrencyActivity.this, R.string.InternetError, Toast.LENGTH_LONG).show();
        }
        getSupportActionBar().setTitle(R.string.currencies);

        final Spinner selectCurrencySpinner = (Spinner) findViewById(R.id.select_currency);
        final ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);

        FloatingActionButton sharingFab = (FloatingActionButton) findViewById(R.id.share_action);
        sharingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharingIntent("https://fj8n4.app.goo.gl/rniX", CurrencyActivity.this);

            }
        });
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                categoriesAdapter.add(dataSnapshot.getKey() /*+ "-" + getResources().getStringArray(R.array.currencies_name)[Arrays.asList(currenciesSymbol).indexOf(dataSnapshot.getKey())]*/);
                if (dataSnapshot.getKey().equals("USD"))
                    selectCurrencySpinner.setSelection(categoriesAdapter.getCount() - 1);

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
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectCurrencySpinner.setAdapter(categoriesAdapter);
        listView = (ListView) findViewById(R.id.currency_only_listview);
        adapter = new RowListViewAdapter(this, listUSDOnly);
        listView.setAdapter(adapter);
        selectCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCurrency = adapterView.getItemAtPosition(i).toString().substring(0, 3);
                currencyObjectList = new ArrayList<>();
                adapter.clear();
                bankColumn.setEnabled(true);
                sellColumn.setEnabled(true);
                buyColumn.setEnabled(true);
                myRef.child(selectedCurrency).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        adapter.add(addToListView(getResources().getStringArray(R.array.banks_long_name)[Arrays.asList(bankShortName).indexOf(dataSnapshot.getKey())], roundingDouble(dataSnapshot.child(SECOND_COLUMN).getValue().toString()), roundingDouble(dataSnapshot.child(THIRD_COLUMN).getValue().toString()), dataSnapshot.child(FOURTH_COLUMN).getValue().toString()));
                        currencyObjectList.add(new CurrencyObject(getResources().getStringArray(R.array.banks_long_name)[Arrays.asList(bankShortName).indexOf(dataSnapshot.getKey())], Double.parseDouble(roundingDouble(dataSnapshot.child(SECOND_COLUMN).getValue().toString())), Double.parseDouble(roundingDouble(dataSnapshot.child(THIRD_COLUMN).getValue().toString())), dataSnapshot.child(FOURTH_COLUMN).getValue().toString()));
                        setListViewHeightBasedOnChildren(listView);
                        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarCurrency);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(CurrencyActivity.this, R.string.nothing, Toast.LENGTH_SHORT).show();
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_currency);
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

    HashMap<String, String> addToListView(String bank, String sell, String buy, String modifiedAt) {
        HashMap<String, String> temp = new HashMap<>();
        temp.put(FIRST_COLUMN, bank);
        temp.put(SECOND_COLUMN, sell);
        temp.put(THIRD_COLUMN, buy);
        temp.put(FOURTH_COLUMN, modifiedAt);
        return temp;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_currency);
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
        } else if (id == R.id.nav_share) {
            sharingIntent("https://fj8n4.app.goo.gl/ukph", CurrencyActivity.this);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_currency);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
