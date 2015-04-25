package com.example.henry.timetrack;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.regions.Regions;

import java.util.List;

/**
 * Created by silvio on 4/25/15.
 */
public class StorageManager {

    public CognitoSyncManager syncClient;
    private Dataset notificationDataset;
    private Dataset hourTaskDataset;

    public CognitoCachingCredentialsProvider credentialsProvider;

    // stuff that is persisted in AWS
    private boolean notificationsActive = false;
    private HoursListItem[] hoursListItems = new HoursListItem[24];


    public StorageManager() {
    }

    public HoursListItem[] getHoursListItems() {
        return this.hoursListItems;
    }

    public HoursListItem getHoursListItem(int index) {
        return this.hoursListItems[index];
    }

    public void setHoursListItem(int index, HoursListItem item) {
        this.hoursListItems[index] = item;
    }

    public void setNotificationsActive(boolean notificationsActive) {
        this.notificationsActive = notificationsActive;
    }

    public void pushAWS() {

    }

    public void pullAWS(MainActivity activity) {
        activity.fillHoursLog();
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

    private void synchronizeDataset(Dataset dataset) {
        dataset.synchronize(new DefaultSyncCallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
            }
        });
    }


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
