/*
 * The MIT License
 *
 * Copyright 2018 Canet Ivan & Chourouq Sarah.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cc.items;

import com.cc.players.Entity;
import com.cc.utils.Save;
import com.cc.utils.messages.Message;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;

/**
 * An action is a part of something done by the user.
 * @author Ivan Canet
 */
public interface Action extends Save<JsonObject> {
    
    public void execute(Entity entity);
    
    public Message getEffects();
    
    /**
     * Creates an Action object from the JSON data.
     * @param json the saved data
     * @return An Action object.
     */
    public static Action load(JsonObject json) {
        switch(json.getString("type", null)){
            case "entity": return new EntityAction(json);
            default:
                throw new IllegalArgumentException("The provided JSON data cannot"
                        + "be recognized as an Action (invalid or missing type):\n"
                        + json.toString(WriterConfig.PRETTY_PRINT));
        }
    }
    
}
