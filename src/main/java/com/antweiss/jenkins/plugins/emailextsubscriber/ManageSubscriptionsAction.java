/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.antweiss.jenkins.plugins.emailextsubscriber;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.model.RootAction;
import hudson.model.TopLevelItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.acegisecurity.context.SecurityContextHolder;

/**
 *
 * @author aweiss
 */
@Extension
public class ManageSubscriptionsAction implements RootAction {

   public String getIconFileName() {
       if ("anonymous".equals(SecurityContextHolder.getContext().getAuthentication().getName()))
        {
            return null;
        }
         return "/plugin/email-ext-subscriber/stock_mail_send.png";
    }

    public String getDisplayName() {
        return "Manage my mail subscriptions";
    }

    public String getUrlName() {
        return "/manageSubscriptions";
    }
   
    private List<TopLevelItem> getJobs() {
        return Hudson.getInstance().getItems();
    }
    
    public  List<Subscription> getMySubscriptions()
    {
        List<TopLevelItem> jobs = getJobs();
        List<Subscription> subscriptions = new ArrayList<Subscription>();
        for (Iterator<TopLevelItem> it = jobs.iterator(); it.hasNext();) {
           TopLevelItem job = it.next();
           SubscribeAction action = new SubscribeAction((AbstractProject) job);
           if (action.hasFailureSubscription() || action.hasSuccessSubscription())
           {
                 Subscription sub = new Subscription(action.hasSuccessSubscription(),action.hasFailureSubscription(),job.getDisplayName(),job.getUrl());
                 subscriptions.add(sub);
           }
       }
        return subscriptions;
    }
    
}
