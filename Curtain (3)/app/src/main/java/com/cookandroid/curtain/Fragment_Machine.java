package com.cookandroid.curtain;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;



public class Fragment_Machine extends Fragment {

    private LineChart chart;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_machine, container, false);

        lineChart = rootView.findViewById(R.id.line_chart);

        ArrayList<String> xAXES = new ArrayList<>();
        ArrayList<Entry> yAXESsin = new ArrayList<>();
        ArrayList<Entry> yAXEScos = new ArrayList<>();

        double x = 0;
        int numDataPoints = 6;

        for(int i=0; i<numDataPoints; i++){
            float sinFunction = Float.parseFloat(String.valueOf(Math.sin(x)));
            float cosFunction = Float.parseFloat(String.valueOf(Math.cos(x)));
            x = x + 0.1;
            yAXESsin.add(new Entry(sinFunction,i));
            yAXEScos.add(new Entry(cosFunction,i));
            xAXES.add(i, String.valueOf(x)); //x축의 값을 저장합니다.
        }
        String[] xaxes = new String[xAXES.size()];
        for(int i=0; i<xAXES.size(); i++){
            xaxes[i] = xAXES.get(i); // 아래그림의 동그란 부분에 표시되는 x축 값.
        }

        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();
//ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
//아래 그림의 파란색 그래프
        LineDataSet lineDataSet1 = new LineDataSet(yAXEScos, "cos");
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setColor(Color.BLUE);

//아래 그림의 빨간색 그래프
        LineDataSet lineDataSet2 = new LineDataSet(yAXESsin,"sin");
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setColor(Color.RED);

        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);

        lineChart.setData(new LineData(xaxes,lineDataSets));
        lineChart.setVisibleXRangeMaximum(65f);

        return rootView;

    }


     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_machine, container, false);

        chart = rootView.findViewById(R.id.line_chart);

        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<Entry> values1 = new ArrayList<>();
        ArrayList<Entry> values2 = new ArrayList<>();


        for (int i = 0; i < 10; i++) {

            //랜덤으로 데이터 받아옴
            float val = (float) (Math.random() * 10);
            values.add(new Entry(i, val));

            float val1 = (float) (Math.random() * 10);
            values1.add(new Entry(i, val1));

            float v_add = (val+val1)/2;

            values2.add(new Entry(i, v_add));
        }

        LineDataSet set1 = new LineDataSet(values, "out jodo");
        LineDataSet set2 = new LineDataSet(values1, "in jodo");
        LineDataSet set3 = new LineDataSet(values2, "average");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets
        dataSets.add(set2);
        dataSets.add(set3);

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // black lines and points
        set1.setColor(Color.BLUE);
        set1.setCircleColor(Color.BLUE);

        set2.setColor(Color.RED);
        set2.setCircleColor(Color.RED);

        set3.setColor(Color.BLACK);
        set3.setCircleColors(Color.BLACK);

        // set data
        chart.setData(data);

        return rootView;
    }


}