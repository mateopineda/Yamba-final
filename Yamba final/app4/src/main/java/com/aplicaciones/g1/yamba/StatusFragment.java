package com.aplicaciones.g1.yamba;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

import static android.view.View.OnClickListener;


public class StatusFragment extends Fragment implements OnClickListener {

    private static final String TAG = "Fragment 1";

    private EditText editStatus;
    private Button buttonTweet;
    private TextView textCount;
    private int defaultTextColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        editStatus = (EditText) view.findViewById(R.id.editStatus);
        buttonTweet = (Button) view.findViewById(R.id.buttonTweet);
        textCount = (TextView) view.findViewById(R.id.textCount);
        buttonTweet.setOnClickListener(this);
        Log.d(TAG, "onCreateView");
        defaultTextColor = textCount.getTextColors().getDefaultColor();
        editStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = 140 - editStatus.length();
                textCount.setText(Integer.toString(count));
                textCount.setTextColor(Color.BLUE);
                if (count <10){
                    textCount.setTextColor(Color.RED);
                } else{
                    textCount.setTextColor(defaultTextColor);
                }

            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        String estado = editStatus.getText().toString();
        Log.d(TAG, "Se dio click " + estado);
        editStatus.setText("");
        editStatus.setHint("Que te parece?");
        new PostTask().execute(estado);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }
    private final class PostTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String username = prefs.getString("username", "");
            String password = prefs.getString("password", "");

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                getActivity().startActivity(new Intent(getActivity(), SettingsActivity.class));
                return "Please update your username and password";
            }

            YambaClient yambacloud = new YambaClient(username,password);

            try{
                yambacloud.postStatus(params[0]);
            } catch (YambaClientException e) {
                e.printStackTrace();
                return "Error al publicar en el cliente de yamba";
            }
            return "se ha publicado bien";
        }

        @Override
        protected void onPostExecute(String result){
            super .onPostExecute(result);

            Toast.makeText(StatusFragment.this.getActivity(), result, Toast.LENGTH_LONG).show();

        }
    }



}