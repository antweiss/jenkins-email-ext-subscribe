/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antweiss.jenkins.plugins.emailextsubscriber;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Action;

/**
 *
 * @author aweiss
*/
public class SubscribeAction implements Action {

    private final AbstractProject<?, ?> project;
 
  public SubscribeAction(final AbstractProject project) {
    this.project = project;
    
  }
    
  /**
* Method necessary to get the side-panel included in the Jelly file
* @return this {@link AbstractProject}
*/
  public AbstractProject<?, ?> getProject() {
    return this.project;
  }
   
  public String getIconFileName() {
        return "/plugin/email-ext/images/template-debugger.png";
    }

    public String getDisplayName() {
        return "Subscribe";
    }

    public String getUrlName() {
        return "Subscribe";
    }
    
    
}
