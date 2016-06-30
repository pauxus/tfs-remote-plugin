package jenkins.plugins.tfscontrol.remote;


import com.blackbuild.tfs.rest.api.model.Result;

import java.io.Serializable;

@SuppressWarnings("unused")
public class TFSBuildResult implements Serializable {

    private final static int IN_PROGRESS = 1;
    private final static int SUCCEEDED = 2;
    private final static int PARTIALLY_SUCCEEDED = 4;
    private final static int FAILED = 8;
    private final static int STOPPED = 16;
    private final static int NOT_STARTED = 32;

    private String buildDefinitionName;
    
    private String buildName;
    
    private String buildURI;
    
    private String buildURL;
    
    private Result buildResult;
    
    public TFSBuildResult(String buildDefinitionName, String buildName, String buildURI, String buildURL, Result result) {
        this.buildDefinitionName = buildDefinitionName;
        this.buildName = buildName;
        this.buildURI = buildURI;
        this.buildURL = buildURL;
        this.buildResult = result;
    }

    public hudson.model.Result getResult() {

        switch (buildResult) {
            case succeeded:
                return hudson.model.Result.SUCCESS;
            case partiallySucceeded:
                return hudson.model.Result.UNSTABLE;
            case failed:
                return hudson.model.Result.FAILURE;
            case canceled:
                return hudson.model.Result.ABORTED;
            default:
                return hudson.model.Result.NOT_BUILT;
        }
    }

    public String getBuildDefinitionName() {
        return buildDefinitionName;
    }

    public String getBuildName() {
        return buildName;
    }

    public String getBuildURI() {
        return buildURI;
    }

    public String getBuildURL() {
        return buildURL;
    }

    public Result getBuildResult() {
        return buildResult;
    }

    public void setBuildDefinitionName(String buildDefinitionName) {
        this.buildDefinitionName = buildDefinitionName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public void setBuildURI(String buildURI) {
        this.buildURI = buildURI;
    }

    public void setBuildURL(String buildURL) {
        this.buildURL = buildURL;
    }

    public void setBuildResult(Result buildResult) {
        this.buildResult = buildResult;
    }

}
