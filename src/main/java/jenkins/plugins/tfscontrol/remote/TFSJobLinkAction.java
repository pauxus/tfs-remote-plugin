package jenkins.plugins.tfscontrol.remote;

import hudson.model.Action;
import hudson.model.Result;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ExportedBean(defaultVisibility = 2)
public class TFSJobLinkAction implements Action, Serializable {

    private List<TFSBuildResult> buildResults = new ArrayList<TFSBuildResult>();
    
    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return "Executed TFS Build";
    }

    @Exported
    public String getUrlName() {
        return null;  // no special page yet
    }

    @Exported
    public List<TFSBuildResult> getBuildResults() {
        return buildResults;
    }
    
    public Result getCombinedStatus() {
        Result result = Result.SUCCESS;
        
        for (TFSBuildResult buildResult : buildResults) {
            result = result.combine(buildResult.getResult());
        }
        
        return result;
    }

    public void addBuildResult(TFSBuildResult buildResult) {
        buildResults.add(buildResult);
    }
}
