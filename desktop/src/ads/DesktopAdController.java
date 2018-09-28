package ads;

import com.badlogic.gdx.utils.Logger;

/**
 * Created by so_ni on 3/27/2018.
 */

public class DesktopAdController implements AdController {
    private  static final Logger log = new Logger(DesktopAdController.class.getSimpleName());

    @Override
    public void showBanner() {
        log.debug("show Banner");

    }

    @Override
    public void showInterstitial() {
        log.debug("sow Interstitial");
    }

    @Override
    public void showRewardVideo() {

    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public boolean isVideoAdLoaded() {
        return true;
    }

    @Override
    public boolean isVideoAdWatced() {
        return true;
    }
}
