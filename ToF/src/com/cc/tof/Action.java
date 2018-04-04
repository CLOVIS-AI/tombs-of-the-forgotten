/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.tof;

import java.util.Arrays;
import java.util.Objects;

/**
 * An action.
 * @author schourouq
 */
public class Action {
    
    final private String command;
    final private String[] parameters;

    /**
     * Constructor.
     * @param command command for an action
     * @param parameters parameters of a command
     */
    public Action(String command, String[] parameters) {
        this.command = command;
        this.parameters = parameters;
    }

    /**
     * Get the command.
     * @return The command.
     */
    public String getCommand() {
        return command;
    }
    
    /**
     * Get the parameters.
     * @return The parameters
     */
    public String[] getParameters() {
        return parameters;
    }

    /**
     * A hash code value for this object.
     * @return A hash code value for this object 
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.command);
        hash = 73 * hash + Arrays.deepHashCode(this.parameters);
        return hash;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj the reference object with which to compare
     * @return {@code true} if this object is the same as the obj argument,
     * false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Action other = (Action) obj;
        if (!Objects.equals(this.command, other.command)) {
            return false;
        }
        if (!Arrays.deepEquals(this.parameters, other.parameters)) {
            return false;
        }
        return true;
    }
    
    /**
     * A string representation of the object.
     * @return A string representation of the object
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(command);
        for(String s : parameters){
            sb.append(" ");
            sb.append(s);
        }
        return sb.toString();
    }
    
}
