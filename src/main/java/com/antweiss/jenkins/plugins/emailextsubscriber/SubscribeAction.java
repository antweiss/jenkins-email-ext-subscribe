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
     *
     * @return this {@link AbstractProject}
     */
    public AbstractProject<?, ?> getProject() {
        return this.project;
    }

    public String getIconFileName() {
        if ("anonymous".equals(getUserId()))
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
            ExtendedEmailPublisher emailPublisher = getPublisher();
            List <EmailTrigger> triggers = emailPublisher.getConfiguredTriggers();
            String recipientList = "";
            for ( EmailTrigger trigger : triggers )
            {
                if (trigger.getDescriptor().getDisplayName().equals(triggerName))
                {
                   recipientList = trigger.getEmail().getRecipientList();
                   if ((trigger.getEmail().getSendToRecipientList()) && !emailPublisher.recipientList.isEmpty())
                    {
                        if (!recipientList.isEmpty())
                        {
                            recipientList+=","+emailPublisher.recipientList;
                        }
                        else
                        {
                            recipientList+=emailPublisher.recipientList;
                        }
                    }
                }
                
                    
            }
           
        return recipientList;
    }
    
    
    public void setRecipients(String triggerName, String recipientList) {

        ExtendedEmailPublisher emailPublisher = getPublisher();
        List<EmailTrigger> triggers = emailPublisher.getConfiguredTriggers();
        for (EmailTrigger trigger : triggers) {
            if (trigger.getDescriptor().getDisplayName().equals(triggerName)) {
                if (trigger.getEmail().getSendToRecipientList())
                {
                   emailPublisher.recipientList =  recipientList;
                }
                else
                {
                    trigger.getEmail().setRecipientList(recipientList);
                }
            }
        }

    }
    private ExtendedEmailPublisher getPublisher()
    {
        DescribableList<Publisher, Descriptor<Publisher>> publishers = project.getPublishersList();
        Descriptor<Publisher> descriptor = Hudson.getInstance().getDescriptor(ExtendedEmailPublisher.class);
        return (ExtendedEmailPublisher) publishers.get(descriptor);
        
    }

       
     public void doSaveSubscription(StaplerRequest req, StaplerResponse rsp) throws IOException,ServletException {
        JSONObject form = req.getSubmittedForm();
        String[] triggers = {"Success","Failure"};
        boolean successSubscription = form.getBoolean("Success");
        boolean failureSubscription = form.getBoolean("Failure");
        String userId = getUserId();
        for (String triggerName : triggers)
        {
            String recipientList = getExtMailerRecipients(triggerName);
            if ( form.getBoolean(triggerName) )
            {
  
                if (!StringUtils.contains(recipientList, userId)) 
                {
                    recipientList=userId+","+recipientList;
                }
            }
            else
            {
                    recipientList = recipientList.replaceAll(userId, "");
            }
            recipientList = recipientList.replaceAll("^,|,$", "");
            setRecipients(triggerName,recipientList);
        }
        project.save();
       
        rsp.sendRedirect(project.getAbsoluteUrl());
     }
     
     
     private Mailer getMailer() {
         DescribableList<Publisher, Descriptor<Publisher>> publishers = project.getPublishersList();
            Descriptor<Publisher> descriptor = Hudson.getInstance().getDescriptor(Mailer.class);
            Publisher emailPublisher = publishers.get(descriptor);
            return (Mailer) emailPublisher;
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
