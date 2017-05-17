package andrewhossam.se3reldollar;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import static andrewhossam.se3reldollar.Constants.EMPTY;
import static andrewhossam.se3reldollar.MainActivity.SUMMARY;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        final SharedPreferences summary = context.getSharedPreferences(SUMMARY, 0);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setTextViewText(R.id.usd_max_buying, " " + summary.getString("usd_max_buying", EMPTY) + " ");


        views.setTextViewText(R.id.usd_max_buying, " " + summary.getString("usd_max_buying", EMPTY) + " ");
        views.setTextViewText(R.id.usd_max_buying_bank, " " + summary.getString("usd_max_buying", EMPTY) + " ");
        views.setTextViewText(R.id.usd_min_selling, " " + summary.getString("usd_max_buying", EMPTY) + " ");
        views.setTextViewText(R.id.usd_min_selling_bank, " " + summary.getString("usd_max_buying", EMPTY) + " ");
        views.setTextViewText(R.id.usd_buying_avg, " " + summary.getString("usd_max_buying", EMPTY) + " ");
        views.setTextViewText(R.id.usd_selling_avg, " " + summary.getString("usd_max_buying", EMPTY) + " ");
        views.setTextViewText(R.id.lastUpdateMain, " " + summary.getString("usd_max_buying", EMPTY) + " ");
        views.setTextViewText(R.id.g18_value, " " + summary.getString("g18_value", EMPTY) + " ");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

