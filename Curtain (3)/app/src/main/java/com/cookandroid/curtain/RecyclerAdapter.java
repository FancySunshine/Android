package com.cookandroid.curtain;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {   // 예약리스트

    int count = 0;
    Context c;
    private JSONArray arr;
    private boolean mCheckBoxState = false;

    public void setCheckBoxState(boolean pState){   //예약 삭제 체크박스
        mCheckBoxState = pState;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView res_name;  // 예약 이름
        TextView res_step;  // 예약 상태
        TextView res_time;  // 예약 시간
        TextView res_day;    // 예약 요일
        Switch res_sch;
        CheckBox res_chk;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            res_name = itemView.findViewById(R.id.res_name);  // 예약 이름
            res_step = itemView.findViewById(R.id.res_step);  // 예약 상태
            res_time = itemView.findViewById(R.id.res_time);  // 예약 시간
            res_day = itemView.findViewById(R.id.res_day);    // 예약 요일
            res_sch = itemView.findViewById(R.id.res_sch);
            res_chk = itemView.findViewById(R.id.res_chk);
        }
    }
    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.res_list, parent, false);

        return new RecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter.ViewHolder holder, int position) {
        try {
            c = holder.itemView.getContext();

            JSONObject data = arr.getJSONObject(position);
            holder.res_name.setText(data.getString("Name"));
            holder.res_step.setText(data.getString("ctr") + "단계");
            holder.res_time.setText(data.getString("StartTime"));

            holder.res_sch.setChecked(data.getString("chk_state").equals("1"));

            SpannableStringBuilder span_s = new SpannableStringBuilder("일월화수목금토");
            String res_day = data.getString("dayofweek");
            for(int i = 0; i < span_s.length(); i++){
                if(res_day.charAt(i) == '1'){
                    span_s.setSpan(new ForegroundColorSpan(Color.RED), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            holder.res_sch.setOnCheckedChangeListener(new OCHL_with_pos(data) {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    try {
                        if(b) {
                            ((MainActivity) MainActivity.mContext).mqttClient.publish("Reservation/check",
                                    (index.getString("Name") + "|1").getBytes(), 0, false);
                        }
                        else{
                            ((MainActivity) MainActivity.mContext).mqttClient.publish("Reservation/check",
                                    (index.getString("Name") + "|0").getBytes(), 0, false);
                        }
                    } catch (MqttException|JSONException e) {
                        e.printStackTrace();
                    }
                }

            });




            holder.res_day.setText(span_s);

            // 예약리스트들의 데이터를 이용하여 Reservation Activity 실행(예약리스트 수정)
            // 커스텀 OnClick Listener를 만들어 JSONObject(data) 값을 OnClick Listener에 전달
            holder.itemView.setOnClickListener(new OCL_with_pos(data) {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(c, Reservation.class);
                        intent.putExtra("Name", index.getString("Name"));
                        intent.putExtra("StartTime", index.getString("StartTime"));
                        intent.putExtra("ctr", index.getString("ctr"));
                        intent.putExtra("dayofweek", index.getString("dayofweek"));
                        intent.putExtra("Memo", index.getString("Memo"));
                        // Main Activity의 함수를 이용하여 성공적으로 저장했다는 코드가 오면 예약리스트 갱신
                        ((MainActivity) MainActivity.mContext).startActivityForResult(intent, 0);
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if( mCheckBoxState ){
            holder.res_chk.setVisibility(View.VISIBLE);

        }
        else {
            holder.res_chk.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        //int i = 0;
        //if(count != 0){
        //    i = count;
        //}
        return arr.length();
    }

    RecyclerAdapter(Context c, JSONArray list){
        this.c = c;
        arr = list;
    }
    /*
    public void setData(JSONArray data) throws JSONException {
        for(int i = 0; i < data.length(); i++){
            arr.set(i, data.getJSONObject(i).toString());
        }
    }

     */

    public void setCount(int count){
        this.count = count;
        //arr = new String[this.count];
    }
    /*
    @Override
    public int getCount() {
        int i = 0;
        if(count != 0){
            i = count;
        }
        return i;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.res_list, viewGroup, false);
        }

        TextView res_name = view.findViewById(R.id.res_name);  // 예약 이름
        TextView res_step = view.findViewById(R.id.res_step);  // 예약 상태
        TextView res_time = view.findViewById(R.id.res_time);  // 예약 시간
        TextView res_day = view.findViewById(R.id.res_day);    // 예약 요일
        TextView res_chk = view.findViewById(R.id.res_chk);    // 예약 삭제 체크박스

        try {
            JSONObject data = new JSONObject(arr[i]);
            res_name.setText(data.getString("Name"));
            res_step.setText(data.getString("ctr") + "단계");
            res_time.setText(data.getString("StartTime"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if( mCheckBoxState ){
            res_chk.setVisibility(View.VISIBLE);

        }
        else {
            res_chk.setVisibility(View.INVISIBLE);
        }

        return view;

    }

*/
    // 커스텀 OnClick Listener 클래스 추가
    public abstract class OCL_with_pos implements View.OnClickListener {
        protected JSONObject index;
        public OCL_with_pos(JSONObject index) {
            this.index = index;
        }
    }
    public abstract class OCHL_with_pos implements CompoundButton.OnCheckedChangeListener{
        protected JSONObject index;
        public OCHL_with_pos(JSONObject index){this.index = index;}
    }

}