/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.antweiss.jenkins.plugins.emailextsubscriber;

import hudson.Extension;
import hudson.model.ProminentProjectAction;
import org.acegisecurity.context.SecurityContextHolder;

/**
 *
 * @author aweiss
 */
@Extension
public class ProjectSubscriptionAction implements ProminentProjectAction {

      public String getIconFileName() {
       if ("anonymous".equals(SecurityContextHolder.getContext().getAuthentication().getName()))
        {
            return null;
        }
         return "/plugin/email-ext-subscriber/stock_mail_send.png";
    }

    public String getDisplayName() {
        return "Subscribe";
    }

    public String getUrlName() {
        return "Subscribe";
    }
    
    
}
