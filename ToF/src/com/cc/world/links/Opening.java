/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world.links;

import com.cc.players.Entity;
import com.cc.world.Room;

/**
 * An opening is a Link that is always open.
 * @author Ivan Canet
 */
public final class Opening extends Link {

    public Opening(Room r1, Room r2) {
        super(r1, r2, true);
    }

    @Override
    public boolean canOpen(Entity e) {
        return false;
    }

    @Override
    public boolean canClose(Entity e) {
        return false;
    }

    @Override
    public boolean open(Entity e) {
        return true;
    }

    @Override
    public boolean close(Entity e) {
        return true;
    }
    
}
