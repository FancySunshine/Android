package com.cookandroid.curtain;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class Fragment_Auto extends Fragment {
    private LinearLayout color_layout;
    private SeekBar seekbar;

    AppCompatButton[] color_btn = new AppCompatButton[10];
    int[] color_btn_ids = new int[]{R.id.color_btn1, R.id.color_btn2, R.id.color_btn3, R.id.color_btn4,
            R.id.color_btn5, R.id.color_btn6, R.id.color_btn7, R.id.color_btn8, R.id.color_btn9, R.id.color_btn10};
    ArrayList<String> colors = new ArrayList<>();  // Color 넣어줄 list


    com.google.android.material.button.MaterialButton[] btn_step = new com.google.android.material.button.MaterialButton[5];
    int [] btn_step_ids = new int[] {R.id.curtain_step0, R.id.curtain_step1, R.id.curtain_step2, R.id.curtain_step3, R.id.curtain_step4};


    int offColor;
    //int offColor = (this).getResource().getColor(R.color.offColor);
    TextView hope_bright, hope_color;
    Switch auto_sw;

    public Fragment_Auto() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_auto, container, false);


        hope_bright = rootView.findViewById(R.id.hope_bright);
        hope_color = rootView.findViewById(R.id.hope_color);


        auto_sw = rootView.findViewById(R.id.auto_sw);
        color_layout = rootView.findViewById(R.id.colorpicker_base);

        offColor = (this).getResources().getColor(R.color.offColor);

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

        for(int i =0; i<btn_step.length; i++) {
            btn_step[i] = rootView.findViewById(btn_step_ids[i]);
            final String btn_text = (String)btn_step[i].getText();
            btn_step[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), btn_text,Toast.LENGTH_SHORT).show();
                }
            });

        }


        //스위치 리스너
        auto_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (auto_sw.isChecked() == true)
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
                            btn_step[i].setPressed(false);
                        }

                        for(int i = 0; i < color_btn.length; i++){
                        color_btn[i].setEnabled(false);
                        color_btn[i].setPressed(false);
                        }

                        hope_bright.setTextColor(Color.parseColor("#F4CFC9C9"));
                        hope_color.setTextColor(Color.parseColor("#F4CFC9C9"));
                    }
            }


        });

        return rootView;

    }

}
