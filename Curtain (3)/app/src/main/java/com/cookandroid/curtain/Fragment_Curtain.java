package com.cookandroid.curtain;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;


public class Fragment_Curtain extends Fragment {

    TextView test, curtain_step;
    Switch sw;
    Slider sb;
    LinearLayout ledlayout;

    int step = 0;

    MaterialButton[] curtain_steps = new MaterialButton[5];
    //int mbuttonids[] = {R.id.curtain_step0, R.id.curtain_step1, R.id.curtain_step2,
    //        R.id.curtain_step3, R.id.curtain_step4};
    MaterialButton max_down, down, up, max_up;

    AppCompatButton[] color_btn = new AppCompatButton[10];
    int[] color_btn_ids = new int[]{R.id.color_btn1, R.id.color_btn2, R.id.color_btn3, R.id.color_btn4,
            R.id.color_btn5, R.id.color_btn6, R.id.color_btn7, R.id.color_btn8, R.id.color_btn9, R.id.color_btn10};
    ArrayList<String> colors = new ArrayList<>();  // Color 넣어줄 list


    public Fragment_Curtain() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_curtain, container, false);
        ledlayout = rootView.findViewById(R.id.Led_layout);
        sw = rootView.findViewById(R.id.aswitch);
        sb = rootView.findViewById(R.id.slider);


        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (sw.isChecked() == true){
                    for(int i = 0; i < color_btn.length; i++){
                        color_btn[i].setEnabled(true);
                    }
                    ledlayout.setBackgroundColor(Color.parseColor("#aacf00"));
                    sb.setEnabled(true);


                }
                else{
                    for(int i = 0; i < color_btn.length; i++){
                        color_btn[i].setEnabled(false);
                    }
                    ledlayout.setBackgroundColor(Color.parseColor("#808080"));
                    sb.setEnabled(false);

                }
            }
        });
        /*
        for(int i = 0; i < curtain_steps.length; i++){
            curtain_steps[i] = rootView.findViewById(mbuttonids[i]);
            final int finalI = i;
            curtain_steps[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ((MainActivity) MainActivity.mContext).mqttClient.publish("Curtain/ctr", String.valueOf(finalI).getBytes(), 0, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            });


        }*/


/*
        colors.add("#f48fb1");
        colors.add("#ffab91");
        colors.add("#c5e1a5");
        colors.add("#80cbc4");
        colors.add("#80deea");
        colors.add("#81d4fa");
        colors.add("#90caf9");
        colors.add("#9fa8da");
        colors.add("#b39ddb");
        colors.add("#ce93d8");
*/

        colors.add("#ffab91");
        colors.add("#F48FB1");
        colors.add("#ce93d8");
        colors.add("#b39ddb");
        colors.add("#9fa8da");
        colors.add("#90caf9");
        colors.add("#81d4fa");
        colors.add("#80deea");
        colors.add("#80cbc4");
        colors.add("#c5e1a5");

        for(int i = 0; i < color_btn.length; i++){
            color_btn[i] = rootView.findViewById(color_btn_ids[i]);
            GradientDrawable d = (GradientDrawable) color_btn[i].getBackground();
            d.setColor(Color.parseColor(colors.get(i)));
            final int finalI = i;
            color_btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), colors.get(finalI), Toast.LENGTH_SHORT).show();
                }
            });
        }




        curtain_step = rootView.findViewById(R.id.curtain_step);

        max_down = rootView.findViewById(R.id.max_down);
        down = rootView.findViewById(R.id.down);
        up = rootView.findViewById(R.id.up);
        max_up = rootView.findViewById(R.id.max_up);

        max_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                step = 0;
                curtain_step.setText(String.valueOf(step) + "단계");
                try {
                    ((MainActivity) MainActivity.mContext).mqttClient.publish("Curtain/ctr", String.valueOf(step).getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }

            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (step - 1 > -1) {
                    step = step - 1;
                    curtain_step.setText(String.valueOf(step) + "단계");
                    try {
                        ((MainActivity) MainActivity.mContext).mqttClient.publish("Curtain/ctr", String.valueOf(step).getBytes(), 0, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (step + 1 < 5) {
                    step = step + 1;
                    curtain_step.setText(String.valueOf(step) + "단계");
                    try {
                        ((MainActivity) MainActivity.mContext).mqttClient.publish("Curtain/ctr", String.valueOf(step).getBytes(), 0, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        max_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                step = 4;
                curtain_step.setText(String.valueOf(step) + "단계");
                try {
                    ((MainActivity) MainActivity.mContext).mqttClient.publish("Curtain/ctr", String.valueOf(step).getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });






        test = rootView.findViewById(R.id.test);

        try {
            ((MainActivity) MainActivity.mContext).mqttClient.subscribe("test", 0);
            ((MainActivity) MainActivity.mContext).mqttClient.publish("client/connect/step", "".getBytes(), 0, false);

        } catch (MqttException e) {
            e.printStackTrace();
        }
        ((MainActivity) MainActivity.mContext).mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                try{((MainActivity) MainActivity.mContext).mqttClient.connect();}catch (Exception e){}
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                if (topic.equals("Curtain/Step")) {
                    curtain_step.setText(message.toString() + "단계");
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        return rootView;
    }
}
