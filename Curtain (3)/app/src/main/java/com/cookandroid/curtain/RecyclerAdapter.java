package com.cookandroid.curtain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        TextView res_chk;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            res_name = itemView.findViewById(R.id.res_name);  // 예약 이름
            res_step = itemView.findViewById(R.id.res_step);  // 예약 상태
            res_time = itemView.findViewById(R.id.res_time);  // 예약 시간
            res_day = itemView.findViewById(R.id.res_day);    // 예약 요일
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
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        try {
            JSONObject data = arr.getJSONObject(position);
            holder.res_name.setText(data.getString("Name"));
            holder.res_step.setText(data.getString("ctr") + "단계");
            holder.res_time.setText(data.getString("StartTime"));
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
}