package com.cookandroid.curtain;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.cookandroid.curtain.BusEvent.*;


public class Fragment_Curtain extends Fragment {


    TextView curtain_step;
    Slider b_slider; //밝기 슬라이더
    TextView ctr_state; //커튼 단계
    LinearLayout auto_layout; // 자동 제어 데이터 전달 화면
    State state;

    MaterialButton[] curtain_steps = new MaterialButton[5];
    MaterialButton max_down, down, up, max_up;

    AppCompatButton[] color_btn = new AppCompatButton[10];
    int[] color_btn_ids = new int[]{R.id.color_btn1, R.id.color_btn2, R.id.color_btn3, R.id.color_btn4,
            R.id.color_btn5, R.id.color_btn6, R.id.color_btn7, R.id.color_btn8, R.id.color_btn9, R.id.color_btn10};
    ArrayList<String> colors = new ArrayList<>();  // Color 넣어줄 list

    Map<String, String> map = new HashMap<String, String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_curtain, container, false);
        b_slider = rootView.findViewById(R.id.slider);


        b_slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                int i = (int) slider.getValue();
                state.setBright(i);
                BusProvider.getInstance().post(new BusEvent
                        (state.getStep(), state.getLed(), state.getBright(), state.getAuto_Step(), state.getAuto_Led()));

                 try {
<<<<<<< HEAD
                    ((MainActivity) MainActivity.mContext).mqttClient.publish("led/bright", String.valueOf(i).getBytes(), 0, false);
=======
                    ((MainActivity) MainActivity.mContext).mqttClient.publish("LED/bright", String.valueOf(i).getBytes(), 0, false);
>>>>>>> 1721db36e611737a4317677196847e940a867691
                } catch (MqttException e) {
                    e.printStackTrace();
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

        // state definition
        state = (State) getActivity().getApplication();

        /// led Buttons Set Color
        for(int i = 0; i < color_btn.length; i++){
            color_btn[i] = rootView.findViewById(color_btn_ids[i]);
            GradientDrawable d = (GradientDrawable) color_btn[i].getBackground();
            d.setColor(Color.parseColor(colors.get(i)));
        }
        /// led Buttons Click Event
        for (AppCompatButton appCompatButton : color_btn) {
            appCompatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < color_btn.length; i++) {
                            if(view.getId() == color_btn[i].getId()){
                                GradientDrawable d = (GradientDrawable) color_btn[i].getBackground();
                                // 테두리 지정
                                d.setStroke(5, Color.parseColor("#FFFFFF"));
                                state.setLed(colors.get(i));

                                BusProvider.getInstance().post(new BusEvent
                                        (state.getStep(), state.getLed(), state.getBright(), state.getAuto_Step(), state.getAuto_Led()));


                                ArrayList<Integer> rgb = new ArrayList<Integer>();
                                String result = "";
                                for(int j = 1; j <= 3; j++){
                                    rgb.add(Integer.parseInt(colors.get(i).substring(2 * j - 1, 2 * j + 1), 16));
                                }
                                for(int k = 0; k < rgb.size(); k++){
                                    result += String.valueOf(rgb.get(k));
                                    if(k < rgb.size() - 1){
                                        result += "|";
                                    }

                                }
                                try {
                                    ((MainActivity) MainActivity.mContext).mqttClient.publish("led/color", result.getBytes(), 0, false);
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();

                                Toast.makeText(getContext(), colors.get(i), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                GradientDrawable d = (GradientDrawable) color_btn[i].getBackground();
                                // 테두리 안보이게
                                d.setStroke(0, Color.parseColor("#FFFFFF"));
                            }
                    }

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
                state.setStep(0);

                curtain_step.setText(String.valueOf(state.getStep()) + "단계");
                BusProvider.getInstance().post(new BusEvent
                        (state.getStep(), state.getLed(), state.getBright(), state.getAuto_Step(), state.getAuto_Led()));


                try {
                    ((MainActivity) MainActivity.mContext).mqttClient.publish("ctn/step", String.valueOf(state.getStep()).getBytes(), 0, false);
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
                    BusProvider.getInstance().post(new BusEvent
                            (state.getStep(), state.getLed(), state.getBright(), state.getAuto_Step(), state.getAuto_Led()));


                    try {
                        ((MainActivity) MainActivity.mContext).mqttClient.publish("ctn/step", String.valueOf(state.getStep()).getBytes(), 0, false);
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
                    BusProvider.getInstance().post(new BusEvent
                            (state.getStep(), state.getLed(), state.getBright(), state.getAuto_Step(), state.getAuto_Led()));



                    try {
                        ((MainActivity) MainActivity.mContext).mqttClient.publish("ctn/step", String.valueOf(state.getStep()).getBytes(), 0, false);
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
                BusProvider.getInstance().post(new BusEvent
                        (state.getStep(), state.getLed(), state.getBright(), state.getAuto_Step(), state.getAuto_Led()));


                try {
                    ((MainActivity) MainActivity.mContext).mqttClient.publish("ctn/step", String.valueOf(state.getStep()).getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });



        return rootView;
    }

    @Subscribe
    public void busStop(BusEvent busEvent) {
        curtain_step.setText(busEvent.curtain + "단계");

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);

    }

}
