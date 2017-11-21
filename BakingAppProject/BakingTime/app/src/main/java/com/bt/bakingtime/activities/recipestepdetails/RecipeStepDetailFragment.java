package com.bt.bakingtime.activities.recipestepdetails;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bt.bakingtime.R;
import com.bt.bakingtime.activities.recipedetaillist.RecipeDetailListFragment;
import com.bt.bakingtime.data.Recipe;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class RecipeStepDetailFragment extends Fragment implements ExoPlayer.EventListener
{
    private static final java.lang.String TAG = RecipeDetailListFragment.class.getSimpleName();
    private static final String PLAY_VIEW_WHEN_FOREGROUND = "playViewWhenForeground";
    private static final String CURRENT_POSITION = "currentPosition";
    private static final String RECIPE_VIDEO_URL = "recipeViewoUrl";

    private Recipe.RecipeStep mRecipeStep;
    private TextView mUpperTextView;
    private TextView mLowerTextView;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private boolean mPlayViewWhenForeground;
    private long mLastPosition;
    private String mRecipeVideoUrl;

    public RecipeStepDetailFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initializeMediaSession();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null)
        {
            mPlayViewWhenForeground = savedInstanceState.getBoolean(PLAY_VIEW_WHEN_FOREGROUND);
            mLastPosition = savedInstanceState.getLong(CURRENT_POSITION);
            mRecipeVideoUrl = savedInstanceState.getString(RECIPE_VIDEO_URL);
        }
        else
        {
            mPlayViewWhenForeground = false;
            mLastPosition = 0;
            mRecipeVideoUrl = "";
        }
    }

    @Override public void onStart()
    {
        super.onStart();

        if(mExoPlayer == null)
        {
            initializePlayer(Uri.parse(mRecipeVideoUrl));
        }

        mExoPlayer.seekTo(mLastPosition);
        mExoPlayer.setPlayWhenReady(mPlayViewWhenForeground);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mUpperTextView = (TextView) view.findViewById(R.id.tv_step_upper_label);
        mLowerTextView = (TextView) view.findViewById(R.id.tv_step_lower_label);
        mSimpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.simpleExoPlayerView);
    }

    public void setRecipeStepDetail(Recipe.RecipeStep recipeStep)
    {
        mRecipeStep = recipeStep;

        mUpperTextView.setText("Step: " + mRecipeStep.getShortDescription());
        mLowerTextView.setText(mRecipeStep.getLongDescription());

        mRecipeVideoUrl = mRecipeStep.getVideoUrl();
        initializePlayer(Uri.parse(mRecipeVideoUrl));
    }

    private void initializePlayer(Uri mediaUri)
    {
        if(mExoPlayer == null)
        {
            LoadControl loadControl = new DefaultLoadControl();
            TrackSelector trackSelector = new DefaultTrackSelector();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this.getContext(), trackSelector, loadControl);
            mSimpleExoPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(this.getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(this.getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

            addThumbnail();
        }
    }

    private void addThumbnail()
    {
        Target target = new Target()
        {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
            {
                mSimpleExoPlayerView.setDefaultArtwork(bitmap);
                mSimpleExoPlayerView.setBackground(new BitmapDrawable(getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };

        if(!mRecipeStep.getThumbnailUrl().isEmpty())
        {
            Picasso.with(this.getContext()).load(mRecipeStep.getThumbnailUrl()).into(target);
        }
        else
        {
            Toast.makeText(getContext(), "No thumbnail found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeMediaSession()
    {
        mMediaSession = new MediaSessionCompat(this.getContext(), TAG);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions( PlaybackStateCompat.ACTION_PLAY |
                             PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);
    }

    @Override public void onPause()
    {
        super.onPause();

        releasePlayer();
        mMediaSession.setActive(false);
    }

    private void releasePlayer()
    {
        if(mExoPlayer != null)
        {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
    }

    @Override public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        if(mExoPlayer != null)
        {
            mPlayViewWhenForeground = mExoPlayer.getPlayWhenReady();
            mLastPosition = mExoPlayer.getCurrentPosition();

            outState.putBoolean(PLAY_VIEW_WHEN_FOREGROUND, mPlayViewWhenForeground);
            outState.putString(RECIPE_VIDEO_URL, mRecipeVideoUrl);
            outState.putLong(CURRENT_POSITION, mLastPosition);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest)
    {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections)
    {

    }

    @Override
    public void onLoadingChanged(boolean isLoading)
    {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState)
    {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady)
        {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), 1f);
        }
        else if((playbackState == ExoPlayer.STATE_READY))
        {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 1f);
        }

        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error)
    {

    }

    @Override
    public void onPositionDiscontinuity()
    {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback
    {
        @Override
        public void onPlay()
        {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause()
        {
            mExoPlayer.setPlayWhenReady(false);
        }
    }
}
