package hudson.plugins.tfs.remote.TFSJobLinkAction

import hudson.plugins.tfs.remote.TFSBuildResult
import lib.LayoutTagLib

l=namespace(LayoutTagLib)
t=namespace("/lib/hudson")
st=namespace("jelly:stapler")
f=namespace("lib/form")

t.summary(icon: my.combinedStatus.color.image) {
    h3("Executed TFS Jobs")
    ul {
        my.buildResults.each { TFSBuildResult tfsJob ->
            a(href: tfsJob.buildURL, tfsJob.buildName)
        }
    }
}
