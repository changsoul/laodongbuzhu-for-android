package com.wudaosoft.laodongbuzhu.model;

/**
 * Created on 2018/3/24 01:30.
 *
 * @author Changsoul.Wu
 */

public class ApplyRecord {

    private String applyNo;
    private String applyStatus;
    private String applyAmount;
    private String applyDate;
    private String applyJob;

    public ApplyRecord() {
    }

    public ApplyRecord(String applyNo, String applyStatus, String applyAmount, String applyDate, String applyJob) {
        this.applyNo = applyNo;
        this.applyStatus = applyStatus;
        this.applyAmount = applyAmount;
        this.applyDate = applyDate;
        this.applyJob = applyJob;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(String applyAmount) {
        this.applyAmount = applyAmount;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getApplyJob() {
        return applyJob;
    }

    public void setApplyJob(String applyJob) {
        this.applyJob = applyJob;
    }
}
