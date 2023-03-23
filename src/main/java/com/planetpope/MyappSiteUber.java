package com.planetpope;

import rife.engine.Server;

public class MyappSiteUber extends MyappSite {
    public static void main(String[] args) {
        new Server()
            .staticUberJarResourceBase("webapp")
            .start(new MyappSiteUber());
    }
}