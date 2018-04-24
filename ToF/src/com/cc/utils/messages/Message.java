/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.utils.messages;

import com.cc.items.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * A Message.
 * @author Ivan Canet
 */
public class Message {
    
    private final List<MessagePart> parts;
    
    public Message() {
        parts = new ArrayList<>();
    }
    
    /**
     * Automatically adds some objects to this message.
     * <p>This method is an utility method that calls 
     * {@link #addAuto(java.lang.Object) } in a loop.
     * @param objects the objects
     */
    public void addAuto(Object... objects){
        for(Object o : objects)
            addAuto(o);
    }
    
    /**
     * Automatically adds an object to the end of this message.
     * <p>Supported objects are:
     * <ul>
     *  <li>String</li>
     *  <li>Item</li>
     * </ul> Any other object will produce an IllegalArgumentException.
     * @param o the object
     */
    public void addAuto(Object o){
        if(o instanceof Item)
            add((Item) o);
        else if(o instanceof String)
            add((String) o);
        else
            throw new IllegalArgumentException("Cannot recognize " + o + " "
                    + "as any allowed objects.");
    }
    
    public void add(String text){
        parts.add(new MessagePart(text));
    }
    
    public void add(Item item){
        parts.add(new MessagePart(item.getName(), Styling.ITEM));
    }
    
    public void add(String text, Styling styling){
        parts.add(new MessagePart(text, styling));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(MessagePart m : parts)
            sb.append(m).append(", ");
        return sb.delete(sb.length()-2, sb.length()).toString();
    }
    
}
