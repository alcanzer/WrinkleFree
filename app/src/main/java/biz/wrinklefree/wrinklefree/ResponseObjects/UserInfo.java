package biz.wrinklefree.wrinklefree.ResponseObjects;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alcanzer on 11/13/17.
 */

public class UserInfo{


    private int userId;

    private String emailAddress;

    private long mobileNumber;

    private String firstName;

    private String lastName;

    private boolean mobileVerified;

    private boolean addressUpdated;

    private String countryMobileCode;

    private String otp;

    private boolean active;

    private long createdTimestamp;

    private long modifiedTimestamp;

    protected UserInfo(Parcel in) {
        userId = in.readInt();
        emailAddress = in.readString();
        mobileNumber = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        mobileVerified = in.readByte() != 0;
        addressUpdated = in.readByte() != 0;
        countryMobileCode = in.readString();
        otp = in.readString();
        active = in.readByte() != 0;
        createdTimestamp = in.readInt();
        modifiedTimestamp = in.readInt();
    }


    public void setUserId(int userId){
        this.userId = userId;
    }
    public int getUserId(){
        return this.userId;
    }
    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }
    public String getEmailAddress(){
        return this.emailAddress;
    }
    public void setMobileNumber(int mobileNumber){
        this.mobileNumber = mobileNumber;
    }
    public long getMobileNumber(){
        return this.mobileNumber;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public void setMobileVerified(boolean mobileVerified){
        this.mobileVerified = mobileVerified;
    }
    public boolean getMobileVerified(){
        return this.mobileVerified;
    }
    public void setAddressUpdated(boolean addressUpdated){
        this.addressUpdated = addressUpdated;
    }
    public boolean getAddressUpdated(){
        return this.addressUpdated;
    }
    public void setCountryMobileCode(String countryMobileCode){
        this.countryMobileCode = countryMobileCode;
    }
    public String getCountryMobileCode(){
        return this.countryMobileCode;
    }
    public void setOtp(String otp){
        this.otp = otp;
    }
    public String getOtp(){
        return this.otp;
    }
    public void setActive(boolean active){
        this.active = active;
    }
    public boolean getActive(){
        return this.active;
    }
    public void setCreatedTimestamp(int createdTimestamp){
        this.createdTimestamp = createdTimestamp;
    }
    public long getCreatedTimestamp(){
        return this.createdTimestamp;
    }
    public void setModifiedTimestamp(int modifiedTimestamp){
        this.modifiedTimestamp = modifiedTimestamp;
    }
    public long getModifiedTimestamp(){
        return this.modifiedTimestamp;
    }

}
