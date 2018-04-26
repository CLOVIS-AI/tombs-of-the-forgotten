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
