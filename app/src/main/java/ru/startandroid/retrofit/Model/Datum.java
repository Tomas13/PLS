package ru.startandroid.retrofit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Datum extends RealmObject {

    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("roleName")
    @Expose
    private String roleName;
    @SerializedName("groupName")
    @Expose
    private String groupName;
    @SerializedName("groupType")
    @Expose
    private String groupType;
    @SerializedName("current")
    @Expose
    private String current;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Object getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

}