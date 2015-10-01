package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.udacity.gradle.utils.AsyncJokeDownloader;
import com.udacity.gradle.utils.JokeDownloadListener;

import xyz.kushal.jokerlibrary.DisplayJokeActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements JokeDownloadListener {
    ProgressBar spinner;
    PublisherInterstitialAd mPublisherInterstitialAd;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        spinner = (ProgressBar) root.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        mPublisherInterstitialAd = new PublisherInterstitialAd(getActivity());
        mPublisherInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mPublisherInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                new AsyncJokeDownloader(MainActivityFragment.this).downloadJoke();
                spinner.setVisibility(View.VISIBLE);
            }
        });

        requestNewInterstitial();

        Button tellJokeButton = (Button) root.findViewById(R.id.tell_joke_button);
        tellJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPublisherInterstitialAd.isLoaded()) {
                    mPublisherInterstitialAd.show();
                } else {
                    new AsyncJokeDownloader(MainActivityFragment.this).downloadJoke();
                    spinner.setVisibility(View.VISIBLE);
                }
            }
        });

        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        return root;
    }

    private void requestNewInterstitial() {
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mPublisherInterstitialAd.loadAd(adRequest);
    }

    @Override
    public void downloadCompleted(String j) {
        spinner.setVisibility(View.GONE);
        Intent mIntent = new Intent(getActivity(), DisplayJokeActivity.class);
        mIntent.putExtra("joke", j);
        startActivity(mIntent);
    }
}
