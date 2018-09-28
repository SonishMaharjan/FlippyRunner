package ads;

/**
 * Created by so_ni on 3/27/2018.
 */

public interface AdController {



    void showBanner();
    void showInterstitial();
    void showRewardVideo();
    boolean isNetworkConnected();
    boolean isVideoAdLoaded();
    boolean isVideoAdWatced();

}
