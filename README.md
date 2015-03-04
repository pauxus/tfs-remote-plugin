Team Foundation Server Control Plugin for Jenkins
============================
Copyright &copy; Stephan Pauxberger

Parts of this code are base on the Team Foundation Server plugin by Erik Ramfelt, Olivier Dagenais, CloudBees, Inc. and others.

Licensed under [MIT Licence].

This plugin contains the Microsoft TFS 2013 SDK for Java [TFS-SDK]
 
About
-----
Allows you to control TFS as a build server from inside Jenkins.

Currently, the following features are implemented:

- Custom Builder that starts and watches a corresponding build in tfs
    - aborting the Jenkins build aborts the TFS build and vice versa
    - Status of the Jenkins build matches the outcome of the TFS build.
    
TODO
----

- Define a TFS trigger that automatically starts a Jenkins Wrapper Job when a corresponding TFS build has been started
- Use Credential plugin
- Split out TFS-SDK into a separate plugin to stay compatible with TFS-Plugin for Jenkins.

Wiki and Info
-------------
Not yet...

[MIT License]: http://opensource.org/licenses/MIT
[TFS-SDK]: http://www.microsoft.com/en-us/download/details.aspx?id=40785
