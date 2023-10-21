package com.example.labakg1;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class LinearController {
    public Pane pane;
    public TextField endX;
    public TextField endY;

    private static final int START_X = 40;
    private static final int START_Y = 605;
    private int bp;

    @FXML
    public void initialize() {
        createGridLabels();
        pane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        createGrid();
    }

    public void buildLine(ActionEvent actionEvent) {
        int step = 40;
        int endx = Integer.parseInt(endX.getText());
        int endy = Integer.parseInt(endY.getText());
        boolean greaterIncrementIsX = endx >= endy;

        bp = greaterIncrementIsX ? endx : endy;
        int minorParameter = greaterIncrementIsX ? endy : endx;
        double offsetFactor = 2 * minorParameter - bp;

        Line defaultLine = new Line(START_X, START_Y, START_X + (step * endx), START_Y - (step * endy));
        defaultLine.setStrokeWidth(1);
        defaultLine.setStroke(Color.SLATEGRAY);
        pane.getChildren().add(defaultLine);

        int tempX = START_X;
        int tempY = START_Y;
        Line lineSegment;

        for (int i = 0; i < bp; i++) {
            if (offsetFactor < 0) {
                if (greaterIncrementIsX) {
                    lineSegment = new Line(tempX, tempY, tempX + step, tempY);
                    tempX += step;
                } else {
                    lineSegment = new Line(tempX, tempY, tempX, tempY - step);
                    tempY -= step;
                }
                offsetFactor += 2 * minorParameter;
            } else {
                lineSegment = new Line(tempX, tempY, tempX + step, tempY - step);
                tempX += step;
                tempY -= step;
                offsetFactor += 2 * (minorParameter - bp);
            }

            lineSegment.setStroke(Color.RED);
            lineSegment.setStrokeWidth(3);
            pane.getChildren().add(lineSegment);

            if (i != 0) {
                if (offsetFactor < 0) {
                    System.out.println("Step along greater increment: U += 2 * SP = 2 * " + minorParameter + " = " + offsetFactor);
                } else {
                    System.out.println("Step diagonally: U += 2 * (SP - LP) = 2 * (" + minorParameter + " - " + bp + ") = " + offsetFactor);
                }
            }
        }
        System.out.println(bp);
    }

    public void clearScreen(ActionEvent actionEvent) {
        ObservableList<Node> children = pane.getChildren();
        int count = children.size();
        for (int i = count - 1; i > count - bp - 2; --i) {
            if (children.get(i) instanceof Line) {
                pane.getChildren().remove(children.get(i));
            }
        }
    }

    private void createGrid() {
        GridPane grid = new GridPane();
        grid.setMouseTransparent(true); // Making the grid non-interactive
        grid.getStyleClass().add("cell-background");

        for (int row = 0; row < 14; row++) {
            for (int col = 0; col < 18; col++) {
                Pane cell = new Pane();
                cell.setPrefSize(40, 40);
                cell.getStyleClass().add("cell");
                cell.setMouseTransparent(true); // Making each cell non-interactive
                grid.add(cell, col, row);
            }
        }

        grid.setLayoutX(START_X);              // align GridPane's left edge with START_X
        grid.setLayoutY(START_Y - 40 * 14);

        pane.getChildren().add(0, grid); // Adding grid as the first child so it's behind everything else
    }

    private void createGridLabels() {
        for (int i = 1; i < 14; i++) {
            Label rowLabel = new Label(String.valueOf(i));
            rowLabel.setLayoutX(START_X-20);
            rowLabel.setLayoutY(START_Y - (i * 40) - 10);
            pane.getChildren().addAll(
                    new Line(START_X-5, START_Y - (i * 40), START_X+3, START_Y - (i * 40)),
                    rowLabel
            );
        }

        for (int i = 1; i < 18; i++) {
            Label colLabel = new Label(String.valueOf(i));
            colLabel.setLayoutX(START_X + (i * 40) - 4);
            colLabel.setLayoutY(START_Y+6);
            pane.getChildren().addAll(
                    new Line(START_X + (i * 40), START_Y-4, START_X + (i * 40), START_Y+4),
                    colLabel
            );
        }
    }
}
