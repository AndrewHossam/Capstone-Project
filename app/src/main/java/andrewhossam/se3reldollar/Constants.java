package andrewhossam.se3reldollar;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * Created by Andrew on 11/15/2016.
 */

public class Constants {
    static final String FIRST_COLUMN = "currency";
    static final String SECOND_COLUMN = "buy";
    static final String THIRD_COLUMN = "sell";
    static final String FOURTH_COLUMN = "modifiedAt";
    static final String MODIFIED_AT = "modifiedAt";
    static final String USD_BUY = "usd_buy";
    static final String USD_SELL = "usd_sell";
    static final String USD_BUY_AVG = "usd_buy_avg";
    static final String USD_SELL_AVG = "usd_sell_avg";
    static final String BANK = "bank";
    static final String VALUE = "value";
    static final String GOLDEN_LE = "gold_LE";
    static final String G18 = "gram_18";
    static final String G21 = "gram_21";
    static final String G24 = "gram_24";
    static final String SOURCE = "source";
    static final String EMPTY = "No data";

    static final int bankDrawables[] = {
            R.drawable.aab,
            R.drawable.abk,
            R.drawable.adib,
            R.drawable.alb,
            R.drawable.alex,
            R.drawable.blom,
            R.drawable.bm,
            R.drawable.cae,
            R.drawable.cairo,
            R.drawable.cbe,
            R.drawable.cib,
            R.drawable.ebe,
            R.drawable.egb,
            R.drawable.fsl,
            R.drawable.hdb,
            R.drawable.midb,
            R.drawable.nbe,
            R.drawable.nbg,
            R.drawable.pbdac,
            R.drawable.saib,
            R.drawable.scb,
            R.drawable.ubeg,
            R.drawable.hsbc,
            R.drawable.aibk,
            R.drawable.aub,
            R.drawable.mash,
            R.drawable.ndb,
            R.drawable.qnb,
            R.drawable.arab
    };
/*
    static final String bankArabicName[]
            = {"البنك العربي الأفريقي الدولي",
            "بنك بيريوس",
            "مصرف أبو ظبي الإسلامي",
            "بنك البركة",
            "بنك الإسكندرية",
            "بنك بلوم مصر",
            "بنك مصر",
            "كريدى اجريكول",
            "بنك القاهرة",
            "البنك المركزى المصرى",
            "CIB البنك التجارى الدولي",
            "البنك المصري لتنمية الصادرات",
            "البنك المصرى الخليجى",
            "بنك فيصل الاسلامي المصري",
            "بنك التعمير والاسكان",
            "بنك مصر إيران للتنمية",
            "البنك الأهلي المصري",
            "البنك الأهلى اليونانى بمصر",
            "بنك التنمية والإئتمان الزراعي",
            "SAIB",
            "بنك قناة السويس",
            "البنك المتحد",
            "HSBC",
            "بنك الاستثمار العربي",
            "البنك الاهلي المتحد",
            "بنك المشرق",
            "بنك الإمارات دبي الوطني",
            "QNB",
            "البنك العربي"
    };
*/


    static final String bankShortName[] = {
            "aab",
            "abk",
            "adib",
            "alb",
            "alex",
            "blom",
            "bm",
            "cae",
            "cairo",
            "cbe",
            "cib",
            "ebe",
            "egb",
            "fsl",
            "hdb",
            "midb",
            "nbe",
            "nbg",
            "pbdac",
            "saib",
            "scb",
            "ubeg",
            "hsbc",
            "aibk",
            "aub",
            "mash",
            "ndb",
            "qnb",
            "arab"
    };
/*

    static HashMap<String, String> currencyHashmap = new HashMap(19);

    static {
        currencyHashmap.put("EGP", "جنيه مصري");
        currencyHashmap.put("AED", "درهم إماراتي");
        currencyHashmap.put("AUD", "دولار أسترالي");
        currencyHashmap.put("BHD", "دينار بحريني");
        currencyHashmap.put("CAD", "دولار كندي");
        currencyHashmap.put("CHF", "فرنك سويسري");
        currencyHashmap.put("CNY", "يوان صيني");
        currencyHashmap.put("DKK", "كرونة دنماركية");
        currencyHashmap.put("EUR", "يورو");
        currencyHashmap.put("GBP", "جنيه إسترليني");
        currencyHashmap.put("JOD", "دينار أردني");
        currencyHashmap.put("JPY", "100 ين ياباني");
        currencyHashmap.put("KWD", "دينار كويتي");
        currencyHashmap.put("NOK", "كرونة نروجية");
        currencyHashmap.put("OMR", "ريال عماني");
        currencyHashmap.put("QAR", "ريال قطري");
        currencyHashmap.put("SAR", "ريال سعودي");
        currencyHashmap.put("SEK", "كرونة سويدية");
        currencyHashmap.put("THB", "بات تايلاندي");
        currencyHashmap.put("USD", "دولار أمريكي");
    }
*/

    static String roundingDouble(String s) {
        DecimalFormat df = new DecimalFormat("#.##");
        double v = Double.parseDouble(s);
        if (v < 1) {
            v *= 100.0;
            s = String.valueOf(v);
        }
        return df.format(Double.valueOf(s));
    }

/*
    static String getBankArabicName(String s) {
        int index = Arrays.asList(currenciesSymbol).indexOf(s);
        if (index == -1)
            return s;
        else
            return bankArabicName[index];
    }
*/


    static void sharingIntent(String text, Context context) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    static String getLastUpdate(long l, Context context) {

        long time = System.currentTimeMillis() - l;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        long hours = TimeUnit.MILLISECONDS.toHours(time);
        long days = TimeUnit.MILLISECONDS.toDays(time);

        if (days == 0 && hours == 0) {
            if (minutes == 0) {
                return "" + seconds + context.getString(R.string.seconds);
            } else if (minutes == 1) {
                return (context.getString(R.string.minite));
            } else if (minutes == 2) {
                return (context.getString(R.string.two_minits));
            } else if (minutes < 11) {
                return "" + minutes + context.getString(R.string.few_minutes);
            } else {
                return "" + minutes + context.getString(R.string.minutes);

            }
        } else if (days == 0) {
            if (hours == 1) {
                return (context.getString(R.string.one_hour));
            } else if (hours == 2) {
                return (context.getString(R.string.two_hours));
            } else if (hours < 11) {
                return (hours + context.getString(R.string.few_hours));
            } else {
                return (hours + context.getString(R.string.hours));
            }
        } else if (days == 1) {
            return (context.getString(R.string.one_day));
        } else if (days == 2) {
            return (context.getString(R.string.two_days));
        } else if (days < 11) {
            return (days + context.getString(R.string.few_days));
        } else {
            return (days + context.getString(R.string.days));

        }
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }
}
