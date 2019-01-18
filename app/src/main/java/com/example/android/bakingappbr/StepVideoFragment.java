package com.example.android.bakingappbr;


import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.android.bakingappbr.databinding.FragmentStepVideoBinding;
import com.example.android.bakingappbr.model.RecipeStep;

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;


public class StepVideoFragment extends Fragment implements ExoPlayer.EventListener {
    private static final String STEP_ALL_SAVE = "step_all_save ";
    private final String TAG = StepVideoFragment.class.getSimpleName();
    private FragmentStepVideoBinding binding;

    private long playPosition;
    private boolean playReady=true ;//true:play auto; null: on pause
    private int currentWindowIndex;
    private static final String PLAYER_POSITION = "play_position";
    private static final String PLAYBACK_READY = "play_ready";


    private RecipeStep mRecipeStep;
    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;


    public static StepVideoFragment SVFInstance(RecipeStep step) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STEP_ALL_SAVE, step);
        StepVideoFragment fragment = new StepVideoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (savedInstanceState != null) {
            playPosition = savedInstanceState.getLong(PLAYER_POSITION);
            playReady = savedInstanceState.getBoolean(PLAYBACK_READY);

        }

        if ((arguments != null) && (arguments.containsKey(STEP_ALL_SAVE))) {
            mRecipeStep = arguments.getParcelable(STEP_ALL_SAVE);

        }
    }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_video, container, false);
            final View view = binding.getRoot();


            if (mRecipeStep.getVideoURL() != null && !mRecipeStep.getVideoURL().matches("")) {
                initializeMediaSession();
                initializePlayer(Uri.parse(mRecipeStep.getVideoURL()));
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                        !getResources().getBoolean(R.bool.isTablet)) {
                    hideSystemUI();
                    binding.stepDirections.setVisibility(View.GONE);
                    binding.exoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    binding.exoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                } else {
                    binding.stepDirections.setText(mRecipeStep.getDescription());
                }
            } else {
                binding.exoPlayerView.setVisibility(View.GONE);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                        !getResources().getBoolean(R.bool.isTablet)) {
                    hideSystemUI();
                    binding.videoPlaceholder.setVisibility(View.VISIBLE);
                } else {
                    binding.stepDirections.setText("\n\n\n"+ mRecipeStep.getDescription());
                    //binding.stepDirections.setGravity(Gravity.CENTER);
                   // binding.videoPlaceholder.setVisibility(View.VISIBLE);
                }
            }
            return view;
        }
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void hideSystemUI() {
        ActionBar actionBar=((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.hide();

        View decorView =getActivity().getWindow().getDecorView();
        int uiOptions =View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;

        decorView.setSystemUiVisibility(uiOptions);
    }



    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getContext(), TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            binding.exoPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(getContext(), String.valueOf(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(playReady);
            mExoPlayer.seekTo(currentWindowIndex, playPosition);//
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            playPosition = mExoPlayer.getCurrentPosition();
            currentWindowIndex = mExoPlayer.getCurrentWindowIndex();
            playReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoPlayer != null) {
            playPosition = mExoPlayer.getCurrentPosition();
            playReady = mExoPlayer.getPlayWhenReady();
            currentWindowIndex = mExoPlayer.getCurrentWindowIndex();
        }
        outState.putLong(PLAYER_POSITION, playPosition);
        outState.putBoolean(PLAYBACK_READY, playReady);
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }



























}
