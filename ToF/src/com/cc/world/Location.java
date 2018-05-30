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
package com.cc.world;

import com.cc.utils.Save;
import com.eclipsesource.json.JsonObject;

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
public class Location implements Comparable<Location>, Save<JsonObject> {
    
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
     * Loads the location from JSON
     * @param json the saved data
     */
    public Location(JsonObject json){
        this(json.getInt("x", 0),
             json.getInt("y", 0),
             json.getInt("z", 0));
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
     * Removes this location to an other one, and returns the result. Neither
     * location is modified.
     * @param l the other location
     * @return The result of a difference between this location and an other one.
     */
    public Location remove(Location l){
        return new Location(X - l.X, 
                            Y - l.Y, 
                            Z - l.Z);
    }
    
    /**
     * The absolute location of this location; that is, the location with the
     * same coordinates but all positive.
     * @return The absolute location of this location.
     */
    public Location abs(){
        return new Location(Math.abs(X), Math.abs(Y), Math.abs(Z));
    }
    
    /**
     * The distance between this location and an other one.
     * @param l the other location
     * @return The distance between two locations.
     */
    public int dist(Location l){
        Location dist = remove(l).abs();
        return Math.max(Math.max(dist.X, dist.Y), dist.Z);
    }
    
    /**
     * Is this location next to an other one?
     * @param l the other location
     * @return {@code true} if the two locations touch each other (their 
     * distance is 1 according to {@link #dist(com.cc.world.Location) }).
     */
    public boolean isNextTo(Location l){
        return dist(l) <= 1;
    }
    
    /**
     * The opposite location.
     * <p>For every location (x, y, z), it's opposite is (-x, -y, -z).
     * @return The opposite location.
     */
    public Location getOpposite(){
        return new Location(-X, -Y, -Z);
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

    @Override
    public JsonObject save() {
        return new JsonObject()
                .add("x", X)
                .add("y", Y)
                .add("z", Z);
    }
    
}
