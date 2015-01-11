package com.intellbi.data.pojo;

public class ConsumerDO {
    private int id;
    private String phone;
    private String identity;
    private int gender;
    private boolean isPhoto;
    private boolean isPhoto4G;
    
    private int status;

    private int userId;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isPhoto() {
        return isPhoto;
    }

    public void setPhoto(boolean isPhoto) {
        this.isPhoto = isPhoto;
    }

    public boolean isPhoto4G() {
        return isPhoto4G;
    }

    public void setPhoto4G(boolean isPhoto4G) {
        this.isPhoto4G = isPhoto4G;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(obj instanceof ConsumerDO) {
            return ((ConsumerDO) obj).getPhone().equals(phone);
        }
        return false;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return phone.hashCode();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
}
