package com.collegeproject.mysocialmedia.Beans;

import java.util.Date;

/**
 * @author Tomin Bijaimon Azhakathu
 * @location Dublin, Ireland
 * @date 25 August 2019, 12:55 Irish Standard Time
 **/


public class FileDBEntry {
    private String entryId;
    private String fileName;
    private String fileDescription;
    private boolean isPrivate;
    private String urlPath;
    private boolean isDeleted;
    private String userID;
    private Date dateOfAddition;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Date getDateOfAddition() {
        return dateOfAddition;
    }

    public void setDateOfAddition(Date dateOfAddition) {
        this.dateOfAddition = dateOfAddition;
    }

    public void setUserName(String displayName) {
        this.userName = displayName;
    }
}
