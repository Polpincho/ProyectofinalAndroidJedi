package com.example.polpincho.proyectofinal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;


public class BadLogFragment extends Fragment implements View.OnClickListener{
    private OnFragmentInteractionListener mListener;


    @Bind(R.id.imageView)
    ImageView imageView;
    Button nextButton;
    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_bad_log, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        nextButton = (Button) rootView.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        ButterKnife.bind(rootView);
        downloadImage(imageView);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private Handler mhHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 100 && msg.obj != null) {
                imageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };


    public void downloadImage(View v) {
        new Thread(new Runnable() {
            private Bitmap loadImageFromNetwork(String url) {
                try {
                    Bitmap bitmap = BitmapFactory
                            .decodeStream((InputStream) new URL(url)
                                    .getContent());
                    return bitmap;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            public void run() {
                final Bitmap bitmap = loadImageFromNetwork("http://www.bolooka.com/img/error_page_logo.png");
                Message msg = new Message();
                msg.what = 100;
                msg.obj = bitmap;
                mhHandler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                if (mListener != null) {
                    mListener.onFragmentInteraction("", 3);
                } else {
                }
                break;
        }
    }
}