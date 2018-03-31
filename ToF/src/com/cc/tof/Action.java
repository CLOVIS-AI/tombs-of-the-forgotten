/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.tof;

/**
 *
 * @author schourouq
 */
public class Action {
    
    final private String commands;
    final private String[] parameters;

    public Action(String commands, String[] parameters) {
        this.commands = commands;
        this.parameters = parameters;
    }

    public String getCommands() {
        return commands;
    }

    public String[] getParameters() {
        return parameters;
    }
    
}
