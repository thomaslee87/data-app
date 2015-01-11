package com.intellbi.data.pojo;

public class UserDO {
    private int id;
    private int groupId;
    private String username;
    private String realname;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(obj instanceof UserDO) {
            return ((UserDO)obj).getUsername().equals(username);
        }
        return false;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return username.hashCode();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
    

}
