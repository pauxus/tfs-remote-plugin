package jenkins.plugins.tfscontrol.remote;

import com.blackbuild.tfs.rest.api.TFSWrapperException;
import com.blackbuild.tfs.rest.api.TfsConnection;
import com.blackbuild.tfs.rest.api.builds.LiveBuild;
import com.blackbuild.tfs.rest.api.model.BuildDefinition;
import hudson.AbortException;
import hudson.Extension;
import hudson.Launcher;
import hudson.console.HyperlinkNote;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.io.PrintStream;

@SuppressWarnings("unused")
public class TFSBuilder extends Builder {
    
    private String url;
    private String teamProject;
    private String buildDefinition;
    private String username;
    private String password; // TODO Credentials Plugin

    @DataBoundConstructor
    public TFSBuilder(String url, String teamProject, String buildDefinition, String username, String password) {
        this.url = url;
        this.teamProject = teamProject;
        this.buildDefinition = buildDefinition;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        PrintStream log = listener.getLogger();

        try {
            TfsConnection connection = new TfsConnection(url + "/" + teamProject + "/", username, password, "GfK");

            LiveBuild tfsBuild = new LiveBuild(connection, connection.queueBuild(buildDefinition).run());

            BuildDefinition definition = connection.getBuildDefinition(tfsBuild.getBuild().getDefinition()).run();

            log.println("Queueing " + HyperlinkNote.encodeTo(definition.getLinks().get("web").get("href"), buildDefinition));

            tfsBuild.waitForInProgress();

            log.println("TFS Build is running: " + HyperlinkNote.encodeTo(tfsBuild.getBuild().getLinks().get("web").get("href"), tfsBuild.getBuild().getFullName()));

            TFSJobLinkAction linkAction = build.getAction(TFSJobLinkAction.class);
            
            if (linkAction == null) {
                linkAction = new TFSJobLinkAction();
                build.addAction(linkAction);
            }
            
            TFSBuildResult buildResult = new TFSBuildResult(buildDefinition, tfsBuild.getBuild().getFullName(), tfsBuild.getBuild().getLinks().get("web").get("href"), tfsBuild.getBuild().getUrl(), null);
            linkAction.addBuildResult(buildResult);

            tfsBuild.waitForCompletion();

            com.blackbuild.tfs.rest.api.model.Result result = tfsBuild.getBuild().getResult();
            log.println("Build " + tfsBuild.getBuild().getBuildNumber() + " completed with status " + result);

            buildResult.setBuildResult(result);
            
            if (result == com.blackbuild.tfs.rest.api.model.Result.canceled) {
                throw new InterruptedException("The TFS build has been aborted on TFS side");
            }

            if (result == com.blackbuild.tfs.rest.api.model.Result.failed) {
                throw new AbortException("The TFS Build failed");
            }

            if (result == com.blackbuild.tfs.rest.api.model.Result.partiallySucceeded) {
                Result oldResult = build.getResult();
                build.setResult(oldResult == null ? Result.UNSTABLE : build.getResult().combine(Result.UNSTABLE));
            }
            
        } catch (com.mashape.unirest.http.exceptions.UnirestException e) {
            throw new IOException(e);
        } catch (TFSWrapperException e) {
            throw new IOException(e);
        }

        return true;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTeamProject() {
        return teamProject;
    }

    public void setTeamProject(String teamProject) {
        this.teamProject = teamProject;
    }

    public String getBuildDefinition() {
        return buildDefinition;
    }

    public void setBuildDefinition(String buildDefinition) {
        this.buildDefinition = buildDefinition;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        // TODO Form Validation
        
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Run TFS Build";
        }
    }
    
}
