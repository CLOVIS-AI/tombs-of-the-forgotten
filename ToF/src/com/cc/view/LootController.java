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
package com.cc.view;

import com.cc.items.Item;
import com.cc.items.ItemContainer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

/**
 *
 * @author schourouq
 */
public class LootController implements Initializable {

    @FXML
    private ListView<Item> listViewLoot;
    
    @FXML
    private ListView<Item> listViewInventory;
    
    @FXML
    private Button Addif;
    
    @FXML
    private Button AddAll;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void setInventories(ItemContainer items1, ItemContainer items2){
        listViewInventory.getItems().addAll(items1.getItems());
        listViewLoot.getItems().addAll(items2.getItems());
        listViewInventory.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listViewLoot.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

}
