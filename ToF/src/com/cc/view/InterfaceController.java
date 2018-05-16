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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 *
 * @author schourouq
 */
public class InterfaceController implements Initializable {

    @FXML
    private AnchorPane LeftMenu;
    @FXML
    private AnchorPane Open;
    @FXML
    private AnchorPane BlockStats;
    @FXML
    private AnchorPane BlockWeapon;
    @FXML
    private AnchorPane BlockApparel;
    @FXML
    private AnchorPane BlockEatable;
    @FXML
    private AnchorPane BlockScrolls;
    @FXML
    private AnchorPane BlockOther;
    @FXML
    private Label Stats;
    @FXML
    private Label Weapons;
    @FXML
    private Label Apparel;
    @FXML
    private Label Eatable;
    @FXML
    private Label Scrolls;
    @FXML
    private Label Other;

    private boolean isMenu = false;
    Timeline move = new Timeline();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        setMenu(BlockStats, Stats);
        setMenu(BlockWeapon, Weapons);
        setMenu(BlockApparel, Apparel);
        setMenu(BlockEatable, Eatable);
        setMenu(BlockScrolls, Scrolls);
        setMenu(BlockOther, Other);
    }

    private void setMenu(AnchorPane block, Label label){
        Open.setOnMouseEntered(e -> {
            isMenu = true;
            slideRight(LeftMenu);
        });
        LeftMenu.setOnMouseExited(e -> {
            isMenu = false;
            slideLeft(LeftMenu);
        });
        label.setOnMousePressed(e -> {
            isMenu = true;
            slideLeft(LeftMenu);
            slideRight(block);
        });
        block.setOnMouseExited(e -> {
            if (move.getStatus() == Timeline.Status.STOPPED) {
                isMenu = false;
                slideLeft(block);
            }
        });
        
    }
    

    private void slideRight(Node menu) {
        move = new Timeline(new KeyFrame(Duration.seconds(0.5), new KeyValue(menu.layoutXProperty(), 255)));
        move.play();
        if (isMenu) {
            Open.setMouseTransparent(true);
            Open.setOpacity(0);
        }
    }

    private void slideLeft(AnchorPane menu) {
        move = new Timeline(new KeyFrame(Duration.seconds(0.5), new KeyValue(menu.layoutXProperty(), -255)));
        move.play();
        if (!isMenu) {
            Open.setMouseTransparent(false);
            Open.setOpacity(1);
        }
    }
}
