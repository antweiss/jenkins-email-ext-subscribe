/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antweiss.jenkins.plugins.emailextsubscriber;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.tasks.BuildWrapperDescriptor;
 
/**
 *
 * @author aweiss
 */
@Extension
public class SubscribeBuildWrapperDescriptor extends BuildWrapperDescriptor {
 
    public SubscribeBuildWrapperDescriptor() {
        super(SubscribeBuildWrapper.class);
        // Load the persisted properties from file.
        load();
    }
 
    @Override
    public boolean isApplicable(AbstractProject<?, ?> item) {
        return true;
    }
 
    @Override
    public String getDisplayName() {
        return "Email-EXT Subscribe";
    }
 

}
