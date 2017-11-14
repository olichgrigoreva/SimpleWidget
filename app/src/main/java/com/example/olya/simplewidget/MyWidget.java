package com.example.olya.simplewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * Created by Olya on 02.11.2017.
 */


public class MyWidget extends AppWidgetProvider {

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    final String LOG_TAG = "myLogs";
    TextView tv;
    long date = System.currentTimeMillis();//получаем системное время

    @Override
    public void onEnabled(Context context) { //вызывается при создании первого экземпляра виджета (их мб несколько)
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");
    }

    //вызывается при обновлении виджета
    @Override
    //На вход метод получает контекст, объект AppWidgetManager и список ID экземпляров виджетов, которые обновляются
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));
        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);
        }
    }

    //применение изменений к виджету
    void updateWidget(Context context, AppWidgetManager appWidgetManager,int appWidgetId) {
        RemoteViews rv = new RemoteViews(context.getPackageName(),R.layout.widget);
        setUpdateTV(rv, context, appWidgetId);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    //Ставим время в качестве текста и обновление виджета по нажатию
    void setUpdateTV(RemoteViews rv, Context context, int appWidgetId) {

        Intent updIntent = new Intent(context, MyWidget.class);

        updIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { appWidgetId });

        //описание намерения и целевого действия для выполнения с ним
        PendingIntent updPIntent = PendingIntent.getBroadcast(context,appWidgetId, updIntent, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy\nHH:mm");//паттерн для системного времени
        String dateString = sdf.format(date);//форматирование по паттерну системного времени

        rv.setOnClickPendingIntent(R.id.tv, updPIntent);//обновление виджета по нажатию

            //определение недели
            int week_num=Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
            int temp=week_num%2;
            if (temp==0){
                rv.setTextViewText(R.id.tv, dateString+"\n"+"Неделя: "+week_num+"\n"+"Красная");
            }
            else{
                rv.setTextViewText(R.id.tv, dateString+"\n"+"Неделя: "+week_num+"\n"+"Синяя");
            }
    }

    //вызывается при удалении каждого экземпляра виджета
    @Override
    //На входметод получает контекст, объект AppWidgetManager и список ID экземпляров виджетов, которые удаляются
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
    }

    //вызывается при удалении последнего экземпляра виджета
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }

}
