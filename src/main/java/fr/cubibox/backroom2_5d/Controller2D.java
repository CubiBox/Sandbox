package fr.cubibox.backroom2_5d;

import fr.cubibox.backroom2_5d.engine.Ray;
import fr.cubibox.backroom2_5d.engine.maths.Line2F;
import fr.cubibox.backroom2_5d.entities.Player;
import fr.cubibox.backroom2_5d.game.Chunk;
import fr.cubibox.backroom2_5d.game.Map;
import fr.cubibox.backroom2_5d.game.Polygon;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller2D implements Initializable {

    private final int paneWidth = 300;
    @FXML
    private Pane coordinateSystem;
    @FXML
    private TextArea functionText;
    @FXML
    private VBox vBoxPanel;
    @FXML
    private Button importer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //drawFunction();
        Movement clock = new Movement();
        clock.start();
    }

    public void drawFunction() {
        coordinateSystem.getChildren().clear();

        //grid
        for (Rectangle r : drawGrid())
            coordinateSystem.getChildren().add(r);


        //draw the player's point
        Player p = Main.getEngine().getPlayer();
        coordinateSystem.getChildren().add(new Circle(Main.toScreenX(p.getX()), Main.toScreenY(p.getY()), 2, Color.RED));


        //draw the player's collision box
        Rectangle collisionBox = new Rectangle(
                Main.toScreenX(p.getX() - p.getCollisionBox().getWidth() / 2),
                Main.toScreenY(p.getY() - p.getCollisionBox().getHeight() / 2),
                Main.toScreenX(p.getCollisionBox().getWidth()),
                Main.toScreenY(p.getCollisionBox().getHeight())
        );
        collisionBox.setFill(Color.TRANSPARENT);
        collisionBox.setStroke(Color.RED);
        coordinateSystem.getChildren().add(collisionBox);

        //draw the player's rays
        for (Ray r : Main.getEngine().getRays()) {
            if (r.getIntersectionX() != Float.POSITIVE_INFINITY && r.getIntersectionY() != Float.POSITIVE_INFINITY) {
                Line l = new Line(Main.toScreenX(p.getX()), Main.toScreenY(p.getY()), Main.toScreenX(r.getIntersectionX()), Main.toScreenY(r.getIntersectionY()));

                if (r.getAngle() == p.getAngle()) {
                    l.setStroke(Color.RED);
                    System.out.println(r.getIntersectionX() + ", " + r.getIntersectionY());
                } else {
                    l.setStroke(Color.BLUE);
                }

                coordinateSystem.getChildren().add(l);
            }
        }

        coordinateSystem.getChildren().add(drawPoint(8f, 8f));

        //draw the polygons & edges normals
        int countPols = -1;
        for (Chunk[] chunkL : Main.getEngine().getMap().getMapContent()) {
            for (Chunk chunk : chunkL) {
                if (chunk.getPols() != null) {
                    for (Polygon pol : chunk.getPols()) {
                        if (countPols != Integer.parseInt(pol.getId())) {
                            coordinateSystem.getChildren().add(pol.getPolShape());
                            countPols++;
                            for (Line2F l : pol.getEdges()) {
                                float midX = l.getA().getX() + (l.getB().getX() - l.getA().getX()) / 2;
                                float midY = l.getA().getY() + (l.getB().getY() - l.getA().getY()) / 2;

                                // place normal on the edge
                                Line normal = new Line(
                                        Main.toScreenX(midX),
                                        Main.toScreenY(midY),
                                        Main.toScreenX(midX + l.getNormal().getX()),
                                        Main.toScreenY(midY + l.getNormal().getY())
                                );

                                normal.setStroke(Color.rgb(255, 255, 0, 1));

                                coordinateSystem.getChildren().add(normal);
                            }
                        }
                    }
                }
            }
        }
    }

    public ArrayList<Rectangle> drawGrid() {
        Double w = Double.valueOf(Main.DIMC);
        Double h = Double.valueOf(Main.DIML);
        ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();

        Color gray1 = Color.rgb(30, 30, 30);
        Color gray2 = Color.rgb(70, 70, 70);
        Rectangle r;

        for (int i = 0; i < Main.getEngine().getMap().getSize(); i += 8)
            for (int j = 1; j <= 7; j++) {
                r = new Rectangle(0, Main.toScreenY(i + j) - 1, w, 2.0);
                r.setFill(gray1);
                rectangles.add(r);
            }

        for (int i = 0; i < Main.getEngine().getMap().getSize(); i += 8)
            for (int j = 1; j <= 7; j++) {
                r = new Rectangle(Main.toScreenX(i + j) - 1, 0, 2.0, h);
                r.setFill(gray1);
                rectangles.add(r);
            }

        for (int i = 0; i < Main.getEngine().getMap().getSize(); i += 8) {
            r = new Rectangle(0, Main.toScreenY(i) - 1.25, w, 2.5);
            r.setFill(gray2);
            rectangles.add(r);
        }

        for (int i = 0; i < Main.getEngine().getMap().getSize(); i += 8) {
            r = new Rectangle(Main.toScreenX(i) - 1.25, 0, 2.5, h);
            r.setFill(gray2);
            rectangles.add(r);
        }

        r = new Rectangle(w / 2 - 1, 0, 2, h);
        r.setFill(Color.rgb(160, 160, 160));
        rectangles.add(r);

        r = new Rectangle(0, h / 2 - 1, w, 2);
        r.setFill(Color.rgb(180, 180, 180));
        rectangles.add(r);

        return rectangles;
    }

    public Circle drawPoint(double x, double y) {
        return new Circle(Main.toScreenX(x), Main.toScreenY(y), 0.3, Color.RED);
    }

    public void importMapButton() {
        Stage stageSave = new Stage();
        stageSave.setTitle("sauvegarder");
        stageSave.setResizable(false);
        stageSave.setWidth(400);
        stageSave.setHeight(800);
        stageSave.setOnCloseRequest(event -> {
            stageSave.close();
        });
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: #880000");
        ArrayList<String> listeSave = new ArrayList<>();
        File file = new File("maps");
        boolean res = file.mkdir();
        if (res) {
            //System.out.println("le dossier a été créer");
        } else {
            //System.out.println("le dossier existe deja");
        }

        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                listeSave.add(f.getName());
                HBox hBox = new HBox();
                hBox.setSpacing(10);
                Label label = new Label(f.getName());
                label.setTextFill(javafx.scene.paint.Color.WHITE);
                Button button = new Button("charger");
                button.setOnAction(event -> {
//                    if (!main.getPolygons().isEmpty()) {
//                        try {exportMapButton(null);}
//                        catch (IOException e) {throw new RuntimeException(e);}
//                    }
                    Map.importMap(new File("maps\\" + f.getName()));
                    drawFunction();
                    stageSave.close();
                });
                Button button3 = new Button("supprimer");
                button3.setOnAction(event -> {
                    f.delete();
                    hBox.getChildren().clear();
                    listeSave.remove(f.getName());
                });
                hBox.getChildren().addAll(label, button, button3);
                vBox.getChildren().add(hBox);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("erreur");
            alert.setHeaderText("erreur");
            alert.setContentText("Pas de fichier de sauvegarde");
            alert.showAndWait();
        }
        stageSave.setScene(new Scene(vBox));
        stageSave.show();

    }

    public TextArea getFunctionText() {
        return functionText;
    }

    public void setFunctionText(TextArea functionText) {
        this.functionText = functionText;
    }

    private class Movement extends AnimationTimer {
        private long last = 0;

        @Override
        public void handle(long now) {
            long FRAMES_PER_SEC = 60L;
            long INTERVAL = 1000000000L / FRAMES_PER_SEC;

            if (now - last > INTERVAL) {
                drawFunction();
                //Main.getEngine().getPlayer().setRays(OldRay.setRays(Main.getEngine().getPlayer(), Main.getEngine().getMap()));
                last = now;
            }
        }
    }
}
