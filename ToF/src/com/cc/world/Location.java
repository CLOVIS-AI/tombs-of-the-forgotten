/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world;

/**
 * A location in the map. Immutable data class.
 * <p>In effect, this class is very similar to a 3-Vector in a mathematical sense.
 * <p>The different axis are described as follow:
 * <ul>
 *  <li><b>X:</b> South-North, positive towards South.</li>
 *  <li><b>Y:</b> East-West, positive towards East.</li>
 *  <li><b>Z:</b> Up-Down, positive towards Up.</li>
 * </ul>
 * @author Ivan Canet
 */
public class Location implements Comparable<Location> {
    
    private final int X, Y, Z;

    /**
     * Creates a location.
     * @param X The X coordinate
     * @param Y The Y coordinate
     * @param Z The Z coordinate
     * @see Location Information about the axis
     */
    public Location(int X, int Y, int Z) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }
    
    /**
     * Creates the default location, of coordinates (0, 0, 0).
     * @see Location Information about the axis
     */
    public Location(){
        this(0, 0, 0);
    }
    
    /**
     * Adds this location with an other one, and returns the result. Neither
     * location is modified.
     * @param l the other location
     * @return The result of a sum between this location and an other one.
     */
    public Location add(Location l){
        return new Location(X + l.X, 
                            Y + l.Y, 
                            Z + l.Z);
    }
    
    /**
     * Calculates where you will be after moving from this Location in a specific
     * Direction.
     * <p>This is the same as {@code d.getArrival(this)}.
     * @param d the direction in which you are going
     * @return Where you will be after moving in the desired direction.
     */
    public Location add(Direction d){
        return add(d.getDirection());
    }

    /**
     * Get the value of the X axis.
     * <p>This value is positive for southern points and negative for northern
     * points.
     * @return The value of the X axis.
     * @see Location Information about the axis
     */
    public int getX() {
        return X;
    }

    /**
     * Get the value of the Y axis.
     * <p>This value is positive for eastern points and negative for western
     * points.
     * @return The value of the Y axis.
     * @see Location Information about the axis
     */
    public int getY() {
        return Y;
    }

    /**
     * Get the value of the Z axis.
     * <p>This value is positive for upper points and negative for lower points.
     * @return The value of the Z axis.
     * @see Location Information about the axis
     */
    public int getZ() {
        return Z;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.X;
        hash = 67 * hash + this.Y;
        hash = 67 * hash + this.Z;
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
        final Location other = (Location) obj;
        if (this.X != other.X) {
            return false;
        }
        if (this.Y != other.Y) {
            return false;
        }
        if (this.Z != other.Z) {
            return false;
        }
        return true;
    }
    
    /**
     * Adds two locations and returns the result.
     * @param a The first location
     * @param b The second location
     * @return The result of a+b.
     * @see #add(com.cc.world.Location) More details
     */
    public static Location add(Location a, Location b){
        return a.add(b);
    }

    @Override
    public int compareTo(Location l) {
        return Z != l.Z ? Z - l.Z :
               X != l.X ? X - l.X :
                          Y - l.Y ;
    }
    
    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", X, Y, Z);
    }
    
}
