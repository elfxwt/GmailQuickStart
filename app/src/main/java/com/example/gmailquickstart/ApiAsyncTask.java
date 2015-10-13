
package com.example.gmailquickstart;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.services.gmail.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An asynchronous task that handles the Gmail API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */
public class ApiAsyncTask extends AsyncTask<Void, Void, Void> {
    private MainActivity mActivity;

    /**
     * Constructor.
     * @param activity MainActivity that spawned this task.
     */
    ApiAsyncTask(MainActivity activity) {
        this.mActivity = activity;
    }

    /**
     * Background task to call Gmail API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
            mActivity.clearResultsText();
            mActivity.updateResultsText(getDataFromApi());

        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
            mActivity.showGooglePlayServicesAvailabilityErrorDialog(
                    availabilityException.getConnectionStatusCode());

        } catch (UserRecoverableAuthIOException userRecoverableException) {
            mActivity.startActivityForResult(
                    userRecoverableException.getIntent(),
                    MainActivity.REQUEST_AUTHORIZATION);

        } catch (Exception e) {
            mActivity.updateStatus("The following error occurred:\n" +
                    e.getMessage());
        }
        if (mActivity.mProgress.isShowing()) {
            mActivity.mProgress.dismiss();
        }
        return null;
    }

    /**
     * Fetch a list of Gmail labels attached to the specified account.
     * @return List of Strings labels.
     * @throws IOException
     */
    private List<String> getDataFromApi() throws IOException {
        // Get the labels in the user's account.
        String user = "me";
        int unreadMesNum = 0;

        List<String> labels = new ArrayList<String>();
        List<String> messages = new ArrayList<String>();
        List<String> selectedMesLable = new ArrayList<String>();
        selectedMesLable.add("INBOX");
        ListLabelsResponse listResponse =
                mActivity.mService.users().labels().list(user).execute(); // this way just execute and get the label list


        Label label = mActivity.mService.users().labels().get(user,"INBOX").execute(); // have the lableName to execute together

//        ListMessagesResponse listMesResponse = mActivity.mService.users().messages().list(user).setLabelIds(selectedMesLable).execute();

        labels.add("所有邮件："+label.getThreadsTotal());
        labels.add("未读邮件："+label.getThreadsUnread());
//        for (Label label : listResponse.getLabels()) {
//            String name = label.getName();
//            if(name != null && (name.equals("SPAM") ||name.equals("UNREAD"))){
//                labels.add(label.getName());
//                Log.d("sophia", "label name is " + label.getName());
//                Log.d("sophia"," get Message Unread is " + label.getMessagesUnread());
//                Log.d("sophia"," get message total is " + label.getMessagesTotal());
//            }
//
//        }

//        Log.d("sophia","inbox message's num is " + listMesResponse.getMessages().size());
//        if(listMesResponse.getMessages() != null)
//        {
//            for(Message msg:listMesResponse.getMessages()){
//                if(msg.getLabelIds()!=null && msg.getLabelIds().size() > 0){
//                    for(String label:msg.getLabelIds()){
//                        Log.d("sophia","label is " + label);
//                        if(label.equals("INBOX"))
//                            unreadMesNum++;
//                    }
//                }
//            }
//        }



//        Log.d("sophia","unread message num is: " + unreadMesNum);

        return labels;
    }




}