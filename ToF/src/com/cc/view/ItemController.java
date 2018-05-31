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
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ivan
 */
public class ItemController implements Initializable {

    @FXML
    private AnchorPane Item;
    @FXML
    private Label ItemName;
    @FXML
    private Label ItemCategory;
    @FXML
    private Label Rarity;
    @FXML
    private Label Weight;
    @FXML
    private Label Durability;
    @FXML
    private Label Description;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    public void setItem(Item item) {
        ItemName.setText(item.getName());
        ItemCategory.setText("");
        Rarity.setText(item.getRarity().toString());
        Weight.setText("" + item.getWeight());
        Durability.setText(item.getDurability() + "/" + item.getMaxDurability());
        Description.setText(item.getEffects().stream().map(e -> e.toStringSimple()).collect(Collectors.joining("\n")));
    }
    
}
