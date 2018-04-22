/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world;

/**
 * A direction.
 * @author Ivan Canet
 */
public enum Direction {
    
    /** The North. */
    NORTH(-1, 0, 0),
    
    /** The South. */
    SOUTH(1, 0, 0),
    
    /** The East. */
    EAST(0, 1, 0),
    
    /** The West. */
    WEST(0, -1, 0),
    
    /** The upper floor. */
    UP(0, 0, 1),
    
    /** The lower floor. */
    DOWN(0, 0, -1);
    
    private final Location location;
    
    Direction(int x, int y, int z){
        location = new Location(x, y, z);
    }

    /**
     * This direction, as a Location; that is, the distance that this direction
     * corresponds to.
     * @return The direction of this object.
     */
    public Location getDirection() {
        return location;
    }
    
    /**
     * The point where you will end up if you travel in this direction from a
     * {@code departure} point.
     * <p>This is the same as {@code departure.add(this.getDirection())}.
     * @param departure Where you depart from
     * @return Where you will end up.
     */
    public Location getArrival(Location departure) {
        return departure.add(location);
    }
    
    /**
     * The opposite Direction to this one.
     * <p>For example, the opposite of NORTH is SOUTH.
     * @return The opposite Direction.
     */
    public Direction getOpposite() {
        return fromCoordinates(location.getOpposite());
    }
    
    /**
     * Gets the direction that corresponds to this location.
     * @param l a location
     * @return The direction that corresponds to a location.
     */
    public static Direction fromCoordinates(Location l) {
        for(Direction d : values())
            if(l.equals(d.location))
                return d;
        
        throw new IllegalArgumentException("No direction corresponds to the "
                + "provided location: " + l);
    }
    
    /**
     * Gets the direction that corresponds to this location.
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return The direction that corresponds to a location.
     */
    public static Direction fromCoordinates(int x, int y, int z){
        return fromCoordinates(new Location(x, y, z));
    }
}
