package hudson.plugins.tfs.remote;

import com.microsoft.tfs.core.clients.build.flags.BuildStatus;
import hudson.model.Action;
import hudson.plugins.tfs.util.ImageUtil;
import org.kohsuke.stapler.export.Exported;

import java.io.Serializable;

/**
 * Created by snpaux on 04.03.2015.
 */
public class TFSJobLinkAction implements Action, Serializable {

    private String uri;

    private String jobName;
    private BuildStatus status;
    
    public TFSJobLinkAction(String uri, String jobName, BuildStatus status) {
        this.uri = uri;
        this.jobName = jobName;
        this.status = status;
    }

    @Exported
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public BuildStatus getStatus() {
        return status;
    }

    public void setStatus(BuildStatus status) {
        this.status = status;
    }

    @Exported
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getIconFileName() {
        return ImageUtil.getImageForStatus(status);
    }

    public String getDisplayName() {
        return "Executed TFS Build";
    }

    public String getUrlName() {
        return uri;
    }
}
