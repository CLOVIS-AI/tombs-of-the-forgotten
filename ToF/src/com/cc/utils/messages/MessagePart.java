/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.utils.messages;

import java.util.Objects;

/**
 * A part of a Message.
 * @author Ivan Canet
 */
public class MessagePart {
    
    private final String text;
    private final Styling styling;
    
    /**
     * Creates a Message part.
     * @param text the text of that part.
     * @param styling the styling of that part.
     */
    public MessagePart(String text, Styling styling) {
        this.text = text;
        this.styling = styling;
    }
    
    /**
     * Creates a Message part using {@link Styling#DEFAULT the default styling}.
     * @param text the text of that part.
     */
    public MessagePart(String text) {
        this(text, Styling.DEFAULT);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.text);
        hash = 83 * hash + Objects.hashCode(this.styling);
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
        final MessagePart other = (MessagePart) obj;
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        if (!Objects.equals(this.styling, other.styling)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[styling:("+styling+")"+text+"]";
    }

    /**
     * The text in this MessagePart.
     * @return the text in this MessagePart.
     */
    public String getText() {
        return text;
    }

    /**
     * The styling of this MessagePart.
     * @return The styling of this MessagePart.
     */
    public Styling getStyling() {
        return styling;
    }
    
}
