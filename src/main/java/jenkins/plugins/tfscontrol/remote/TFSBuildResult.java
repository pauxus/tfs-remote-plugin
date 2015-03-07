package jenkins.plugins.tfscontrol.remote;

import com.microsoft.tfs.core.clients.build.flags.BuildStatus;
import hudson.model.Result;

import java.io.Serializable;
import java.net.URI;

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
    
    private BuildStatus status;

    public TFSBuildResult(String buildDefinitionName, String buildName, String buildURI, String buildURL, BuildStatus status) {
        this.buildDefinitionName = buildDefinitionName;
        this.buildName = buildName;
        this.buildURI = buildURI;
        this.buildURL = buildURL;
        this.status = status;
    }

    public Result getResult() {

        switch (status.toIntFlags()) {
            case IN_PROGRESS:
                return Result.NOT_BUILT;
            case SUCCEEDED:
                return Result.SUCCESS;
            case PARTIALLY_SUCCEEDED:
                return Result.UNSTABLE;
            case FAILED:
                return Result.FAILURE;
            case STOPPED:
                return Result.ABORTED;
            case NOT_STARTED:
                return Result.NOT_BUILT;
            default:
                return Result.NOT_BUILT;
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

    public BuildStatus getStatus() {
        return status;
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

    public void setStatus(BuildStatus status) {
        this.status = status;
    }
}
