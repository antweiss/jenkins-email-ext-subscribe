/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antweiss.jenkins.plugins.emailextsubscriber;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.tasks.BuildWrapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author aweiss
 */
public class SubscribeBuildWrapper extends BuildWrapper{
    
    private boolean isSubscriptionAllowed;
    @DataBoundConstructor
    public SubscribeBuildWrapper()
    {
        isSubscriptionAllowed = true;
    }
    
    @Override
    public Collection<? extends Action> getProjectActions(AbstractProject job) {
        final SubscribeAction subscribeAction = new SubscribeAction(job);
        return Arrays.asList(subscribeAction);
    }
    
    @Override
    public Environment setUp(AbstractBuild build, Launcher launcher, BuildListener listener)
            throws IOException, InterruptedException {
        // Return an empty environment as we are not changing anything.
        return new Environment() {};
    }
    
}
