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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_machine, container, false);


        chart = rootView.findViewById(R.id.line_chart);
        chart.setTouchEnabled(false);

        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<Entry> values1 = new ArrayList<>();


//        String strtext = getArguments().getString("time");
//
//        state.getLux();
//        String str = state.getLux();

//        for (int i = 0; i < 8; i++) {
//
//            //랜덤으로 데이터 받아옴
//            float val = (float) (Math.random() * 10);
//            values.add(new Entry(i, val));
//
//            float val1 = (float) (Math.random() * 10);
//            values1.add(new Entry(i, val1));
//
//        }
        //String[] aa = {"aa", "bb", "cc", "dd", "ee"};
        //drawChart(aa, values);

        return rootView;
    }

    @Subscribe
    public void busStop(BusEvent busEvent) throws JSONException {
        if(busEvent.lux != null){

            //Toast.makeText(getContext(), busEvent.lux, Toast.LENGTH_SHORT).show();
            JSONArray msg = new JSONArray(busEvent.lux);
            ArrayList<Entry> out = new ArrayList<>();
            String[] labels = new String[msg.length()];

            for (int i = 0; i < 8; i++) {
                out.add(new Entry(i, Float.parseFloat(msg.getJSONObject(i).getString("out"))));
                labels[i] = msg.getJSONObject(i).getString("time").substring(11, 13) + "시";

                    //labels[i] = labels[i].substring(6,8) + "시";
            }

            drawChart(labels, out);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);

    }

    public void drawChart(String[] time, ArrayList<Entry> out) {
        LineDataSet set1 = new LineDataSet(out, "외부조도값");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // black lines and points
        set1.setColor(Color.BLUE);
        set1.setCircleColor(Color.BLUE);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(time));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);


        // set data
        chart.setData(data);
        chart.invalidate();
    }


}