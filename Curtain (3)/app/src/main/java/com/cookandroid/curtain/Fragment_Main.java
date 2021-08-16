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
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.w3c.dom.Text;

public class Fragment_Main extends Fragment {

    TextView ctn_state, led_bright,auto_state;
    Button color_btn, color_btn2;
    LinearLayout auto_layout;

    State state;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        ctn_state = rootView.findViewById(R.id.ctn_state);//수동제어 단계 버튼
        auto_state = rootView.findViewById(R.id.auto_state);//자동제어 단계 버튼
        color_btn = rootView.findViewById(R.id.color_btn); //수동제어 색상 버튼
        color_btn2 = rootView.findViewById(R.id.color_btn_auto);  //자동제어 색상 버튼
        auto_layout = rootView.findViewById(R.id.auto_layout); // 자동제어 layout 부분
        led_bright = rootView.findViewById(R.id.led_bright);//


        state = (State) getActivity().getApplication();

        // 수동 제어 색상
        state.setLed("#ffab91");
        GradientDrawable d = (GradientDrawable) color_btn.getBackground();
        d.setColor(Color.parseColor(state.getLed()));

        // 자동 제어 색상
        state.setAuto_Led("#ffab91");
        GradientDrawable d_auto = (GradientDrawable) color_btn2.getBackground();
        d_auto.setColor(Color.parseColor(state.getAuto_Led()));





        return rootView;


    }

    @Subscribe
    public void busStop(BusEvent busEvent) {
        // 수동 제어 단계
        if(busEvent.flag) {
            ctn_state.setText(busEvent.curtain + "단계");
            // 수동 제어 색상
            GradientDrawable d = (GradientDrawable) color_btn.getBackground();
            d.setColor(Color.parseColor(busEvent.led));

            led_bright.setText("밝기 : " + busEvent.bright + "%");

            //자동 제어 단계
            auto_state.setText("자동 단계 : " + busEvent.auto_step);


            //자동 제어 색상
            GradientDrawable d1 = (GradientDrawable) color_btn2.getBackground();
            d1.setColor(Color.parseColor(busEvent.auto_led));
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


