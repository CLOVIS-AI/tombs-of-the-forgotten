/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.tof;

import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.commands);
        hash = 73 * hash + Arrays.deepHashCode(this.parameters);
        return hash;
    }

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
        if (!Objects.equals(this.commands, other.commands)) {
            return false;
        }
        if (!Arrays.deepEquals(this.parameters, other.parameters)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(commands);
        for(String s : parameters){
            sb.append(" ");
            sb.append(s);
        }
        return sb.toString();
    }
    
}
