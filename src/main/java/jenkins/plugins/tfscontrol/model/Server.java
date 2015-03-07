package jenkins.plugins.tfscontrol.model;

import com.microsoft.tfs.core.TFSConfigurationServer;
import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.httpclient.Credentials;
import com.microsoft.tfs.core.httpclient.DefaultNTCredentials;
import com.microsoft.tfs.core.httpclient.UsernamePasswordCredentials;
import com.microsoft.tfs.core.util.CredentialsUtils;
import com.microsoft.tfs.core.util.URIUtils;
import com.microsoft.tfs.util.Closable;
import jenkins.plugins.tfslib.TfsSdkLibAccess;

import java.net.URI;

public class Server extends TfsSdkLibAccess implements Closable {
    
    private final String url;
    private final String userName;
    private final String userPassword;
    private final TFSTeamProjectCollection tpc;

    public Server(String url, String username, String password) {
        this.url = url;
        this.userName = username;
        this.userPassword = password;
        final URI uri = URIUtils.newURI(url);

        Credentials credentials = null;
        // In case no user name is provided and the current platform supports
        // default credentials, use default credentials
        if ((username == null || username.length() == 0) && CredentialsUtils.supportsDefaultCredentials()) {
            credentials = new DefaultNTCredentials();
        }
        else if (username != null && password != null) {
            credentials = new UsernamePasswordCredentials(username, password);
        }

        if (credentials != null) {
            this.tpc = new TFSTeamProjectCollection(uri, credentials);
        }
        else {
            this.tpc = null;
        }
    }

    public TFSTeamProjectCollection getTeamProjectCollection()
    {
        return this.tpc;
    }
    
    public String getUrl() {
        return url;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public synchronized void close() {
        if (this.tpc != null) {
            TFSConfigurationServer configurationServer = tpc.getConfigurationServer();
            if (configurationServer != null) {
                configurationServer.close();
            }
            this.tpc.close();
        }
        
    }
}
