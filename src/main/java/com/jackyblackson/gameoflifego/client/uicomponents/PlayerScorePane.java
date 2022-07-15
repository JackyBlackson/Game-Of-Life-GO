package com.jackyblackson.gameoflifego.client.uicomponents;

import com.almasb.fxgl.dsl.FXGL;
import com.jackyblackson.gameoflifego.client.info.GamePlayInfo;
import javafx.geometry.Pos;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class PlayerScorePane {
    public static BorderPane getPlayerInfoPane(){
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        BorderPane  retPane = null;
        int index = 0;
        pane.addRow(index,
                scorePaneFactory(Color.WHITE, "Player"),
                scorePaneFactory(Color.WHITE, "Cells"),
                scorePaneFactory(Color.WHITE, "Scores")
        );
        try{
            for (String s : GamePlayInfo.getInstance().playerInfoStringSet) {
                if(s != null){
                    index++;
                    String[] info = s.split("%");
                    Color color = Color.web(info[1]);
                    pane.addRow(index,
                            scorePaneFactory(color, info[0]),
                            scorePaneFactory(color, info[2]),
                            scorePaneFactory(color, info[3])
                    );
                }
            }
            index++;
            BarChart<String, Number> chart = new BarChart<>(new CategoryAxis(), new NumberAxis());
            new BarChartUtils(chart).operateBarChart();
            retPane = new BorderPane(pane, null, null, chart, null);
        } catch (Exception ex){
            FXGL.showMessage("Cannot get players' score, because of message" + ex.getMessage());
        }
        return retPane;
    }

    private static StackPane scorePaneFactory(Paint background, String text){
        Rectangle windowsUi = new Rectangle(130, 40);

        windowsUi.setStroke(background);
        windowsUi.setStrokeWidth(3);

        Text titleText = FXGL.getUIFactoryService().newText(text);
        titleText.setEffect(new DropShadow(5, Color.WHITE));

        StackPane pane = new StackPane(windowsUi, titleText);
        StackPane.setAlignment(pane, Pos.CENTER);
        pane.setEffect(new DropShadow(10, Color.WHITE));

        return pane;
    }
}
