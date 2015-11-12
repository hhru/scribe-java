package com.github.scribejava.core.model;

public abstract class SubScribeConfig {

    private static ForceTypeOfHttpRequest forceTypeOfHttpRequests = ForceTypeOfHttpRequest.NONE;

    public static ForceTypeOfHttpRequest getForceTypeOfHttpRequests() {
        return forceTypeOfHttpRequests;
    }

    public static void setForceTypeOfHttpRequests(ForceTypeOfHttpRequest forceTypeOfHttpRequests) {
        SubScribeConfig.forceTypeOfHttpRequests = forceTypeOfHttpRequests;
    }
}
