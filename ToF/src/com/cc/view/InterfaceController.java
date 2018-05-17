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

import com.cc.tof.ToF;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author schourouq
 */
public class InterfaceController implements Initializable {

    @FXML
    private AnchorPane LeftMenu, Open, BlockStats, BlockWeapon, BlockApparel,
            BlockEatable, BlockScrolls, BlockOther, BlockAll;

    @FXML
    private Label Apparel, Weapons, Stats, Eatable, Scrolls, Other, All;

    @FXML
    private MenuItem ViewWeapon, ViewApparel, ViewEatable, ViewScroll, ViewOther,
            ViewAll;

    private boolean isMenu = false;
    private Timeline move = new Timeline();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Open.setOnMouseEntered(e -> {
            isMenu = true;
            slideRight(LeftMenu);
            slideLeft(BlockStats);
            slideLeft(BlockWeapon);
            slideLeft(BlockApparel);
            slideLeft(BlockEatable);
            slideLeft(BlockScrolls);
            slideLeft(BlockOther);
            slideLeft(BlockAll);
        });
        LeftMenu.setOnMouseExited(e -> {
            isMenu = false;
            slideLeft(LeftMenu);
        });

        showMenu(BlockStats, Stats);
        showMenu(BlockWeapon, Weapons);
        showMenu(BlockApparel, Apparel);
        showMenu(BlockEatable, Eatable);
        showMenu(BlockScrolls, Scrolls);
        showMenu(BlockOther, Other);
        showMenu(BlockAll, All);

        viewItem(ViewWeapon);
        viewItem(ViewApparel);
        viewItem(ViewEatable);
        viewItem(ViewScroll);
        viewItem(ViewOther);
        viewItem(ViewAll);

    }

    /**
     * Displays a menu listed among a main menu.
     *
     * @param block A menu
     * @param label A button
     */
    private void showMenu(AnchorPane block, Label label) {
        label.setOnMousePressed(e -> {
            isMenu = true;
            slideLeft(LeftMenu);
            slideRight(block);
        });
    }

    /**
     * Animation for displaying a menu on the game screen.
     *
     * @param menu A menu
     */
    private void slideRight(AnchorPane menu) {
        move = new Timeline(new KeyFrame(Duration.seconds(0.5), new KeyValue(menu.layoutXProperty(), 255)));
        move.play();
        if (isMenu) {
            Open.setMouseTransparent(true);
            Open.setOpacity(0);
        }
    }

    /**
     * Animation for hiding a menu out of the game screen.
     *
     * @param menu A menu
     */
    private void slideLeft(AnchorPane menu) {
        move = new Timeline(new KeyFrame(Duration.seconds(0.5), new KeyValue(menu.layoutXProperty(), -255)));
        move.play();
        if (!isMenu) {
            Open.setMouseTransparent(false);
            Open.setOpacity(1);
        }
    }

    public void viewItem(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ToF.getResource("Item.fxml"));
            Parent menu = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(menu));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void viewItem (MenuItem i) {
        i.setOnAction(e -> {
            viewItem(e);
        });
    }
}
