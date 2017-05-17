package andrewhossam.se3reldollar;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static andrewhossam.se3reldollar.Constants.FIRST_COLUMN;
import static andrewhossam.se3reldollar.Constants.FOURTH_COLUMN;
import static andrewhossam.se3reldollar.Constants.SECOND_COLUMN;
import static andrewhossam.se3reldollar.Constants.THIRD_COLUMN;
import static andrewhossam.se3reldollar.Constants.getLastUpdate;

/**
 * Created by Andrew on 11/15/2016.
 */

class RowListViewAdapter extends ArrayAdapter<HashMap<String, String>> {

    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    TextView txtFourth;
    private ArrayList<HashMap<String, String>> list;

    public RowListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
        super(activity, R.layout.colmn_3row, list);
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            if (list.get(0).size() == 3)
                convertView = inflater.inflate(R.layout.colmn_3row, null);
            else
                convertView = inflater.inflate(R.layout.colmn_4row, null);

        }

        txtFirst = (TextView) convertView.findViewById(R.id.currency);
        txtSecond = (TextView) convertView.findViewById(R.id.buy);
        txtThird = (TextView) convertView.findViewById(R.id.sell);

        HashMap<String, String> map = list.get(position);
        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));
        txtThird.setText(map.get(THIRD_COLUMN));


        if (list.get(0).size() == 4) {
            txtFourth = (TextView) convertView.findViewById(R.id.modified_at);
            txtFourth.setText(getLastUpdate(Long.parseLong(map.get(FOURTH_COLUMN)), activity.getApplicationContext()));
        }
        return convertView;
    }

}
