package com.example.uzair.connectiontest;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.PDKUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Configure PDKClient here

    PDKClient pdkClient;
    // Add your app id here

    final String appId = "5009371956196168337";
    LinearLayout linearLayout;
    ImageView profileImg;
    TextView fname, lname, bio;

    private final String Fields = "id, counts, created_at, first_name, last_name, bio";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Now Create connection

        pdkClient = PDKClient.configureInstance(this, appId);
        pdkClient.onConnect(this);

        //Finished connection with manifest file
    }

    //Login Button

    public void Click(View view)
    {
        List scopes = new ArrayList<String>();
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);

        pdkClient.login(this, scopes, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                Log.d(getClass().getName(), response.getData().toString());
                finalResult();
                //user logged in, use response.getUser() to get PDKUser object
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pdkClient.onOauthResponse(requestCode, resultCode, data);
    }

    //get id
    public void finalResult()
    {
        linearLayout = (LinearLayout) findViewById(R.id.lnrId);
        profileImg = (ImageView)findViewById(R.id.imgload);
        fname = (TextView)findViewById(R.id.nameId);
        lname = (TextView)findViewById(R.id.lastnmId);
        bio = (TextView)findViewById(R.id.bioId);

        linearLayout.setVisibility(View.VISIBLE);

        PDKClient.getInstance().getMe(Fields, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response){

                //set data here
                PDKUser user;
                user = response.getUser();
                fname.setText("First Name:  "+user.getFirstName());
                lname.setText("Last Name:   "+user.getLastName());
                bio.setText("Bio:   "+user.getBio());
                //Loading Image
                Glide.with(MainActivity.this).load(user.getImageUrl()).into(profileImg);

        }

        @Override
        public void onFailure(PDKException exception) {
    }
    });
    }

     //Build & run used glide for photo.

}
