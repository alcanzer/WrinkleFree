package biz.wrinklefree.wrinklefree;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by alcanzer on 1/6/18.
 */

public class UserInfoPref {
    static final String PREF_NAME = "wrinklefree.userpref";
    static final String ADD_ONE = "AddressOne";
    static final String ADD_TWO = "AddressTwo";
    static final String LANDMARK = "Landmark";
    static final String PINCODE = "Pincode";
    static final String FIRST_NAME = "FirstName";
    static final String LAST_NAME = "LastName";
    static final String MOBILE_NUMBER = "MobileNumber";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private static UserInfoPref userInfoPref;
    Context ctx;


    public UserInfoPref(Context ctx){
        this.ctx = ctx;
        preferences = ctx.getSharedPreferences(PREF_NAME, 0);
        editor = preferences.edit();
    }


    public String getKey(String key){
        return preferences.getString(key, null);
    }

    public void putKey(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }

}
