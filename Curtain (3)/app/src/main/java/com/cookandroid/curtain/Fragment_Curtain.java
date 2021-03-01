package com.cookandroid.curtain;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.squareup.otto.Subscribe;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class Fragment_Curtain extends Fragment {


    LinearLayout ledlayout;
    TextView test, curtain_step;
    Switch sw; //색상 스위치
    Slider sb; //밝기 슬라이더
    TextView ctr_state; //커튼 단계
    LinearLayout auto_layout; // 자동 제어 데이터 전달 화면
    State state;
    int step = 0;

    MaterialButton[] curtain_steps = new MaterialButton[5];
    MaterialButton max_down, down, up, max_up;

    AppCompatButton[] color_btn = new AppCompatButton[10];
    int[] color_btn_ids = new int[]{R.id.color_btn1, R.id.color_btn2, R.id.color_btn3, R.id.color_btn4,
            R.id.color_btn5, R.id.color_btn6, R.id.color_btn7, R.id.color_btn8, R.id.color_btn9, R.id.color_btn10};
    ArrayList<String> colors = new ArrayList<>();  // Color 넣어줄 list



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_curtain, container, false);
        ledlayout = rootView.findViewById(R.id.Led_layout);
        sw = rootView.findViewById(R.id.aswitch);
        sb = rootView.findViewById(R.id.slider);





        //스위치 리스너
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (sw.isChecked() == true){
                    for(int i = 0; i < color_btn.length; i++){
                        color_btn[i].setEnabled(true);
                    }
                    ledlayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
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
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {


                ctr_state.setText(String.valueOf(state.getStep()) + "단계");


                try {
                    ((MainActivity) MainActivity.mContext).mqttClient.publish("Curtain/ctr", String.valueOf(state.getStep()).getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }

            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.getStep() - 1 > -1) {
                    state.setStep(state.getStep() - 1);

                    curtain_step.setText(String.valueOf(state.getStep()) + "단계");


                    try {
                        ((MainActivity) MainActivity.mContext).mqttClient.publish("Curtain/ctr", String.valueOf(state.getStep()).getBytes(), 0, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.getStep() + 1 < 5) {
                    state.setStep( state.getStep() + 1);
                    curtain_step.setText(String.valueOf(state.getStep()) + "단계");



                    try {
                        ((MainActivity) MainActivity.mContext).mqttClient.publish("Curtain/ctr", String.valueOf(state.getStep()).getBytes(), 0, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        max_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state.setStep(4);

                curtain_step.setText(String.valueOf(state.getStep()) + "단계");
                String name = (String) curtain_step.getText();


                try {
                    ((MainActivity) MainActivity.mContext).mqttClient.publish("Curtain/ctr", String.valueOf(state.getStep()).getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });






        test = rootView.findViewById(R.id.test);


        return rootView;
    }

    @Subscribe
    public void busStop(BusEvent busEvent) {
        if(busEvent.flag != null) {
            curtain_step.setText(busEvent.flag + "단계");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);

    }

}
