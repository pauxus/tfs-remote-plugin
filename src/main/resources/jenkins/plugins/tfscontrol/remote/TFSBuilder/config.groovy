package jenkins.plugins.tfscontrol.remote.TFSBuilder

def f = namespace(lib.FormTagLib);

f.entry(title:_("URL"), field:"url") { // TODO autocomplete
    f.textbox()
}

f.entry(title:_("Team Project"), field:"teamProject") { // TODO autocomplete
    f.textbox()
}

f.entry(title:_("Build Definition"), field:"buildDefinition") {  // TODO autocomplete
    f.textbox()
}
f.entry(title:_("username"), field:"username") {  // TODO autocomplete
    f.textbox()
}
f.entry(title:_("password"), field:"password") {  // TODO autocomplete
    f.password()
}
