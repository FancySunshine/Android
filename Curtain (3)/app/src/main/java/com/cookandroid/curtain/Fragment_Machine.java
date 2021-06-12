package com.cookandroid.curtain;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;



public class Fragment_Machine extends Fragment {

    private LineChart chart;

    State state;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);

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
        chart.setTouchEnabled(false);

        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<Entry> values1 = new ArrayList<>();

//        String strtext = getArguments().getString("time");

        //state.getLux();
        //String str = state.getLux();


        for (int i = 0; i < 10; i++) {

            //랜덤으로 데이터 받아옴
            float val = (float) (Math.random() * 10);
            values.add(new Entry(i, val));

            float val1 = (float) (Math.random() * 10);
            values1.add(new Entry(i, val1));

        }
        String[] aa = {"aa", "bb", "cc"};
        drawChart(aa, values, values1);

        return rootView;
    }

    @Subscribe
    public void busStop(BusEvent busEvent) throws JSONException {
        if(busEvent.lux != null){
            //Toast.makeText(getContext(), busEvent.lux, Toast.LENGTH_SHORT).show();
            JSONArray msg = new JSONArray(busEvent.lux);
            ArrayList<Entry> out = new ArrayList<>();
            ArrayList<Entry> in = new ArrayList<>();
            String[] labels = new String[msg.length()];
            for (int i = 0; i < msg.length(); i++) {

                out.add(new Entry(i, Float.parseFloat(msg.getJSONObject(i).getString("out"))));

                in.add(new Entry(i, Float.parseFloat(msg.getJSONObject(i).getString("in"))));

                labels[i] = msg.getJSONObject(i).getString("time");
            }

            drawChart(labels, out, in);
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);

    }

    public void drawChart(String[] time, ArrayList<Entry> out, ArrayList<Entry> in) {
        LineDataSet set1 = new LineDataSet(out, "out lux");
        LineDataSet set2 = new LineDataSet(in, "in lux");
        //LineDataSet set3 = new LineDataSet(values2, "avg lux");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets
        dataSets.add(set2);
        //dataSets.add(set3);

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // black lines and points
        set1.setColor(Color.BLUE);
        set1.setCircleColor(Color.BLUE);

        set2.setColor(Color.RED);
        set2.setCircleColor(Color.RED);


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(time));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

//        set3.setColor(Color.BLACK);
//        set3.setCircleColors(Color.BLACK);

        // set data
        chart.setData(data);
        chart.invalidate();
    }


}