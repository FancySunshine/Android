package com.cookandroid.curtain;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reservation extends AppCompatActivity {
    Slider sld_step;
    TimePicker res_tp;
    TextView hope_bright, hope_color;
    EditText res_name, res_memo;
    MaterialButtonToggleGroup res_mbt;
    Button res_save, res_cancel;
    Switch sw_curtain, sw_led;
    String message = "";
    // 요일 버튼 Id

    int[] mbutton_ids = {R.id.sun, R.id.mon, R.id.tue, R.id.wed, R.id.thu, R.id.fri, R.id.sat};
    MaterialButton[] mbutton = new MaterialButton[7];
    //LinearLayout led_layout, curtain_layout;


    //LED(색상) 배열
    AppCompatButton[] color_btn = new AppCompatButton[10];
    int[] color_btn_ids = new int[]{R.id.color_btn1, R.id.color_btn2, R.id.color_btn3, R.id.color_btn4,
            R.id.color_btn5, R.id.color_btn6, R.id.color_btn7, R.id.color_btn8, R.id.color_btn9, R.id.color_btn10};
    ArrayList<String> colors = new ArrayList<>();  // Color 넣어줄 list

    //LED(희망하는 방의 밝기 버튼) 배열
    com.google.android.material.button.MaterialButton[] btn_step = new com.google.android.material.button.MaterialButton[5];
    int [] btn_step_ids = new int[] {R.id.curtain_step0, R.id.curtain_step1, R.id.curtain_step2, R.id.curtain_step3, R.id.curtain_step4};



    public static Activity activity;
    public static LinearLayout color_layout;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        setTitle("Reservation");

        activity = Reservation.this;

        res_tp = findViewById(R.id.res_tp);                // 예약 시간

        res_name = findViewById(R.id.res_name);        // 예약 이름
        res_memo = findViewById(R.id.res_memo);         // 예약 메모

        res_mbt = findViewById(R.id.res_mbt);                // 예약 요일 선택 버튼

        sld_step = findViewById(R.id.sld_step2);               // 커튼 설정 슬라이드

        res_save = findViewById(R.id.res_save);               // 예약 저장
        res_cancel = findViewById(R.id.res_cancel);          // 예약 취소

        sw_curtain = findViewById(R.id.sw_curtain);         // curtain 스위치

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


        for(int i = 0; i < mbutton_ids.length; i++){
            mbutton[i] = findViewById(mbutton_ids[i]);
        }

        sw_curtain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (sw_curtain.isChecked() == true) {
                    sld_step.setEnabled(true);
                    //curtain_layout.setBackgroundColor(Color.parseColor("#000000"));
                } else {
                    sld_step.setEnabled(false);
                    //curtain_layout.setBackgroundColor(Color.parseColor("#000000"));
                }
            }

        });



        sw_led = findViewById(R.id.sw_led);         // led 스위치

        hope_bright = findViewById(R.id.hope_bright);
        hope_color = findViewById(R.id.hope_color);

        for(int i = 0; i < color_btn.length; i++){
            color_btn[i] = findViewById(color_btn_ids[i]);
            GradientDrawable d = (GradientDrawable) color_btn[i].getBackground();
            d.setColor(Color.parseColor(colors.get(i)));
            final int finalI = i;
            color_btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getContext(), colors.get(finalI), Toast.LENGTH_SHORT).show();
                }
            });
        }

        for(int i =0; i<btn_step.length; i++) {
            btn_step[i] = findViewById(btn_step_ids[i]);
            final String btn_text = (String)btn_step[i].getText();
            btn_step[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getContext(), btn_text,Toast.LENGTH_SHORT).show();
                }
            });

        }


        sw_led.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (sw_led.isChecked() == true)
                {
                    for(int i = 0; i< btn_step.length; i++) {
                        btn_step[i].setEnabled(true);
                    }

                    for(int i = 0; i < color_btn.length; i++) {
                        color_btn[i].setEnabled(true);
                    }

                    hope_bright.setTextColor(Color.parseColor("#4D4D4D"));
                    hope_color.setTextColor(Color.parseColor("#4D4D4D"));

                }
                else
                {
                    for(int i = 0; i< btn_step.length; i++) {
                        btn_step[i].setEnabled(false);
                    }

                    for(int i = 0; i < color_btn.length; i++){
                        color_btn[i].setEnabled(false);
                    }

                    hope_bright.setTextColor(Color.parseColor("#F4CFC9C9"));
                    hope_color.setTextColor(Color.parseColor("#F4CFC9C9"));
                }
            }

        });


        sld_step.setLabelFormatter(new LabelFormatter() {        // 커튼 단계별 텍스트 표시
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                return (int) value + "단계";
            }
        });

        // MQTT SET CALLBACK
        ((MainActivity) MainActivity.mContext).mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                try{((MainActivity) MainActivity.mContext).mqttClient.connect();}catch (Exception e){}
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                // 예약이 성공적으로 저장되었을 때, RESULT_OK 코드를 Main Activity로 전달
                if (topic.equals("rsv/addres")) {
                    setResult(RESULT_OK);
                    if(message.toString().equals("success")) {
                        Toast.makeText(getApplicationContext(), "예약이 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else if(message.toString().equals("fail")){
                        Toast.makeText(getApplicationContext(), "예약을 저장하는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        /*
        try {
            ((MainActivity) MainActivity.mContext).mqttClient.subscribe("Reservation/add/success", 0);
        } catch (MqttException e) {
            e.printStackTrace();
        }

         */
        res_save.setOnClickListener(new View.OnClickListener() {       // 예약 정보 저장 이벤트
            @Override
            public void onClick(View view) {
                if (res_name.getText().toString().equals("")) {         // 예약 이름 미설정시 이벤트
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else {                   // 선택된 요일값 받아오기

                    String result = "";
                    for(int i = 0; i < mbutton.length; i++){
                        if(mbutton[i].isChecked()){
                            result += "1";
                        }
                        else{
                            result += "0";
                        }
                    }

                    message = res_name.getText() + "|" + res_tp.getCurrentHour() + ":" + res_tp.getCurrentMinute() + "|"
                            + result + "|" + String.valueOf((int) sld_step.getValue())
                            + "|" + res_memo.getText();
                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                    try {
                        ((MainActivity) MainActivity.mContext).mqttClient.publish("rsv/addreq", message.getBytes(), 0, false);

                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }
            }

        });

        res_cancel.setOnClickListener(new View.OnClickListener() {        // 예약 취소 이벤트
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

        for (int i = 0; i < color_btn.length; i++) {
            color_btn[i] = findViewById(color_btn_ids[i]);
            GradientDrawable d = (GradientDrawable) color_btn[i].getBackground();
            d.setColor(Color.parseColor(colors.get(i)));
            final int finalI = i;
            color_btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), colors.get(finalI), Toast.LENGTH_SHORT).show();
                }
            });
        }
        // intent의 Extra가 있을 때 실행
        // RecyclerAdapter.java 에서 얻은 값들을 이용
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String[] time = intent.getStringExtra("StartTime").split(":");
            String[] code = intent.getStringExtra("dayofweek").split("");
            for (int i = 0; i < code.length; i++) {
                if (code[i].equals("1")) {
                    res_mbt.check(mbutton_ids[i]);
                }
            }
            res_tp.setHour(Integer.parseInt(time[0]));
            res_tp.setMinute(Integer.parseInt(time[1]));
            res_name.setText(intent.getStringExtra("Name"));
            res_name.setEnabled(false);
            res_memo.setText(intent.getStringExtra("Memo"));
            sld_step.setValue(Integer.parseInt(intent.getStringExtra("ctr")));

        }
    }

}