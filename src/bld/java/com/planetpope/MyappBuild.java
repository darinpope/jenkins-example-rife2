package com.planetpope;

import rife.bld.WebProject;

import java.util.List;

import static rife.bld.dependencies.Repository.*;
import static rife.bld.dependencies.Scope.*;
import static rife.bld.operations.TemplateType.*;

public class MyappBuild extends WebProject {
    public MyappBuild() {
        pkg = "com.planetpope";
        name = "Myapp";
        mainClass = "com.planetpope.MyappSite";
        uberJarMainClass = "com.planetpope.MyappSiteUber";
        version = version(0,1,0);

        precompiledTemplateTypes = List.of(HTML);

        repositories = List.of(MAVEN_CENTRAL, SONATYPE_SNAPSHOTS);
        scope(compile)
            .include(dependency("com.uwyn.rife2", "rife2", version(1,5,4)));
        scope(test)
            .include(dependency("org.jsoup", "jsoup", version(1,15,4)))
            .include(dependency("org.junit.jupiter", "junit-jupiter", version(5,9,2)))
            .include(dependency("org.junit.platform", "junit-platform-console-standalone", version(1,9,2)));
        scope(standalone)
            .include(dependency("org.eclipse.jetty", "jetty-server", version(11,0,14)))
            .include(dependency("org.eclipse.jetty", "jetty-servlet", version(11,0,14)))
            .include(dependency("org.slf4j", "slf4j-simple", version(2,0,7)));
    }

    public static void main(String[] args) {
        new MyappBuild().start(args);
    }
}