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
import hudson.plugins.emailext.ExtendedEmailPublisher;
import hudson.plugins.emailext.plugins.EmailTrigger;
import org.acegisecurity.context.SecurityContextHolder;
import hudson.tasks.Mailer;
import hudson.tasks.Publisher;
import hudson.util.DescribableList;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
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
            return getExtMailerRecipients("Success");
        }
    }
    
    public boolean hasSuccessSubscription()
    {
        String successRecipients = getExtMailerRecipients("Success");
        return StringUtils.contains(successRecipients, getUserId());
    }
    public boolean hasFailureSubscription()
    {
        String failureRecipients = getExtMailerRecipients("Failure");
        return StringUtils.contains(failureRecipients, getUserId());
    }

     
    private String getExtMailerRecipients(String triggerName) {
            DescribableList<Publisher, Descriptor<Publisher>> publishers = project.getPublishersList();
            Descriptor<Publisher> descriptor = Hudson.getInstance().getDescriptor(ExtendedEmailPublisher.class);
            ExtendedEmailPublisher emailPublisher = (ExtendedEmailPublisher) publishers.get(descriptor);
            List <EmailTrigger> triggers = emailPublisher.getConfiguredTriggers();
            String recipientList = "";
            for ( EmailTrigger trigger : triggers )
            {
                if (trigger.getDescriptor().getDisplayName().equals(triggerName))
                {
                   recipientList = trigger.getEmail().getRecipientList();
                   if (trigger.getEmail().getSendToRecipientList())
                    {
                        recipientList+=emailPublisher.recipientList;
                    }
                }
                
                    
            }
           
        return recipientList;
    }
    
    
    private Mailer getMailer() {
         DescribableList<Publisher, Descriptor<Publisher>> publishers = project.getPublishersList();
            Descriptor<Publisher> descriptor = Hudson.getInstance().getDescriptor(Mailer.class);
            Publisher emailPublisher = publishers.get(descriptor);
            return (Mailer) emailPublisher;
    }

    public boolean doSetRecipients(StaplerRequest req, StaplerResponse rsp) throws IOException,ServletException {
        Mailer mailer = getMailer();
        JSONObject form = req.getSubmittedForm();
        return true;
//        if (!StringUtils.equals(value, mailer.recipients)) {
//            mailer.recipients = value;
//            return true;
//        } else {
//            return false;
//        }
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
    
    //public void doSaveSubscriptions {
    
//}
    
}
