package hudson.plugins.tfs.remote;

import com.microsoft.tfs.core.clients.build.IBuildDefinition;
import com.microsoft.tfs.core.clients.build.IBuildDetail;
import com.microsoft.tfs.core.clients.build.IBuildServer;
import com.microsoft.tfs.core.clients.build.IQueuedBuild;
import com.microsoft.tfs.core.clients.build.flags.BuildStatus;
import com.microsoft.tfs.core.clients.build.flags.QueryOptions;
import com.microsoft.tfs.core.clients.build.flags.QueueStatus;
import com.microsoft.tfs.core.util.TSWAHyperlinkBuilder;
import hudson.AbortException;
import hudson.Extension;
import hudson.Launcher;
import hudson.console.HyperlinkNote;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.plugins.tfs.model.Server;
import hudson.plugins.tfs.util.BuildInformationPrinter;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;

/**
 * Created by snpaux on 03.03.2015.
 */
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

        Server server = null;

        try {
            server = new Server(url, username, password);

            TSWAHyperlinkBuilder hyperlinkBuilder = new TSWAHyperlinkBuilder(server.getTeamProjectCollection());
                    
            IBuildServer buildServer = server.getTeamProjectCollection().getBuildServer();

            IBuildDefinition definition = buildServer.getBuildDefinition(teamProject, buildDefinition);

            IQueuedBuild tfsBuild = buildServer.queueBuild(definition);

            log.print("Starting a new TFS Build of: " + buildDefinition + " ");

            waitForJobStart(tfsBuild);

            IBuildDetail buildDetail = tfsBuild.getBuild();
            buildDetail.refresh(new String[] {"*"}, QueryOptions.NONE);

            URI detailsURL = hyperlinkBuilder.getViewBuildDetailsURL(buildDetail.getURI());
            log.println("(" + HyperlinkNote.encodeTo(detailsURL.toString(), buildDetail.getBuildNumber()) + ")");


            TFSJobLinkAction linkAction = new TFSJobLinkAction(detailsURL.toString(), buildDetail.getBuildNumber(), BuildStatus.IN_PROGRESS);
            build.addAction(linkAction);
            
            waitForCompletion(build, log, tfsBuild);

            // Display the status of the completed build.
            buildDetail.refresh(new String[] {"*"}, QueryOptions.NONE);
            BuildStatus buildStatus = buildDetail.getStatus();

            BuildInformationPrinter.printInformationToStream(buildDetail.getInformation(), log);
            
            log.println("Build " + buildDetail.getBuildNumber() + " completed with status " + tfsBuild.getBuildServer().getDisplayText(buildStatus));

            linkAction.setStatus(buildStatus);
            
            if (buildStatus.equals(BuildStatus.STOPPED)) {
                throw new InterruptedException("The TFS build has been aborted on TFS side");
            }
            
            if (buildStatus.equals(BuildStatus.FAILED)) {
                throw new AbortException("The TFS Build failed");
            }
            
            if (buildStatus.equals(BuildStatus.PARTIALLY_SUCCEEDED)) {
                build.setResult(build.getResult().combine(Result.UNSTABLE));
            }
            
        } finally {
            server.close();
        }

        return true;
    }

    private void waitForJobStart(IQueuedBuild tfsBuild) throws InterruptedException {

        IBuildDetail buildDetail = tfsBuild.getBuild();
        while (!tfsBuild.getStatus().equals(QueueStatus.COMPLETED) && buildDetail.getStatus().equals(BuildStatus.NOT_STARTED)) {
            Thread.sleep(2000);
            tfsBuild.refresh(QueryOptions.NONE);
            buildDetail.refresh(new String[] {"GetStatus"}, QueryOptions.NONE);
        }
    }

    private void waitForCompletion(AbstractBuild<?, ?> build, PrintStream log, IQueuedBuild tfsBuild) throws InterruptedException {
        try {
            while (!tfsBuild.getStatus().equals(QueueStatus.COMPLETED)) {
                Thread.sleep(2000);
                tfsBuild.refresh(QueryOptions.NONE);
            }
        } catch (InterruptedException e) {
            log.println("Build has been aborted, aborting TFS build as well...");

            IBuildDetail buildDetail = tfsBuild.getBuild();
            buildDetail.refresh(new String[] {"GetStatus"}, QueryOptions.NONE);
            
            if (buildDetail.getStatus().equals(BuildStatus.NOT_STARTED))
                tfsBuild.cancel();
            else
                // build already started
                tfsBuild.getBuildServer().stopBuilds(new IBuildDetail[] {buildDetail});

            build.setResult(Result.ABORTED);
            throw e;
        }
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
