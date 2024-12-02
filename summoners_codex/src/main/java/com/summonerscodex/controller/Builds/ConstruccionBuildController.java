package com.summonerscodex.controller.Builds;

import com.summonerscodex.model.Campeon;
import com.summonerscodex.model.Objeto;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.summonerscodex.model.Build;
import com.summonerscodex.services.CampeonesServices;
import com.summonerscodex.services.MongoDBConexion;
import com.summonerscodex.services.ObjetoServices;
import com.summonerscodex.utils.Session;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class ConstruccionBuildController {

    @FXML
    private ComboBox<Campeon> comboBoxCampeones; 
    @FXML
    private ComboBox<Objeto> comboBoxObjetos; 
    @FXML
    private ListView<String> listViewObjetos; 
    @FXML
    private Button btnAgregarObjeto, btnEliminarObjeto, btnGuardarBuild,btnRegresar;
    @FXML
    private ImageView imageViewCampeon1,imageViewObjeto1,imageViewObjeto2,imageViewObjeto3,imageViewObjeto4,imageViewObjeto5,imageViewObjeto6;

    private List<Campeon> campeones = new ArrayList<>();  
    private List<Objeto> objetosDisponibles = new ArrayList<>(); 
    private Build build; 
    
    private String usuarioAutenticado = Session.getInstance().getUsuarioAutenticado();
    @FXML
    private void initialize() throws Exception {
        // Inicializar el servicio de campeones y objetos
        CampeonesServices campeonesServices = new CampeonesServices();
        campeones = campeonesServices.obtenerCampeonesDesdeDataDragon();
        ObjetoServices objetoServices = new ObjetoServices();        
        objetosDisponibles = objetoServices.obtenerObjetosDesdeDataDragon();
        comboBoxCampeones.getItems().addAll(campeones);
        // Usar un StringConverter para mostrar solo el nombre del campeón en el ComboBox
        comboBoxCampeones.setCellFactory(param -> new ListCell<Campeon>() {
            @Override
            protected void updateItem(Campeon item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName()); 
                }
            }
        });
        comboBoxCampeones.setConverter(new StringConverter<Campeon>() {
            @Override
            public String toString(Campeon campeon) {
                return campeon != null ? campeon.getName() : "";
            }

            @Override
            public Campeon fromString(String string) {
                return null;
            }
        });
        comboBoxObjetos.getItems().addAll(objetosDisponibles);

        // Usar un StringConverter para mostrar solo el nombre del objeto en el ComboBox
        comboBoxObjetos.setCellFactory(param -> new ListCell<Objeto>() {
            @Override
            protected void updateItem(Objeto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName()); 
                }
            }
        });
        comboBoxObjetos.setConverter(new StringConverter<Objeto>() {
            @Override
            public String toString(Objeto objeto) {
                return objeto != null ? objeto.getName() : "";
            }

            @Override
            public Objeto fromString(String string) {
                return null; 
            }
        });    
        build = new Build(null, new ArrayList<>());  
        // Evento para actualizar la imagen del campeón seleccionado
        comboBoxCampeones.setOnAction(e -> {
			try {
				actualizarImagenCampeon();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});     
        // Definir las acciones de los botones
        btnAgregarObjeto.setOnAction(e -> {
			try {
				agregarObjeto();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        btnEliminarObjeto.setOnAction(e -> {
			try {
				eliminarObjeto();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        btnGuardarBuild.setOnAction(e -> {
			try {
				guardarBuild();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
    }

    private void actualizarImagenCampeon() throws Exception {
        Campeon campeonSeleccionado = comboBoxCampeones.getValue();
        if (campeonSeleccionado != null) {
            // Actualizar la imagen del campeón en el ImageView correspondiente
            String imageUrl = campeonSeleccionado.getImageUrl();
            imageViewCampeon1.setImage(new Image(imageUrl));
            // Establecer el campeón en la build
            build.setCampeon(campeonSeleccionado);
        }
    }

    private void agregarObjeto() throws Exception {
    	// Verificar si ya hay 6 objetos en la build
        if (build.getItems().size() >= 6) {
            return; 
        }
        Objeto objetoSeleccionado = comboBoxObjetos.getValue();
        if (objetoSeleccionado != null) {
            if (!build.getItems().contains(objetoSeleccionado.getName())) {
                // Agregar el objeto solo si no está ya en la lista
                build.addObjeto(objetoSeleccionado.getName());               
                listViewObjetos.getItems().add(objetoSeleccionado.getName());
                // Actualizar la imagen del objeto en el siguiente ImageView vacío
                String imageUrl = objetoSeleccionado.getImageUrl();
                if (imageUrl != null) {
                    if (build.getItems().size() == 1) {
                        imageViewObjeto1.setImage(new Image(imageUrl));
                    } else if (build.getItems().size() == 2) {
                        imageViewObjeto2.setImage(new Image(imageUrl));
                    } else if (build.getItems().size() == 3) {
                        imageViewObjeto3.setImage(new Image(imageUrl));
                    } else if (build.getItems().size() == 4) {
                        imageViewObjeto4.setImage(new Image(imageUrl));
                    } else if (build.getItems().size() == 5) {
                        imageViewObjeto5.setImage(new Image(imageUrl));
                    } else if (build.getItems().size() == 6) {
                        imageViewObjeto6.setImage(new Image(imageUrl));
                    }
                }
            }
        }
    }

    private void eliminarObjeto() throws Exception {
        String objetoSeleccionado = listViewObjetos.getSelectionModel().getSelectedItem();
        if (objetoSeleccionado != null) {
            // Eliminar el objeto de la build y la UI
            build.removeObjeto(objetoSeleccionado);
            listViewObjetos.getItems().remove(objetoSeleccionado);

            // Crear una imagen blanca para usar como fondo
            Image imageBlanca = new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/wcAAwAB/4dCUeQAAAAASUVORK5CYII=");

            // Actualizar las imágenes de los objetos restantes
            for (int i = 0; i < build.getItems().size(); i++) {
                String objeto = build.getItems().get(i);
                if (objeto.equals(objetoSeleccionado)) {
                    continue;  
                }

                // Establecer la imagen blanca para los objetos que ya no están en la lista
                switch (i) {
                    case 0:
                        imageViewObjeto1.setImage(new Image(objetosDisponibles.stream().filter(o -> o.getName().equals(build.getItems().get(0))).findFirst().get().getImageUrl()));
                        break;
                    case 1:
                        imageViewObjeto2.setImage(new Image(objetosDisponibles.stream().filter(o -> o.getName().equals(build.getItems().get(1))).findFirst().get().getImageUrl()));
                        break;
                    case 2:
                        imageViewObjeto3.setImage(new Image(objetosDisponibles.stream().filter(o -> o.getName().equals(build.getItems().get(2))).findFirst().get().getImageUrl()));
                        break;
                    case 3:
                        imageViewObjeto4.setImage(new Image(objetosDisponibles.stream().filter(o -> o.getName().equals(build.getItems().get(3))).findFirst().get().getImageUrl()));
                        break;
                    case 4:
                        imageViewObjeto5.setImage(new Image(objetosDisponibles.stream().filter(o -> o.getName().equals(build.getItems().get(4))).findFirst().get().getImageUrl()));
                        break;
                    case 5:
                        imageViewObjeto6.setImage(new Image(objetosDisponibles.stream().filter(o -> o.getName().equals(build.getItems().get(5))).findFirst().get().getImageUrl()));
                        break;
                }
            }

            // Para limpiar las imágenes si el objeto ya no existe en la build
            for (int i = build.getItems().size(); i < 6; i++) {              
                switch (i) {
                    case 0:
                        imageViewObjeto1.setImage(imageBlanca);
                        break;
                    case 1:
                        imageViewObjeto2.setImage(imageBlanca);
                        break;
                    case 2:
                        imageViewObjeto3.setImage(imageBlanca);
                        break;
                    case 3:
                        imageViewObjeto4.setImage(imageBlanca);
                        break;
                    case 4:
                        imageViewObjeto5.setImage(imageBlanca);
                        break;
                    case 5:
                        imageViewObjeto6.setImage(imageBlanca);
                        break;
                }
            }
        }
    }



    public void guardarBuild() throws Exception {
        // Obtener el nombre y la URL de la imagen del campeón
        String campeonNombre = build.getCampeon() != null ? build.getCampeon().getName() : "";
        String campeonImagenUrl = build.getCampeon() != null ? build.getCampeon().getImageUrl() : ""; 
        
        // Crear una lista para almacenar las URLs de las imágenes de los objetos
        List<String> objetosNombres = new ArrayList<>();
        List<String> objetosImagenesUrls = new ArrayList<>();
        
        // Obtener los objetos y sus imágenes
        for (String objetoNombre : build.getItems()) {
            objetosNombres.add(objetoNombre);
            Objeto objeto = objetosDisponibles.stream()
                                              .filter(o -> o.getName().equals(objetoNombre))
                                              .findFirst()
                                              .orElse(null);
            if (objeto != null) {
                objetosImagenesUrls.add(objeto.getImageUrl());
            }
        }

        // Verificar si la build contiene un campeón y al menos un objeto
        if (campeonNombre.isEmpty() || objetosNombres.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al guardar");
            alert.setHeaderText(null);
            alert.setContentText("La build debe contener un campeón y al menos un objeto.");
            alert.showAndWait();
            return;
        }

        // Mostrar una confirmación al usuario
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmación de guardado");
        confirmationAlert.setHeaderText("¿Estás seguro de guardar esta build?");
      

        // Esperar la respuesta del usuario
        var resultado = confirmationAlert.showAndWait();
        if (resultado.isEmpty() || resultado.get() != ButtonType.OK) {
            // Si el usuario cancela, salir del método
            return;
        }

        // Crear el documento de la build
        Document buildDocument = new Document("campeonNombre", campeonNombre)
                                    .append("campeonImagenUrl", campeonImagenUrl)
                                    .append("objetosNombres", objetosNombres) // Lista con los nombres de los objetos
                                    .append("objetosImagenesUrls", objetosImagenesUrls); // Lista con las URLs de las imágenes de los objetos

        // Conectar a la base de datos y guardar la build
        MongoDBConexion.conectar();
        MongoCollection<Document> usuariosCollection = MongoDBConexion.getDatabase().getCollection("usuarios");

        try {
            // Actualizar el documento del usuario
            usuariosCollection.updateOne(
                Filters.eq("usuario", usuarioAutenticado),
                Updates.push("builds", buildDocument)
            );

            // Mostrar mensaje de éxito
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Éxito");
            successAlert.setHeaderText(null);
            successAlert.setContentText("¡Build guardada exitosamente!");
            successAlert.showAndWait();
        } catch (MongoException e) {
            // Mostrar mensaje de error
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("No se pudo guardar la build.");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        } finally {
            MongoDBConexion.cerrarConexion();
        }
    }


    @FXML
    private void RegresarMenu() {
        try {
        
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/summonerscodex/views/Builds/SeleccionBuildPantalla.fxml"));
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
