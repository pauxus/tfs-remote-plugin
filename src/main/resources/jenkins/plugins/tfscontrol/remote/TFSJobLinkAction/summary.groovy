package jenkins.plugins.tfscontrol.remote.TFSJobLinkAction

import jenkins.plugins.tfscontrol.remote.TFSBuildResult
import lib.LayoutTagLib

l=namespace(LayoutTagLib)
t=namespace("/lib/hudson")
st=namespace("jelly:stapler")
f=namespace("lib/form")

t.summary(icon: my.combinedStatus.color.image) {
    h3("Executed TFS Jobs")
    ul {
        my.buildResults.each { TFSBuildResult tfsJob ->
            a(href: tfsJob.buildURL, target: '_blank', tfsJob.buildName)
        }
    }
}
