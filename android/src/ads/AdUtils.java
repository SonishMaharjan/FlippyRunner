package ads;

import com.google.android.gms.ads.AdRequest;

/**
 * Created by so_ni on 3/29/2018.
 */

public class AdUtils {
    //for test ad
   // private  static final String TEST_DEVICE ="CB6F04095AA644FF7F7D26EBCEE9AC21";//comment this for real add


    public static AdRequest buidRequest()
    {
      return  new AdRequest.Builder().build();//uncomment this for real ad

      //  return (new AdRequest.Builder().addTestDevice(TEST_DEVICE).build());//comment this for real Ad
    }

    private AdUtils()
    {

    }
}
