package com.summonerscodex.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class MainViewController implements Initializable {
              
    @FXML
    private Button btnIniciarSesion, btnRegistrarse;
    
    @FXML
    private StackPane containerForm;
    
    private VBox signInForm, signUpForm; 
    
    @FXML
    public void actionEvent(ActionEvent e){
        
        Object evt = e.getSource();
        
        if(evt.equals(btnIniciarSesion)){
            signInForm.setVisible(true);
            signUpForm.setVisible(false);                    
        }else if(evt.equals(btnRegistrarse)){
            signUpForm.setVisible(true);                                
            signInForm.setVisible(false);
        }
        
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            signInForm = loadForm("/com/summonerscodex/views/SignInForm.fxml");
            signUpForm = loadForm("/com/summonerscodex/views/SignUpForm.fxml");    
            containerForm.getChildren().addAll(signInForm, signUpForm);
            signInForm.setVisible(true);
            signUpForm.setVisible(false);
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
       
    private VBox loadForm(String url) throws IOException{    
        return (VBox) FXMLLoader.load(getClass().getResource(url));    
    }
    
    
}
