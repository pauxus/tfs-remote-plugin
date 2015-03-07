Team Foundation Server Control Plugin for Jenkins
============================
Copyright &copy; Stephan Pauxberger

Parts of this code are based on the Team Foundation Server plugin by Erik Ramfelt, Olivier Dagenais, CloudBees, Inc. and others.

Licensed under [MIT Licence].

About
-----
Allows you to control TFS as a build server from inside Jenkins.

Currently, the following features are implemented:

- Custom Builder that starts and watches a corresponding build in tfs
    - aborting the Jenkins build aborts the TFS build and vice versa
    - Status of the Jenkins build matches the outcome of the TFS build.
- Console log and Action link to corresponding job in TFS
    
TODO
----

- Define a TFS trigger that automatically starts a Jenkins Wrapper Job when a corresponding TFS build has been started
- Use Credential plugin (in SDK Lib plugin)


Wiki and Info
-------------
Not yet...

[MIT License]: http://opensource.org/licenses/MIT
