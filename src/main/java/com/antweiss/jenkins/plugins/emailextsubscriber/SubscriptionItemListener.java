/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.antweiss.jenkins.plugins.emailextsubscriber;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.model.Item;
import hudson.model.Project;
import hudson.model.listeners.ItemListener;
import hudson.tasks.BuildWrapper;
import hudson.util.DescribableList;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author aweiss
 */
@Extension
public class SubscriptionItemListener extends ItemListener {
   @Override
    public void onCreated(Item item) {
        if(item instanceof Project) {
            Project project = (Project) item;
            LOGGER.finest("Activating Subscribe for project '" + project.getName() + "'...");
            synchronized(project) {
                    project.getBuildWrappersList().add(new SubscribeBuildWrapper());
                try {
                    project.save();
                } catch (IOException ex) {
                    Logger.getLogger(SubscriptionItemListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            LOGGER.finest("Subscribe is now activated for project '" + project.getName() + "'");
        }
    }

    @Override
    public void onLoaded() {
        for(Project project: Hudson.getInstance().getProjects()) {
            DescribableList<BuildWrapper, Descriptor<BuildWrapper>> wrappers = project.getBuildWrappersList();
            Descriptor<BuildWrapper> descriptor = Hudson.getInstance().getDescriptor(SubscribeBuildWrapper.class);
            BuildWrapper wrapper = (BuildWrapper) wrappers.get(descriptor);
            if(wrapper == null) {
                LOGGER.finest("Activating Subscribe for project '" + project.getName() + "'...");
            synchronized(project) {
                    project.getBuildWrappersList().add(new SubscribeBuildWrapper());
                    try {
                        project.save();
                    } catch (IOException ex) {
                        Logger.getLogger(SubscriptionItemListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
                    LOGGER.finest("Subscribe is now activated for project '"
+ project.getName() + "'");
           }
           else {
                LOGGER.finest("Subscribe is activated for project '" +
project.getName() + "'");
            }
        }
    }

    private static final Logger LOGGER = Logger.getLogger(SubscriptionItemListener.class.getName());  
}
