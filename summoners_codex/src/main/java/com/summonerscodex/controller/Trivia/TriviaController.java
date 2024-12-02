package com.summonerscodex.controller.Trivia;

import com.summonerscodex.model.Pregunta;
import com.summonerscodex.services.PreguntaService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.util.List;

public class TriviaController {

    @FXML
    private Label numeroPreguntaLabel,preguntaLabel,resultadoLabel,resultadosFinalesLabel;
    
    @FXML
    private RadioButton opcionAButton,opcionBButton,opcionCButton;

    @FXML
    private Button verificarButton, btnRegresar;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private VBox feedbackBox;

    @FXML
    private Circle feedbackIcon;

    @FXML
    private HBox resultadoHBox;

    private ToggleGroup opcionesGroup;
    private int currentQuestionIndex = 0;
    private List<Pregunta> questions;
    private boolean respuestaVerificada = false;

    private int respuestasCorrectas = 0;
    private int respuestasIncorrectas = 0;

    private PreguntaService questionService;

    @FXML
    public void initialize() {
        questionService = new PreguntaService();
        questions = questionService.obtenerPreguntas();

        if (questions == null || questions.isEmpty()) {
            System.err.println("No se han obtenido preguntas.");
            return;
        }

        opcionesGroup = new ToggleGroup();
        opcionAButton.setToggleGroup(opcionesGroup);
        opcionBButton.setToggleGroup(opcionesGroup);
        opcionCButton.setToggleGroup(opcionesGroup);

        feedbackBox.setVisible(false);
        resultadosFinalesLabel.setVisible(false);

        mostrarPregunta();
    }

    @FXML
    private void verificarOSiguiente() {
        if (!respuestaVerificada) {
            verificarRespuesta();
        } else {
            currentQuestionIndex++;
            mostrarPregunta();
        }
    }

    private void verificarRespuesta() {
        RadioButton selectedRadioButton = (RadioButton) opcionesGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            String respuestaSeleccionada = selectedRadioButton.getText();
            Pregunta currentQuestion = questions.get(currentQuestionIndex);

            if (currentQuestion.esCorrecta(respuestaSeleccionada)) {
                resultadoLabel.setText("¡Correcto!");
                resultadoLabel.setStyle("-fx-text-fill: #00FF7F;"); 
                feedbackIcon.setFill(javafx.scene.paint.Color.GREEN);  
                respuestasCorrectas++;
            } else {
                resultadoLabel.setText("Incorrecto. La respuesta correcta es: " + currentQuestion.getOpcionCorrecta());
                resultadoLabel.setStyle("-fx-text-fill: #FF4C4C;"); 
                feedbackIcon.setFill(javafx.scene.paint.Color.RED);  
                respuestasIncorrectas++;
            }

            feedbackBox.setVisible(true);
            verificarButton.setText("Siguiente");
            respuestaVerificada = true;
            deshabilitarOpciones();
        }
    }

    private void deshabilitarOpciones() {
        opcionAButton.setDisable(true);
        opcionBButton.setDisable(true);
        opcionCButton.setDisable(true);
    }

    private void habilitarOpciones() {
        opcionAButton.setDisable(false);
        opcionBButton.setDisable(false);
        opcionCButton.setDisable(false);
    }

    private void mostrarPregunta() {
        if (currentQuestionIndex < questions.size()) {
            Pregunta currentQuestion = questions.get(currentQuestionIndex);

            double progreso = (double) (currentQuestionIndex + 1) / questions.size();
            progressBar.setProgress(progreso);

            numeroPreguntaLabel.setText("Pregunta " + (currentQuestionIndex + 1) + " de " + questions.size());
            preguntaLabel.setText(currentQuestion.getTextoPregunta());
            opcionAButton.setText(currentQuestion.getOpcionA());
            opcionBButton.setText(currentQuestion.getOpcionB());
            opcionCButton.setText(currentQuestion.getOpcionC());

            opcionesGroup.selectToggle(null);
            feedbackBox.setVisible(false);
            verificarButton.setText("Verificar");
            respuestaVerificada = false;
            habilitarOpciones();
        } else {
            numeroPreguntaLabel.setText("¡Trivia completada!");
            preguntaLabel.setText("Gracias por participar.");
            verificarButton.setText("Reiniciar");
            verificarButton.setOnAction(event -> reiniciarTrivia());
            verificarButton.setVisible(true);
            mostrarResultados();
        }
    }

    private void mostrarResultados() {
        if (resultadosFinalesLabel != null) {
            String resultados = "Respuestas Correctas: " + respuestasCorrectas + "\n" +
                                "Respuestas Incorrectas: " + respuestasIncorrectas + "\n" +
                                "Puntaje Final: " + String.format("%.2f", (double) respuestasCorrectas / questions.size() * 100) + "%";
            if (respuestasCorrectas == questions.size()) {
                resultadosFinalesLabel.setText(resultados + "\n¡Excelente! Puntaje perfecto.");
            } else if (respuestasCorrectas >= questions.size() / 2) {
                resultadosFinalesLabel.setText(resultados + "\n¡Buen trabajo! Tu puntaje fue bastante bueno.");
            } else {
                resultadosFinalesLabel.setText(resultados + "\nSigue practicando, ¡lo harás mejor la próxima vez!");
            }
            
            resultadosFinalesLabel.setVisible(true);
            mostrarVentanaEmergente(); 
        }
    }
    private void reiniciarTrivia() {
        currentQuestionIndex = 0;
        respuestasCorrectas = 0;
        respuestasIncorrectas = 0;
        resultadosFinalesLabel.setVisible(false);
        verificarButton.setText("Verificar");
        verificarButton.setOnAction(event -> verificarOSiguiente());
        mostrarPregunta();
    }
    private void mostrarVentanaEmergente() {
        // Crear el mensaje emergente
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Resultados del Quiz");
        alert.setHeaderText("¡Trivia Terminada!");

        // Mostrar los resultados
        String mensaje = "Aciertos: " + respuestasCorrectas + "\nFallos: " + respuestasIncorrectas;
        alert.setContentText(mensaje);
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        alert.initOwner(stage);
        alert.showAndWait();
    }
    @FXML
    private void RegresarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/Seleccion_de_pantalla.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRegresar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al regresar a la pantalla de selección: " + e.getMessage());
        }
    }
}
