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
    private Label Apparel;
    @FXML
    private Label Weapons;
    @FXML
    private Label Stats;
    @FXML
    private Label Eatable;
    @FXML
    private Label Scrolls;
    @FXML
    private Label Other;

    private boolean isMenu = false;
    private Timeline move = new Timeline();
    private AnchorPane p;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Open.setOnMouseEntered(e -> {
            isMenu = true;
            slideRight(LeftMenu);
        });
        LeftMenu.setOnMouseExited(e -> {
            isMenu = false;
            slideLeft(LeftMenu);
        });
        Stats.setOnMousePressed(e -> {
            isMenu = true;
            slideLeft(LeftMenu);
            slideRight(BlockStats);
        });
        BlockStats.setOnMouseExited(ev -> {
            if (move.getStatus() == Timeline.Status.STOPPED) {
                isMenu = false;
                slideLeft(BlockStats);
            }
        });
        Weapons.setOnMousePressed(e -> {
            isMenu = true;
            slideLeft(LeftMenu);
            slideRight(BlockWeapon);
        });
        BlockWeapon.setOnMouseExited(ev -> {
            if (move.getStatus() == Timeline.Status.STOPPED) {
                isMenu = false;
                slideLeft(BlockWeapon);
            }
        });
        Apparel.setOnMousePressed(e -> {
            isMenu = true;
            slideLeft(LeftMenu);
            slideRight(BlockApparel);
        });
        BlockApparel.setOnMouseExited(ev -> {
            if (move.getStatus() == Timeline.Status.STOPPED) {
                isMenu = true;
                slideLeft(BlockApparel);
            }
        });
        Eatable.setOnMousePressed(e -> {
            isMenu = true;
            slideLeft(LeftMenu);
            slideRight(BlockEatable);
        });
        BlockEatable.setOnMouseExited(ev -> {
            if (move.getStatus() == Timeline.Status.STOPPED) {
                isMenu = false;
                slideLeft(BlockEatable);
            }
        });
        Scrolls.setOnMousePressed(e -> {
            isMenu = true;
            slideLeft(LeftMenu);
            slideRight(BlockScrolls);
        });
        BlockScrolls.setOnMouseExited(ev -> {
            if (move.getStatus() == Timeline.Status.STOPPED) {
                isMenu = false;
                slideLeft(BlockScrolls);
            }
        });
        Other.setOnMousePressed(e -> {
            isMenu = true;
            slideLeft(LeftMenu);
            slideRight(BlockOther);
        });
        BlockOther.setOnMouseExited(ev -> {
            if (move.getStatus() == Timeline.Status.STOPPED) {
                isMenu = false;
                slideLeft(BlockOther);
            }
        });

    }
    
    private void slideRight(AnchorPane menu) {
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
