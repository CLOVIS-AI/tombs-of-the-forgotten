/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.utils.messages;

import java.awt.Color;
import java.util.Objects;

/**
 * The styling of a message. A Styling is a mutable type until it's 
 * {@link #lock() lock()} method is called, it then becomes immutable.
 * @author Ivan Canet
 */
public class Styling {
    
    private boolean isLocked = false;
    
    private boolean isBold;
    private boolean isItalic;
    private Color color;
    
    /**
     * Creates a new styling with default values.
     */
    public Styling() {
        isBold = false;
        isItalic = false;
        color = Color.BLACK;
    }
    
    private void tryUnlocking() {
        if(isLocked)
            throw new IllegalStateException("This styling is locked"
                    + "using its #lock() method: " + this);
    }

    /**
     * Is this styling bold?
     * @return {@code true} if it is bold.
     */
    public boolean isBold() {
        return isBold;
    }

    /**
     * Makes this styling bold.
     * @return This styling itself, to allow method-chaining.
     */
    public Styling setBold() {
        tryUnlocking();
        
        isBold = true;
        return this;
    }

    /**
     * Is this styling italic?
     * @return {@code true} if it is italic.
     */
    public boolean isItalic() {
        return isItalic;
    }

    /**
     * Makes this styling italic.
     * @return This styling itself, to allow method-chaining.
     */
    public Styling setItalic() {
        tryUnlocking();
        
        isItalic = true;
        return this;
    }

    /**
     * The color used by this styling.
     * @return The color used by this styling.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color used by this Styling.
     * @param color the color you want to use
     * @return This styling itself, to allow method-chaining.
     */
    public Styling setColor(Color color) {
        tryUnlocking();
        
        this.color = color;
        return this;
    }
    
    /**
     * Is this styling locked?
     * <p>When a styling is locked, it cannot be modified anymore.
     * @return {@code true} if it is locked.
     */
    public boolean isLocked() {
        return isLocked;
    }
    
    /**
     * Locks this styling.
     * <p>When a styling is locked, it cannot be modified anymore.
     * @return This styling itself, to allow method-chaining.
     */
    public Styling lock() {
        isLocked = true;
        return this;
    }
    
    @Override
    public String toString() {
        return "styling("
                + (isBold ? "bold," : "")
                + (isItalic ? "italic," : "")
                + (isLocked ? "locked," : "")
                + "color:" + color + ")";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (this.isLocked ? 1 : 0);
        hash = 17 * hash + (this.isBold ? 1 : 0);
        hash = 17 * hash + (this.isItalic ? 1 : 0);
        hash = 17 * hash + Objects.hashCode(this.color);
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
        final Styling other = (Styling) obj;
        if (this.isLocked != other.isLocked) {
            return false;
        }
        if (this.isBold != other.isBold) {
            return false;
        }
        if (this.isItalic != other.isItalic) {
            return false;
        }
        if (!Objects.equals(this.color, other.color)) {
            return false;
        }
        return true;
    }
    
    // ************************************************ C U S T O M  V A L U E S
    
    /** The default styling of the application's text.
     *  This styling is locked, you can not modify it. */
    public static final Styling DEFAULT = new Styling()
            .lock();
    
    /** The styling used by the application to display Items.
     *  This styling is locked, you can not modify it. */
    public static final Styling ITEM = new Styling()
            .setBold()
            .setColor(Color.YELLOW)
            .lock();
    
    /** The styling used by the application to display damage taken, etc.
     *  This styling is locked, you can not modify it. */
    public static final Styling DAMAGE = new Styling()
            .setBold()
            .setColor(Color.RED)
            .lock();
    
}
