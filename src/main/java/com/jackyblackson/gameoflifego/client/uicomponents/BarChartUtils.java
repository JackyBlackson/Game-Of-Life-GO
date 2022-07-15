package com.jackyblackson.gameoflifego.client.uicomponents;

import com.almasb.fxgl.dsl.FXGL;
import com.jackyblackson.gameoflifego.client.info.GamePlayInfo;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Background;

class BarChartUtils
{
    private BarChart bc;
    BarChartUtils(BarChart barChart)
    {
        this.bc = barChart;
    }
    void operateBarChart()
    {
        //两条轴线
        final CategoryAxis xAxis = (CategoryAxis) bc.getXAxis();
        final NumberAxis yAxis = (NumberAxis) bc.getYAxis();
        //图表标题
        bc.setTitle("Player Statistics");
        //轴名
        xAxis.setLabel("Value");
        yAxis.setLabel("Players");

        //系列1
        try{
            int i = 0;
            for (String stat : GamePlayInfo.getInstance().playerInfoStringSet) {
                if(stat != null){
                    String[] playerStat = stat.split("%");
                    XYChart.Series<String, Long> player = new XYChart.Series<>();
                    player.setName(playerStat[0]);

                    player.getData().add(new XYChart.Data<>("Cells", Long.parseLong(playerStat[2])));
                    player.getData().add(new XYChart.Data<>("Score", Long.parseLong(playerStat[3])));
                    bc.getData().add(i, player);

                    Node node = bc.lookup("default-color" + i);

                    i++;
                }
            }
        } catch (Exception ex){
            FXGL.showMessage("Cannot interpret statistics from server: " + ex.getMessage());
        }
    }
}
