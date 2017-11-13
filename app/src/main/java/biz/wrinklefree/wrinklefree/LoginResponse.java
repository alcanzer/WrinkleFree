package biz.wrinklefree.wrinklefree;

import java.util.List;

/**
 * Created by alcanzer on 11/13/17.
 */

public class LoginResponse {

    private int status;

    private List<UserInfo> userInfo;

    private boolean isFirstTimeUser;

    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setUserInfo(List<UserInfo> userInfo){
        this.userInfo = userInfo;
    }
    public List<UserInfo> getUserInfo(){
        return this.userInfo;
    }
    public void setIsFirstTimeUser(boolean isFirstTimeUser){
        this.isFirstTimeUser = isFirstTimeUser;
    }
    public boolean getIsFirstTimeUser(){
        return this.isFirstTimeUser;
    }

}
