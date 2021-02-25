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

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class Fragment_Curtain extends Fragment {

    State state;

    TextView test, curtain_step;
    Switch sw; //색상 스위치
    Slider sb; //밝기 슬라이더
    LinearLayout ledlayout;
    TextView ctr_state; //커튼 단계

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


        state = (State) getActivity().getApplication();



        //스위치 리스너
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

        //프래그먼트 데이터 전달


        max_down = rootView.findViewById(R.id.max_down);
        down = rootView.findViewById(R.id.down);
        up = rootView.findViewById(R.id.up);
        max_up = rootView.findViewById(R.id.max_up);

        max_down.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                state.setStep(0);


                ctr_state.setText(String.valueOf(state.getStep()) + "단계");
//
//                //프래그먼트 데이터 전달
//                String name = (String) curtain_step.getText();
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                Fragment_Curtain fragment1 = new Fragment_Curtain();  //프레그먼트끼리 rfgName넘기기 위한 bundle
//                Fragment_Main fragment2 = new Fragment_Main();
//                Bundle bundle = new Bundle();
//                bundle.putString("Name", name);
//                fragment2.setArguments(bundle);
//                // 버튼을 눌렀을 때 RE-Fr자바를 탈 수 있도록 함
//                transaction.commit(); //저장해라 commit


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

////프래그먼트 데이터 전달
//                    String name = (String) curtain_step.getText();
//                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    Fragment_Curtain fragment1 = new Fragment_Curtain();  //프레그먼트끼리 rfgName넘기기 위한 bundle
//                    Fragment_Main fragment2 = new Fragment_Main();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Name", name);
//                    fragment2.setArguments(bundle);
//                    fragment1.setArguments(bundle); //Name 변수 값 전달. 반드시 setArguments() 메소드를 사용하지 않으면, 받는 쪽에서 null 값으로 받음.
//                    // 버튼을 눌렀을 때 RE-Fr자바를 탈 수 있도록 함
//                    //transaction.replace(R.id.frame, fragment1); //프레임 레이아웃에서 프레그먼트 1로 변경(replace)해라
//                    transaction.commit(); //저장해라 commit

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




//                    //프래그먼트 데이터 전달
//                    String name = (String) curtain_step.getText();
//                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction(); //프레그먼트끼리 rfgName넘기기 위한 bundle
//                    Fragment_Main fragment2 = new Fragment_Main();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Name", name);
//                    fragment2.setArguments(bundle);
//                    transaction.commit();
//
//

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

                

//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.detach(Fragment_Main).attach(Fragment_Main).commit();

//                //프래그먼트 데이터 전달
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                Fragment_Curtain fragment1 = new Fragment_Curtain();  //프레그먼트끼리 rfgName넘기기 위한 bundle
//                Fragment_Main fragment2 = new Fragment_Main();
//                Bundle bundle = new Bundle();
//                bundle.putString("Name", name);
//                fragment2.setArguments(bundle);
//                fragment1.setArguments(bundle); //Name 변수 값 전달. 반드시 setArguments() 메소드를 사용하지 않으면, 받는 쪽에서 null 값으로 받음.
//                // 버튼을 눌렀을 때 RE-Fr자바를 탈 수 있도록 함
//                //transaction.replace(R.id.frame, fragment1); //프레임 레이아웃에서 프레그먼트 1로 변경(replace)해라
//                transaction.commit(); //저장해라 commit


                try {
                    ((MainActivity) MainActivity.mContext).mqttClient.publish("Curtain/ctr", String.valueOf(state.getStep()).getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });






        test = rootView.findViewById(R.id.test);

        try {
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
