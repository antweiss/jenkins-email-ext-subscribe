/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antweiss.jenkins.plugins.emailextsubscriber;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.model.User;
import org.acegisecurity.context.SecurityContextHolder;
import hudson.tasks.Mailer;
import hudson.tasks.Publisher;
import hudson.util.DescribableList;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;

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
        return "/plugin/email-ext-subscriber/stock_mail_send.png";
    }

    public String getDisplayName() {
        return "Subscribe";
    }

    public String getUrlName() {
        return "Subscribe";
    }
    
    public String getUserId() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userId;
    }
    
    public String getRecipients() {
        Mailer mailer = getMailer();
        if (mailer != null) {
            return mailer.recipients;
        } else {
            return null;
        }
    }

    private Mailer getMailer() {
        DescribableList<Publisher, Descriptor<Publisher>> publishers = project.getPublishersList();
        Descriptor<Publisher> descriptor = Hudson.getInstance().getDescriptor(Mailer.class);
        Publisher emailPublisher = publishers.get(descriptor);
        return (Mailer) emailPublisher;
    }

    public boolean setRecipients(String value) {
        Mailer mailer = getMailer();
        if (!StringUtils.equals(value, mailer.recipients)) {
            mailer.recipients = value;
            return true;
        } else {
            return false;
        }
    }

    public boolean addMailer() throws IOException {
        Mailer mailer = getMailer();
        if (mailer == null) {
            DescribableList<Publisher, Descriptor<Publisher>> publishers = project.getPublishersList();
            publishers.add(new Mailer());
            return true;
        } else {
            return false;
        }
    }
    
    
}
