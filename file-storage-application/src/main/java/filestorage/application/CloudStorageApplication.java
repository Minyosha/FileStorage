package filestorage.application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CloudStorageApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CloudStorageApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Cloud Storage Application Client");
        stage.setScene(scene);
        stage.show();
    }

//    private void readLoop() {
//        try {
//            while (true) {
//                String command = network.readString();
//                if (command.equals("#list#")) {
//                    Platform.runLater(() -> serverView.getItems().clear());
//                    int len = network.readInt();
//                    for (int i = 0; i < len; i++) {
//                        String file = network.readString();
//                        Platform.runLater(() -> serverView.getItems().add(file));
//                    }
//                }
//                Platform.runLater(() -> serverView.getItems().add(command));
//            }
//        } catch (Exception e) {
//            System.err.println("Connection lost");
//        }
//    }

    public static void main(String[] args) {
        launch();
    }
}