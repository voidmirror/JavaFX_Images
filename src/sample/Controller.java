package sample;

import javafx.animation.TranslateTransition;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button btnChange;
    @FXML
    private Button btnMove;
    @FXML
    private Button btnCompare;
    @FXML
    private ChoiceBox<String> choiceBox1;
    @FXML
    private ChoiceBox<String> choiceBox2;
    @FXML
    private ImageView view1;
    @FXML
    private MediaView mediaView;

    int previousNumofKeys = 0;
    Image image1;
    Image image2;
    HashMap<String, Image> hashMap;
    FileChooser fileChooser;

    int i = 0;

    public void changeImage() {
        if (i == 0) {
            view1.setImage(image2);
            i++;
        } else {
            view1.setImage(image1);
            i--;
        }
        System.out.println(i);
    }

    public void move() {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(4000), view1);
        translateTransition.setFromY(view1.getTranslateY());
        translateTransition.setToY(view1.getTranslateY() + 100);
        translateTransition.play();
    }

    public void chooseImage() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            System.out.println(selectedFile);
            try {
                Image tmp = null;
                tmp = new Image(selectedFile.toURI().toURL().toString());
                view1.setImage(tmp);
                StringBuffer s = new StringBuffer(selectedFile.toURI().toURL().toString());
                int slashes = 0;
                char c;
                for (int i = 0; i < selectedFile.toURI().toURL().toString().length(); i++) {
                    c = selectedFile.toURI().toURL().toString().charAt(i);
                    System.out.print(selectedFile.toURI().toURL().toString().charAt(i));
                    if (c == '/') {
                        slashes++;
                    }
                }
                System.out.println();
                int slHere = 0;
                for (int i = 0; i < selectedFile.toURI().toURL().toString().length(); i++) {
                    c = selectedFile.toURI().toURL().toString().charAt(i);
                    s.deleteCharAt(0);
                    if (c == '/') {
                        slHere++;
                        if (slHere == slashes) {
                            break;
                        }
                    }
                }
                System.out.println(s);

                hashMap.put(s.toString(), tmp);
                System.out.println(hashMap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

    }

    public void compareImages() {

    }

    public void refreshChoiceBoxes() {
        if (hashMap.size() > previousNumofKeys) {
            ArrayList<String> arrayList = new ArrayList<>();
            for (Object s : hashMap.keySet()) {
                arrayList.add(s.toString());
            }
            ObservableList<String> observableList = FXCollections.observableArrayList(arrayList);
            choiceBox1.getItems().clear();
            choiceBox2.getItems().clear();
            choiceBox1.getItems().addAll(observableList);
            choiceBox2.getItems().addAll(observableList);
            previousNumofKeys = hashMap.size();
        }
    }

    public void comparison() throws IOException {
        ImageEqualer imageEqualer = new ImageEqualer(hashMap.get(choiceBox1.getValue()), hashMap.get(choiceBox2.getValue()), view1);
//        imageEqualer.isEqual();
        imageEqualer.grayscaleByteFormer(imageEqualer.getImage1());
    }

    public void changeImageView() {
        view1.setImage(hashMap.get(choiceBox1.getValue()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Hey dude");
        hashMap = new HashMap<>();
        try {
            image1 = new Image((new File("src\\sample\\1.png")).toURI().toURL().toString());
            image2 = new Image((new File("src\\sample\\2.png")).toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        view1.setImage(image1);


    }
}
