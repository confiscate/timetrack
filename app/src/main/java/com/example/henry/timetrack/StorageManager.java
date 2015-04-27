package com.example.henry.timetrack;

import android.content.Context;
import android.content.SharedPreferences;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.regions.Regions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by silvio on 4/25/15.
 */
public class StorageManager {
    private static String TASK_STORE = "com.example.henry.TASK_STORE";

    private static String NOTIFICATION_STORE = "com.example.henry.NOTIFICATION_STORE";
    private static String NOTIFICATION_STORE_ON_OFF = "com.example.henry.NOTIFICATION_STORE_KEY";
    private static String NOTIFICATION_STORE_HOURS = "com.example.henry.NOTIFICATION_STORE_HOURS";

//    public CognitoSyncManager syncClient;
//    private Dataset notificationDataset;
//    private Dataset hourTaskDataset;
//
//    public CognitoCachingCredentialsProvider credentialsProvider;

    // stuff that is persisted in AWS
    private HoursListItem[] hoursListItems = new HoursListItem[24];


    public StorageManager() {
    }

    public HoursListItem[] getHoursListItems() {
        return hoursListItems;
    }

    public HoursListItem getHoursListItem(int index) {
        return hoursListItems[index];
    }

    public void setHoursListItem(int index, HoursListItem item) {
        hoursListItems[index] = item;
    }

    public void setNotificationsActive(boolean notificationsActive, Context context) {
        SharedPreferences.Editor notificationStoreEditor =
                context.getSharedPreferences(NOTIFICATION_STORE, Context.MODE_PRIVATE)
                       .edit();
        notificationStoreEditor.putBoolean(NOTIFICATION_STORE_ON_OFF, notificationsActive);
        notificationStoreEditor.commit();
    }

    public boolean getNotificationsActive(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                NOTIFICATION_STORE, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(NOTIFICATION_STORE_ON_OFF, false);
    }

    public void setNotificationPeriod(String minutes, Context context) {
        SharedPreferences.Editor notificationStoreEditor =
                context.getSharedPreferences(NOTIFICATION_STORE, Context.MODE_PRIVATE)
                        .edit();
        notificationStoreEditor.putString(NOTIFICATION_STORE_HOURS, minutes);
        notificationStoreEditor.commit();
    }

    public String getNotificationsPeriod(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                NOTIFICATION_STORE, Context.MODE_PRIVATE);
        return sharedPref.getString(NOTIFICATION_STORE_HOURS, "1");
    }

    public void pushTasksToStorage(Context context) {
        SharedPreferences.Editor storeEditor =
                context.getSharedPreferences(TASK_STORE, Context.MODE_PRIVATE)
                        .edit();
        for (int i = 0; i < hoursListItems.length; i++) {
            if (hoursListItems[i].getTaskDesc().trim().equals("")) {
                storeEditor.remove(hoursListItems[i].getKey());
                continue;
            }
            storeEditor.putString(hoursListItems[i].getKey(),
                    hoursListItems[i].getTaskDesc());
        }
        storeEditor.commit();
    }

    public void pullTasksFromStorage(MainActivity activity) {
        activity.fillHoursLog();
        SharedPreferences sharedPref = activity.getSharedPreferences(
                TASK_STORE, Context.MODE_PRIVATE);
        for (int i = 0; i < hoursListItems.length; i++) {
            String taskDesc = sharedPref.getString(hoursListItems[i].getKey(), "");
            if (taskDesc.trim().equals("")) {
                continue;
            }
            hoursListItems[i].setTaskDesc(taskDesc);
        }
    }

    public HoursListItem[] getAllListItems(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                TASK_STORE, Context.MODE_PRIVATE);
        ArrayList<HoursListItem> allPastItems = new ArrayList();
        Map<String, ?> storedTasks = sharedPref.getAll();
        for (Map.Entry<String, ?> entry : storedTasks.entrySet()) {
            String taskDesc = entry.getValue().toString();
            if (taskDesc.trim().equals("")) {
                continue;
            }
            int separatorIndex = entry.getKey().lastIndexOf(' ');
            if (separatorIndex <= 0) {
                continue;
            }
            String hour = entry.getKey().substring(0, separatorIndex);
            String date = entry.getKey().substring(separatorIndex + 1);
            allPastItems.add(new HoursListItem(hour, date, taskDesc));
        }
        Collections.sort(allPastItems);
        return allPastItems.toArray(new HoursListItem[allPastItems.size()]);
    }

//    private void authenticateAWS() {
//        credentialsProvider = new CognitoCachingCredentialsProvider(
//                this, // Context
//                "", // Identity Pool ID
//                Regions.US_EAST_1 // Region
//        );
//
//        // Initialize the Cognito Sync client
//        syncClient = new CognitoSyncManager(
//                this,
//                Regions.US_EAST_1, // Region
//                credentialsProvider);
//
//        // Create a record in a dataset and synchronize with the server
//        hourTaskDataset = syncClient.openOrCreateDataset("hourTaskDataset");
//        synchronizeDataset(hourTaskDataset);
//        notificationDataset = syncClient.openOrCreateDataset("notificationDataset");
//        synchronizeDataset(notificationDataset);
//    }

//    private void synchronizeDataset(Dataset dataset) {
//        dataset.synchronize(new DefaultSyncCallback() {
//            @Override
//            public void onSuccess(Dataset dataset, List newRecords) {
//            }
//        });
//    }


//    notificationDataset.put("on", on.toString());
//    synchronizeDataset(notificationDataset);
//    if (on) {
//
//    }

//    if (hourTaskDataset != null) {
//        if (newTaskDesc.equals("")) {
//            hourTaskDataset.remove(key);
//        } else {
//            hourTaskDataset.put(key, newTaskDesc);
//        }
//        hourTaskDataset.synchronize(new DefaultSyncCallback() {
//            @Override
//            public void onSuccess(Dataset dataset, List newRecords) {
//                refreshList();
//            }
//        });
//    }
}
