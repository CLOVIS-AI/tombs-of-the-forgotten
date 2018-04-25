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
    
    /**
     * Creates a new message.
     */
    public Message() {
        parts = new ArrayList<>();
    }
    
    /**
     * Automatically adds some objects to this message.
     * <p>This method is an utility method that calls 
     * {@link #addAuto(java.lang.Object) } in a loop.
     * @param objects the objects
     * @return this Message itself, to allow method-chaining.
     */
    public Message addAuto(Object... objects){
        for(Object o : objects)
            addAuto(o);
        
        return this;
    }
    
    /**
     * Automatically adds an object to the end of this message.
     * <p>Supported objects are:
     * <ul>
     *  <li>String</li>
     *  <li>Item</li>
     * </ul> Any other object will produce an IllegalArgumentException.
     * @param o the object
     * @return this Message itself, to allow method-chaining.
     */
    public Message addAuto(Object o){
        if(o instanceof Item)
            add((Item) o);
        else if(o instanceof String)
            add((String) o);
        else
            throw new IllegalArgumentException("Cannot recognize " + o + " "
                    + "as any allowed objects.");
        
        return this;
    }
    
    /**
     * Adds a MessagePart to this Message.
     * @param m the message part
     * @return this Message itself, to allow method-chaining.
     */
    public Message add(MessagePart m) {
        parts.add(m);
        
        return this;
    }
    
    /**
     * Adds a text to this Message (using the styling {@link Styling#DEFAULT}).
     * @param text the text
     * @return this Message itself, to allow method-chaining.
     */
    public Message add(String text){
        add(text, Styling.DEFAULT);
        
        return this;
    }
    
    /**
     * Adds the mention of an Item to this message (using the styling
     * {@link Styling#ITEM}).
     * @param item the item
     * @return this Message itself, to allow method-chaining.
     */
    public Message add(Item item){
        add(item.getName(), Styling.ITEM);
        
        return this;
    }
    
    /**
     * Adds the mention of an Item to this message (using the styling
     * {@link Styling#ITEM}).
     * @param item the item
     * @param includeDescription if {@code true}, the item's description is 
     * appended between parenthesis. 
     * @return this Message itself, to allow method-chaining.
     */
    public Message add(Item item, boolean includeDescription){
        add(item);
        
        if(includeDescription)
            add(" (" + item.getDescription() + ")");
        
        return this;
    }
    
    /**
     * Adds a text with a styling.
     * @param text the text
     * @param styling the styling
     * @return this Message itself, to allow method-chaining.
     */
    public Message add(String text, Styling styling){
        add(new MessagePart(text, styling));
        
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(MessagePart m : parts)
            sb.append(m).append(", ");
        return sb.delete(sb.length()-2, sb.length()).toString();
    }
    
}
