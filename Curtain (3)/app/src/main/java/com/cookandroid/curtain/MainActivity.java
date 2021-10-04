package com.cookandroid.curtain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {


    AppBarLayout mAppBar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    MqttAndroidClient mqttClient;
    BottomNavigationView navView;
    LinearLayout lay1, top_lay;
    TextView lux_avg, now_lux, textBrightness;
    MaterialButton ctr_add, ctr_sel, ctr_del, ctr_all, ctr_cancel;
    public static Context mContext;
    RecyclerView res_lv;
    RecyclerAdapter adapter;
    String id = "A12345";
    boolean all_chk = false;
    private ViewPager2 mPager;
    private FragmentStateAdapter pageAdapter;
    private int num_page = 3;
    private CircleIndicator3 mIndicator;
    MqttCallback mqttCallback;
    Fragment_Main fm;

    //MaterialButton[] curtain_steps = new MaterialButton[5];
    //int mbuttonids[] = {R.id.curtain_step0, R.id.curtain_step1, R.id.curtain_step2,
    //                   R.id.curtain_step3, R.id.curtain_step4};
    
    State state;

    /// Reservation Activity에서 종료 후 실행되는 부분(Main Activity의 예약리스트 갱신)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            try {
                mqttClient.setCallback(mqttCallback);
                mqttClient.publish("rsv/req", id.getBytes(),0, false);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BusProvider.getInstance().register(this);


        // 다른 액티비티에서 MainActivity 함수를 사용하기 위한 문맥(Context) 저장
        mContext = this;
        // XML 파일의 뷰를 가져옴.
        mAppBar = findViewById(R.id.app_bar);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        lay1 = findViewById(R.id.lay1);

        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);


        //action = findViewById(R.id.action);

        top_lay = findViewById(R.id.top_lay);


        lux_avg = findViewById(R.id.lux_avg);

        now_lux = findViewById(R.id.now_lux);
        textBrightness = findViewById(R.id.textBrightness);

        //예약 추가/삭제 버튼
        ctr_add = findViewById(R.id.ctr_add);
        ctr_sel = findViewById(R.id.ctr_sel);
        ctr_all = findViewById(R.id.ctr_all);
        ctr_del = findViewById(R.id.ctr_del);
        ctr_cancel = findViewById(R.id.ctr_cancel);

        ctr_all.setVisibility(View.GONE);
        ctr_del.setVisibility(View.GONE);
        ctr_cancel.setVisibility(View.GONE);



        res_lv = findViewById(R.id.res_lv);
        res_lv.setLayoutManager(new LinearLayoutManager(this));
        //res_lv.setNestedScrollingEnabled(true);

        state = (State) getApplication();



        fm = (Fragment_Main) getSupportFragmentManager().findFragmentById(R.id.frag_curtain);

