/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.antweiss.jenkins.plugins.emailextsubscriber;

import jenkins.model.Jenkins;

/**
 *
 * @author aweiss
 */
public class Subscription {
    public boolean Success;
    public boolean Failure;
    public String Name;
    public String editUrl;
    public Subscription(boolean Success,boolean Failure,String Name, String Url)
    {
        this.Success = Success;
        this.Failure = Failure;
        this.Name = Name;
        this.editUrl = Jenkins.getInstance().getRootUrl()+Url+"Subscribe";
    }
}
