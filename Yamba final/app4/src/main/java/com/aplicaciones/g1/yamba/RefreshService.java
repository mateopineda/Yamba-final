package com.aplicaciones.g1.yamba;

import java.util.List;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClient.Status;
import com.marakana.android.yamba.clientlib.YambaClientException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RefreshService extends IntentService {
    private static final String TAG = RefreshService.class.getSimpleName();

    public RefreshService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreated");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        Log.d(TAG, "Yamba");
        final String username = prefs.getString("username", "");
        final String password = prefs.getString("password", "");
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Log.d(TAG, "Please update your username and password");
            return;
        }
        Log.d(TAG, "onStarted");

        ContentValues values = new ContentValues();

        YambaClient cloud = new YambaClient(username, password);
        try {
            int count = 0;
            List<Status> timeline = cloud.getTimeline(20);
            for (Status status : timeline) {
                values.clear();
                values.put(StatusContract.Column.ID, status.getId());
                values.put(StatusContract.Column.USER, status.getUser());
                values.put(StatusContract.Column.MESSAGE, status.getMessage());
                values.put(StatusContract.Column.CREATED_AT, status
                        .getCreatedAt().getTime());

                Uri uri = getContentResolver().insert(
                        StatusContract.CONTENT_URI, values);
                if (uri != null) {
                    count++;
                    Log.d(TAG,
                            String.format("%s: %s", status.getUser(),
                                    status.getMessage()));
                }
            }

            if (count > 0) {
                sendBroadcast(new Intent(
                        "com.marakana.android.yamba.action.NEW_STATUSES").putExtra(
                        "count", count));
            }

        } catch (YambaClientException e) {
            Log.e(TAG, "Failed to fetch the timeline", e);
            e.printStackTrace();
        }

        return;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroyed");
    }

}