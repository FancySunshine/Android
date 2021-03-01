package com.cookandroid.curtain;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.w3c.dom.Text;

public class Fragment_Main extends Fragment {

    TextView ctn_state;
    String color, auto_color;
    Button color_btn, color_btn2;
    LinearLayout auto_layout;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        ctn_state = rootView.findViewById(R.id.ctn_state);
        color_btn = (rootView).findViewById(R.id.color_btn); //수동제어 색상 버튼
        color_btn2 = (rootView).findViewById(R.id.color_btn_auto);  //자동제어 색상 버튼
        auto_layout = (rootView).findViewById(R.id.auto_layout);


        // 수동 제어 색상
        color = "#ffab91";
        GradientDrawable d = (GradientDrawable) color_btn.getBackground();
        d.setColor(Color.parseColor(color));

        // 자동 제어 색상
        auto_color = "#ffab91";
        GradientDrawable d_auto = (GradientDrawable) color_btn2.getBackground();
        d_auto.setColor(Color.parseColor(auto_color));





        return rootView;


    }
    @Subscribe
    public void busStop(BusEvent busEvent) {
        if(busEvent.flag != null) {
            ctn_state.setText(busEvent.flag + "단계");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);

    }
    @Override
    public void onStart() {
        super.onStart();

    }

}


