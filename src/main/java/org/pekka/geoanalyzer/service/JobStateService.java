package org.pekka.geoanalyzer.service;

import org.springframework.stereotype.Service;

@Service
public class JobStateService {

    private boolean jobStarted;
    private boolean jobFailed;
    private boolean jobSuccess;

    public boolean isJobStarted() {
        return jobStarted;
    }

    public boolean isJobFailed() {
        return jobFailed;
    }

    public void setJobFailed(boolean jobFailed) {
        this.jobFailed = jobFailed;
    }

    public void startJob() {
        this.jobStarted = true;
    }

    public void resetJob() {
        this.jobStarted = false;
        this.jobFailed = false;
    }

    public boolean isJobSuccess() {
        return jobSuccess;
    }

    public void jobFinished(boolean jobSuccess) {
        this.jobSuccess = jobSuccess;
    }
}
