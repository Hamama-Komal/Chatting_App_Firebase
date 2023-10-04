package com.example.chatapp.ModelClasses;

public class UserClass {

    public UserClass(){

    }

    public UserClass(String uid, String name, String phoneNumber, String profilePic) {
        this.uid = uid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profilePic = profilePic;
    }

    private  String uid, name, phoneNumber, profilePic;

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfilePic() {
        return profilePic;
    }
}
