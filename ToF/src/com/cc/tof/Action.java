/* MIT License
 *
 * Copyright (c) 2018 Canet Ivan & Chourouq Sarah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
