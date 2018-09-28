package com.mikh.updown;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mikh.updown.GameMain;

import ads.AdController;
import ads.AdUtils;
import ads.AdsUnitIds;
import services.PlayServices;

public class AndroidLauncher extends AndroidApplication implements AdController, PlayServices, RewardedVideoAdListener {

	private AdView bannerAdView;

	private InterstitialAd interstitialAd;

	private RewardedVideoAd mAd;


	private GoogleSignInClient mGoogleSignInClient;

	private AchievementsClient mAchievementsClient;
	private LeaderboardsClient mLeaderboardsClient;
	private PlayersClient mPlayersClient;


	private static final int RC_UNUSED = 5001;
	private static final int RC_SIGN_IN = 9001;
	int val;
	static  final int LB = 1;

	boolean vAdloaded,vAdWatched,tempBool;




	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initAds();
		initUI();

		mGoogleSignInClient = GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());




	}

	@Override
	protected void onStart()
	{
		super.onStart();


	}

	@Override
	protected void onResume() {
		super.onResume();//call this or there will be error
		bannerAdView.resume();
		signInSilently();
	}

	@Override
	protected void onPause() {
		super.onPause();
		bannerAdView.pause();
	}



	@Override
	protected void onDestroy() {
		bannerAdView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onStop()
	{
		super.onStop();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == RC_SIGN_IN) {
			Task<GoogleSignInAccount> task =
					GoogleSignIn.getSignedInAccountFromIntent(intent);

			try {
				GoogleSignInAccount account = task.getResult(ApiException.class);
				onConnected(account);
				System.out.println("Sumuki UI sign in succes");
				if(val==LB)
				{
					onShowLeaderboardsRequested();
					val=0;
				}

			} catch (ApiException apiException) {
			/*	String message = apiException.getMessage();
				if (message == null || message.isEmpty()) {
					message = getString(R.string.signin_other_error);
				}
		*/
				onDisconnected();

			/*	new AlertDialog.Builder(this)
						.setMessage(message)
						.setNeutralButton(android.R.string.ok, null)
						.show();*/
				System.out.println("Sumuki UI sign in failes");
				Toast.makeText(this,"Couldn't Sign In",1/1 ).show();
			}
		}
	}

	@Override
	public void showBanner() {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				loadBanner();
			}
		});

	}

	@Override
	public void showInterstitial() {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showInterstitialInternal();
			}
		});


	}

	@Override
	public void showRewardVideo() {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showRewardVideoInternal();
			}
		});


	}

	void showRewardVideoInternal()
	{
		if(mAd.isLoaded())
		{
			mAd.show();

		}
	}

	void showInterstitialInternal()
	{
		if(interstitialAd.isLoaded())
		{
			interstitialAd.show();

		}

		loadInterstitial();


	}
	void loadInterstitial()
	{
		if(isNetworkConnected())
		{
			interstitialAd.loadAd(AdUtils.buidRequest());
		}
	}

	@Override
	public boolean isNetworkConnected() {
		//checking connection
		ConnectivityManager connectivityManager =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		return networkInfo != null && networkInfo.isConnected(); //dk why but write && not &

		//CB6F04095AA644FF7F7D26EBCEE9AC21//tester ad

	}

	@Override
	public boolean isVideoAdLoaded() {




		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				vAdloaded = mAd.isLoaded();
			}
		});

		return vAdloaded;

	}

	@Override
	public boolean isVideoAdWatced() {

		tempBool = vAdWatched;
		vAdWatched = false;

		return tempBool;
	}




	void initAds(){

		bannerAdView = new AdView(this);//androidApplication extent activity and Activity is context
		bannerAdView.setId(R.id.adViewId); //choose R from game not form google.android

		bannerAdView.setAdUnitId(AdsUnitIds.BANNER_ID);
		bannerAdView.setAdSize(AdSize.SMART_BANNER);


		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(AdsUnitIds.INTERSTTITIAL_ID);

		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				//super.onAdClosed();
				loadInterstitial();
			}
		});

		loadInterstitial();

		MobileAds.initialize(this,AdsUnitIds.REWARD_ID);

		mAd = MobileAds.getRewardedVideoAdInstance(this);
		mAd.setRewardedVideoAdListener(this);
		loadVidAd();

	}

	void loadVidAd()
	{
		if(isNetworkConnected())
		{
			mAd.loadAd(AdsUnitIds.REWARD_ID,AdUtils.buidRequest());

		}
	}

	void initUI()
	{

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//initialize(new GameMain(this), config);

		View gameView = initializeForView(new GameMain(this,this),config);

		RelativeLayout layout = new RelativeLayout(this);//relative layout is similar as table in libgdx


		//ad view params
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT
		);

		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		//game view params
		//ViewGroup.LayoutParams.MATCH_PARENT for full screen
		RelativeLayout.LayoutParams gameParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


		//launcing game above ad
		//gameParams.addRule(RelativeLayout.ABOVE,bannerAdView.getId());//id from stings.xml is called

		//adding those two views to lay out

		layout.addView(gameView,gameParams); // game params no adparams
		layout.addView(bannerAdView,adParams);

		setContentView(layout);//otherwise screen will be blank

	}


	private void loadBanner()
	{
		if(isNetworkConnected())
		{
			bannerAdView.loadAd(AdUtils.buidRequest());
		}
	}

	//GpServices

	private void signInSilently() {
		//Log.d(TAG, "signInSilently()");

		//	mGoogleSignInClient = GoogleSignIn.getClient(this,
		//			GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);

	/*	mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
				new OnCompleteListener<GoogleSignInAccount>() {
					@Override
					public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
						if (task.isSuccessful()) {
						//	Log.d(TAG, "signInSilently(): success");
							onConnected(task.getResult());


						} else {
						//	Log.d(TAG, "signInSilently(): failure", task.getException());
							onDisconnected();

							System.out.println("Sumuki Silent sign in not sucses" );
							startSignInIntent();
						}
					}
				});
				*/

		/////
		mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
				new OnCompleteListener<GoogleSignInAccount>() {
					@Override
					public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
						if (task.isSuccessful()) {
							//Log.d(TAG, "signInSilently(): success");
							onConnected(task.getResult());
							//System.out.println("Sumuki Silent sign in sucses" );
						} else {
							//Log.d(TAG, "signInSilently(): failure", task.getException());
							onDisconnected();


							//System.out.println("Sumuki Silent sign in not sucses" );

						}
					}
				});
	}


	private void startSignInIntent() {
		startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
	}


	private void onConnected(GoogleSignInAccount googleSignInAccount) {
		//Log.d(TAG, "onConnected(): connected to Google APIs");

		mPlayersClient = Games.getPlayersClient(this, googleSignInAccount);
		mAchievementsClient = Games.getAchievementsClient(this, googleSignInAccount);
		mLeaderboardsClient = Games.getLeaderboardsClient(this, googleSignInAccount);



		// Set the greeting appropriately on main menu
		mPlayersClient.getCurrentPlayer()
				.addOnCompleteListener(new OnCompleteListener<Player>() {
					@Override
					public void onComplete(@NonNull Task<Player> task) {

						if (task.isSuccessful()) {

						}
						else {
							Exception e = task.getException();
						//	handleException(e, getString(R.string.players_exception));
							onDisconnected();

						}

					}
				});


	/*	// if we have accomplishments to push, push them
		if (!mOutbox.isEmpty()) {
			pushAccomplishments();
			Toast.makeText(this, getString(R.string.your_progress_will_be_uploaded),
					Toast.LENGTH_LONG).show();
		}
*/
		//	loadAndPrintEvents();
	}

	private void onDisconnected() {
		//Log.d(TAG, "onDisconnected()");

		mAchievementsClient = null;
		mLeaderboardsClient = null;
		mPlayersClient = null;

	}

	private void handleException(Exception e, String details) {
		int status = 0;

		if (e instanceof ApiException) {
			ApiException apiException = (ApiException) e;
			status = apiException.getStatusCode();
		}

		String message = getString(R.string.status_exception_error, details, status, e);

		new AlertDialog.Builder(this)
				.setMessage(message)
				.setNeutralButton(android.R.string.ok, null)
				.show();
	}





	@Override
	public void onShowLeaderboardsRequested() {

		if(mGoogleSignInClient!=null)
		{
			if(mLeaderboardsClient!= null)
			{


				mLeaderboardsClient.getAllLeaderboardsIntent()
						.addOnSuccessListener(new OnSuccessListener<Intent>() {
							@Override
							public void onSuccess(Intent intent) {
								startActivityForResult(intent, RC_UNUSED);
							}
						})
						.addOnFailureListener(new OnFailureListener() {
							@Override
							public void onFailure(@NonNull Exception e) {
								//handleException(e, getString(R.string.achievements_exception));

								onDisconnected();
							}
						});





			}
			else
			{
				val =LB;
				startSignInIntent();

			}
		}

	/* called in doc
	Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
				.getLeaderboardIntent(getString(R.string.leaderboard_toppers))
				.addOnSuccessListener(new OnSuccessListener<Intent>() {
					@Override
					public void onSuccess(Intent intent) {
						startActivityForResult(intent, RC_LEADERBOARD_UI);
					}
				});

	*/

	}

	@Override
	public void updateLeaderboards(int finalScore) {
		if(mLeaderboardsClient != null)
		{
			mLeaderboardsClient.submitScore(getString(R.string.leaderboard_toppers),
					finalScore);

		}
	}

	@Override
	public void signIn() {
			startSignInIntent();

	}


	@Override
	public void onRewardedVideoAdLoaded() {

	}

	@Override
	public void onRewardedVideoAdOpened() {

	}

	@Override
	public void onRewardedVideoStarted() {

	}

	@Override
	public void onRewardedVideoAdClosed() {

		//mAd.loadAd(AdsUnitIds.REWARD_ID,AdUtils.buidRequest());

		loadVidAd();
	}

	@Override
	public void onRewarded(RewardItem rewardItem) {

		System.out.println("Revive is rewarded::"+rewardItem);
		vAdWatched = true;
	}

	@Override
	public void onRewardedVideoAdLeftApplication() {

	}

	@Override
	public void onRewardedVideoAdFailedToLoad(int i) {
		loadVidAd();
	}
}
