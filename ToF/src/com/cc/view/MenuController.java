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
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author schourouq
 */
public class MenuController implements Initializable {

    @FXML
    private AnchorPane NewArrow;
    @FXML
    private AnchorPane LoadArrow;
    @FXML
    private AnchorPane QuitArrow;
    @FXML
    private Label NewLabel;
    @FXML
    private Label LoadLabel;
    @FXML
    private Label QuitLabel;
    @FXML
    private Label Title;
    @FXML
    private Label MainTitle;
    /**
     * Initializes the controller class.
     * @param url the url
     * @param rb the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Title.setFont(Font.loadFont(ToF.getResource("MR.ttf").toString(), 30));
        //MainTitle.setFont(Font.loadFont(ToF.getResource("Skyrim.ttf").toString(), 20));
        NewArrow.setVisible(false);
        LoadArrow.setVisible(false);
        QuitArrow.setVisible(false);
        NewLabel.setOnMouseEntered(e -> NewArrow.setVisible(true));
        NewLabel.setOnMouseExited(e -> NewArrow.setVisible(false));
        NewLabel.setOnMousePressed(e -> ToF.newGame());
        LoadLabel.setOnMouseEntered(e -> LoadArrow.setVisible(true));
        LoadLabel.setOnMouseExited(e -> LoadArrow.setVisible(false));
        LoadLabel.setOnMousePressed(e -> ToF.load());
        QuitLabel.setOnMouseEntered(e -> QuitArrow.setVisible(true));
        QuitLabel.setOnMouseExited(e -> QuitArrow.setVisible(false));
        QuitLabel.setOnMousePressed(e -> Platform.exit());
        
    }    
    
}