/*
        JSONArray msg = null;
        try {
            msg = new JSONArray("[{\"Name\":\"tctc6g6\",\"StartTime\":\"18:2\",\"ctr\":1,\"dayofweek\":\"1111111\",\"Memo\":\"\",\"curtain_num\":null,\"chk_state\":0},{\"Name\":\"w818w\",\"StartTime\":\"17:53\",\"ctr\":0,\"dayofweek\":\"0000111\",\"Memo\":\"\",\"curtain_num\":null,\"chk_state\":0},{\"Name\":\"공부\",\"StartTime\":\"9:1\",\"ctr\":2,\"dayofweek\":\"1011111\",\"Memo\":\"4\",\"curtain_num\":null,\"chk_state\":0},{\"Name\":\"댜댜더더\",\"StartTime\":\"19:0\",\"ctr\":2,\"dayofweek\":\"1100101\",\"Memo\":\"너머재재재\",\"curtain_num\":null,\"chk_state\":0},{\"Name\":\"도현도\",\"StartTime\":\"20:16\",\"ctr\":2,\"dayofweek\":\"1000100\",\"Memo\":\"\",\"curtain_num\":null,\"chk_state\":0}]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new RecyclerAdapter(getApplicationContext(), msg);
        //adapter.setCount(msg.length());
        //adapter.setData(msg);
        res_lv.setAdapter(adapter);
*/
// 서버와 통신하기 위한 MQTT 클라이언트 생성
        int rnd = (int) (Math.random() * 1000);
        mqttClient = new MqttAndroidClient(this, "tcp://172.16.101.145:1883", "Android" + rnd);


        try {
            IMqttToken token = mqttClient.connect(getMqttConnectionOption());
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    mqttClient.setBufferOpts(getDisconnectedBufferOptions());    //연결에 성공한경우
                    Log.e("Connect_success", "Success");

                    try {
                        //Topic Subscribe
                        mqttClient.subscribe("rsv/list", 0);
                        mqttClient.subscribe("rsv/addres", 0);
                        mqttClient.subscribe("rsv/delres", 0);
                        mqttClient.subscribe("lux/graph", 0);
                        mqttClient.subscribe("lux/graph1", 0);
                        mqttClient.subscribe("rpi/info", 0); // 라즈베리파이 정보 얻어오기
                        mqttClient.subscribe("auto/step", 0);
                        mqttClient.publish("rsv/req", id.getBytes(),0, false);


                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    //연결에 실패한경우
                    Log.e("connect_fail", "Failure " + exception.toString());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        // mqttCallback을 다시 Set해야해서 변수로 따로 빼놨습니다.
        mqttCallback = new MqttCallback() {  //클라이언트의 콜백을 처리하는부분
            @Override
            public void connectionLost(Throwable cause) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                // Subscribe 한 Topic이 Client에 도착했을 때 콜백
                if (topic.equals("rsv/list")){
                    JSONArray msg = new JSONArray(message.toString());
                    adapter = new RecyclerAdapter(getApplicationContext(), msg);
                    //adapter.setCount(msg.length());
                    //adapter.setData(msg);
                    res_lv.setAdapter(adapter);
                }
                /*
                else if (topic.equals("Reservation/add/success")) {
                    Toast.makeText(getApplicationContext(), "예약이 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    Reservation rs = (Reservation)Reservation.activity;
                    rs.finish();
                    mqttClient.publish("client/connect", id.getBytes(),0, false);

                } */
                else if (topic.equals("test")) {
                    state.setStep(Integer.parseInt(message.toString()));
                    //BusProvider.getInstance().post(new BusEvent(state.getStep(), state.getLed(), state.getBright()));
                }
                else if (topic.equals("rsv/delres")) {
                    if(message.toString().equals("success")) {
                        Toast.makeText(getApplicationContext(), "선택한 예약이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        // 예약 리스트 갱신
                        mqttClient.publish("rsv/req", id.getBytes(), 0, false);
                    }else if(message.toString().equals("fail")) {
                        Toast.makeText(getApplicationContext(), "선택한 예약 삭제를 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (topic.equals("rsv/addres")) {
                    if(message.toString().equals("success")) {
                        Toast.makeText(getApplicationContext(), "선택한 예약이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                        // 예약 리스트 갱신
                        mqttClient.publish("rsv/req", id.getBytes(), 0, false);
                    }else if(message.toString().equals("fail")) {
                        Toast.makeText(getApplicationContext(), "선택한 예약 추가를 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                // Lux 데이터 값 받아오기
                else if(topic.equals("lux/graph")){
                    JSONArray msg = new JSONArray(message.toString());
                    int currentBrightness = Integer.parseInt(msg.getJSONObject(msg.length() -1).getString("in"));
                    //현재 방안 조도 값
                    now_lux.setText("실내 조도 : " + currentBrightness);

                    //조도값에 따른 단계 표시
                    switch(currentBrightness/200) {
                        case 0:textBrightness.setText("매우어두움"); break;
                        case 1:textBrightness.setText("어두움"); break;
                        case 2:textBrightness.setText("보통"); break;
                        case 3:textBrightness.setText("밝음"); break;
                        default:textBrightness.setText("매우밝음"); break;
                    }

                }else if(topic.equals("lux/graph1")) {
                    JSONArray msg = new JSONArray(message.toString());
                    BusProvider.getInstance().post(new BusEvent(message.toString()));

                    //Fragment_Machine fm = new Fragment_Machine();//추가
                    //fm.busStop(new BusEvent(message.toString()));//추가
                }
                else if(topic.equals("auto/step")) {
                    state.setAuto_Step(message.toString());
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        };

        // MQTT 콜백 Set
        mqttClient.setCallback(mqttCallback);
        //ViewPager2
        mPager = findViewById(R.id.viewpager);
        mPager.post(new Runnable() {
            @Override
            public void run() {
                mPager.setCurrentItem(1, true);

            }
        });
        pageAdapter = new ControlAdapter(this, num_page);
        mPager.setAdapter(pageAdapter);
        mIndicator = findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.createIndicators(num_page, 1);
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mIndicator.animatePageSelected(position);
                //BusProvider.getInstance().post(new BusEvent(state.getStep(), state.getLed(), state.getBright()));

            }
        });


/*
        for(int i = 0; i < curtain_steps.length; i++){
            curtain_steps[i] = findViewById(mbuttonids[i]);
            final int finalI = i;
            curtain_steps[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mqttClient.publish("Curtain/ctr", String.valueOf(finalI).getBytes(), 0, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        */

        // 메인 탭화면 설정(curtain)
        lay1.setVisibility(LinearLayout.VISIBLE);



        // 예약 추가 버튼을 눌렀을 때 리스너 설정
        ctr_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Reservation.class);
                // Reservation Activity가 종료되었을 때 성공적으로 저장되었다는 메세지를 받기 위해 함수 수정
                startActivityForResult(intent, 0);
            }
        });
        // 예약 삭제 버튼을 눌렀을 때 리스너 설정(추후 추가 예정)
        ctr_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctr_add.setVisibility(View.GONE);
                ctr_sel.setVisibility(View.GONE);
                ctr_all.setVisibility(View.VISIBLE);
                ctr_del.setVisibility(View.VISIBLE);
                ctr_cancel.setVisibility(View.VISIBLE);
                adapter.setCheckBoxState(true);
                Toast.makeText(getApplicationContext(), "삭제할 예약들을 선택해주세요", Toast.LENGTH_SHORT).show();
            }
        });
        ctr_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctr_add.setVisibility(View.VISIBLE);
                ctr_sel.setVisibility(View.VISIBLE);
                ctr_all.setVisibility(View.GONE);
                ctr_del.setVisibility(View.GONE);
                ctr_cancel.setVisibility(View.GONE);
                adapter.setCheckBoxState(false);
                all_chk = false;
                int count = adapter.getItemCount();

                for(int i = 0; i < count; i++){
                    CheckBox cb = res_lv.getChildAt(i).findViewById(R.id.res_chk);
                    cb.setChecked(false);
                }

            }
        });
        ctr_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 모두 선택 클릭시 모두 선택하거나 모두 해제
                int count = adapter.getItemCount();

                if(!all_chk){
                    for(int i = 0; i < count; i++) {
                        CheckBox cb = res_lv.getChildAt(i).findViewById(R.id.res_chk);
                        cb.setChecked(true);
                        all_chk = true;
                    }

                }
                else{
                    for(int i = 0; i < count; i++) {
                        CheckBox cb = res_lv.getChildAt(i).findViewById(R.id.res_chk);
                        cb.setChecked(false);
                        all_chk = false;
                    }
                }
            }
        });
        ctr_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("데이터 삭제").setMessage("선택된 항목의 데이터를 삭제하시겠습니까?");
                // 삭제 버튼
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        int count = adapter.getItemCount();
                        ArrayList<String> result = new ArrayList<String>();
                        for(int i = 0; i < count; i++){
                            CheckBox cb = res_lv.getChildAt(i).findViewById(R.id.res_chk);
                            if(cb.isChecked()){
                                TextView tv = res_lv.getChildAt(i).findViewById(R.id.res_name);
                                result.add("'" + tv.getText().toString() + "'");
                            }
                        }
                        String del = result.toString().replace("[", "").replace("]", "");
                        try {
                            mqttClient.publish("rsv/delreq", del.getBytes(), 0, false);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                });

                // 취소 버튼
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override public void onShow(DialogInterface arg0)
                    {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                    }
                });
                dialog.show();    // 알림창 띄우기

            }
        });





    }


    // MQTT 연결 설정
    private DisconnectedBufferOptions getDisconnectedBufferOptions() {
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(true);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;
    }
    private MqttConnectOptions getMqttConnectionOption() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setConnectionTimeout(300);
        mqttConnectOptions.setKeepAliveInterval(1000);
        return mqttConnectOptions;
    }
    /*
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        try {
            mqttClient.unregisterResources();
            mqttClient.disconnect();

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

     */


    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);

    }

}
