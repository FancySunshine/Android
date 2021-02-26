package com.cookandroid.curtain;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import com.google.android.material.slider.Slider.OnChangeListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;

import petrov.kristiyan.colorpicker.ColorPicker;



public class Fragment_LED extends Fragment {

    private LinearLayout color_layout;
    //private LinearLayout curtainled;
    private SeekBar seekbar;
    private Switch aSwitch;
    private LinearLayout ledlayout;
    AppCompatButton[] color_btn = new AppCompatButton[10];
    int[] color_btn_ids = new int[]{R.id.color_btn1, R.id.color_btn2, R.id.color_btn3, R.id.color_btn4,
            R.id.color_btn5, R.id.color_btn6, R.id.color_btn7, R.id.color_btn8, R.id.color_btn9, R.id.color_btn10};
    ArrayList<String> colors = new ArrayList<>();  // Color 넣어줄 list


    public Fragment_LED() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_led, container, false);
        color_layout = rootView.findViewById(R.id.colorpicker_base);
        aSwitch = rootView.findViewById(R.id.aswitch);
        ledlayout = rootView.findViewById(R.id.Led_layout);
        //curtainled = rootView.findViewById(R.id.CurtainLed);

        //final Drawable backcolor = curtainled.getBackground();
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

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (aSwitch.isChecked() == true){
                    ledlayout.setVisibility(rootView.VISIBLE);
                    for(int i = 0; i < color_btn.length; i++){
                        color_btn[i].setEnabled(true);
                    }


                    seekbar.setEnabled(true);

                }else{
                    for(int i = 0; i < color_btn.length; i++){
                        color_btn[i].setEnabled(false);
                    }
                    ledlayout.setBackgroundColor(Color.parseColor("#FFcbFF"));
                    seekbar.setEnabled(false);


                }
            }
        });





        return rootView;

    }

}
