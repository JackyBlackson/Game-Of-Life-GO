package com.jackyblackson.gameoflifego.client.uicomponents;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;

import java.util.HashMap;
import java.util.Map;


class PieChartUtils
{
    private PieChart pieChart;
    private ObservableList<PieChart.Data> pieChartData;
    PieChartUtils(PieChart pieChart)
    {
        pieChartData = FXCollections.observableArrayList();
        this.pieChart = pieChart;
    }
    private void addData(String name, double value)
    {
        pieChartData.add(new PieChart.Data(name, value));
    }
    private void showChart()
    {
        pieChart.setData(pieChartData);
    }
    private void setDataColor(int index, String color)
    {
        pieChartData.get(index).getNode().setStyle("-fx-background-color: "+ color);
    }
    private void setMarkVisible(boolean b)
    {
        pieChart.setLabelsVisible(b);
    }
    private void setLegendColor(HashMap<Integer, String> colors)
    {
        String setColor = "";
        for(Map.Entry<Integer, String> entry:colors.entrySet())
        {
            int index = entry.getKey();
            String color = entry.getValue();
            setColor = setColor.concat("CHART_COLOR_" + (index+1) + ":" + color+";");
        }
        pieChart.setStyle(setColor);
    }
    private void setLegendSide(String side)
    {
        if(side.equalsIgnoreCase("top"))
            pieChart.setLegendSide(Side.TOP);
        else if(side.equalsIgnoreCase("bottom"))
            pieChart.setLegendSide(Side.BOTTOM);
        else if(side.equalsIgnoreCase("left"))
            pieChart.setLegendSide(Side.LEFT);
        else{
            pieChart.setLegendSide(Side.RIGHT);
        }
    }
    private void setTitle(String title)
    {
        pieChart.setTitle(title);
    }
    void operatePieChart()
    {
        PieChartUtils pieChartUtils = new PieChartUtils(pieChart);
        pieChartUtils.addData("R", 0.5);
        pieChartUtils.addData("G", 0.3);
        pieChartUtils.addData("B", 0.2);
        //????????????
        pieChartUtils.showChart();
        //?????????????????????????????????????????????????????????
        pieChartUtils.setDataColor(0, "red");
        pieChartUtils.setDataColor(1, "green");
        pieChartUtils.setDataColor(2, "blue");
        //????????????
        pieChartUtils.setMarkVisible(false);
        //????????????????????????????????????
        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(0, "red");
        hashMap.put(1, "green");
        hashMap.put(2, "blue");
        //???????????????????????????
        pieChartUtils.setLegendColor(hashMap);
        pieChartUtils.setLegendSide("Bottom");
        //?????????????????????
        pieChartUtils.setTitle("RGB");
    }
}
