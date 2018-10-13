package com.example.pedro.bakingit;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsActivity extends AppCompatActivity {

    private static final String CURRENT_POSITION = "current_position";
    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.txt_step_description)
    TextView txtStepDescription;
    @BindView(R.id.btn_previous)
    TextView btnPrevious;
    @BindView(R.id.btn_next)
    TextView btnNext;
    @BindView(R.id.btn_close)
    ImageView btnClose;

    private static final String RECIPE_EXTRA ="recipe_extra" ;
    private static final String STEP_EXTRA = "step_extra";
    private static final String STEP_INDEX = "step_index";
    private SimpleExoPlayer mExoPlayer;
    Recipe recipe;
    Step step;
    int index;
    Long currentPosition = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        recipe = extras.getParcelable(RECIPE_EXTRA);
        step = extras.getParcelable(STEP_EXTRA);
        index = extras.getInt(STEP_INDEX);

        if(savedInstanceState !=null){
            currentPosition = savedInstanceState.getLong(CURRENT_POSITION);
        }
        populateStep(step);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExoPlayer.stop();
                mExoPlayer.release();
                finish();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;
                if(recipe.getSteps().size()== (index)){
                    Toast.makeText(StepDetailsActivity.this, R.string.last_step,Toast.LENGTH_LONG).show();
                    index--;
                }else{
                    step = recipe.getSteps().get(index);
                    populateStep(step);
                }
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index--;
                if(index<0){
                    Toast.makeText(StepDetailsActivity.this, R.string.first_step,Toast.LENGTH_LONG).show();
                    index = 0;
                }else{
                    step = recipe.getSteps().get(index);
                    populateStep(step);
                }
            }
        });

    }

    private void populateStep(Step step) {
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.recipe_thumb));

        txtStepDescription.setText(step.getDescription());

        if(mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer = null;
        }
        if(step.getVideoURL() != null && !step.getVideoURL().isEmpty()){
            initializePlayer(Uri.parse(step.getVideoURL()));
        }else if(step.getThumbnailURL() != null && !step.getThumbnailURL().isEmpty()){
            initializePlayer(Uri.parse(step.getThumbnailURL()));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mExoPlayer.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mExoPlayer.release();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(CURRENT_POSITION, mExoPlayer.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }

    private void initializePlayer(Uri mediaUri) {
        // Create an instance of the ExoPlayer.
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        mExoPlayer.seekTo(currentPosition);
        mPlayerView.setPlayer(mExoPlayer);
        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(this, "BakeIt");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                this, userAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);

    }
}
