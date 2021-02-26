package com.cookandroid.curtain;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Fragment_Auto extends Fragment {
    private LinearLayout color_layout;
    private SeekBar seekbar;
    AppCompatButton[] color_btn = new AppCompatButton[10];
    int[] color_btn_ids = new int[]{R.id.color_btn1, R.id.color_btn2, R.id.color_btn3, R.id.color_btn4,
            R.id.color_btn5, R.id.color_btn6, R.id.color_btn7, R.id.color_btn8, R.id.color_btn9, R.id.color_btn10};
    ArrayList<String> colors = new ArrayList<>();  // Color 넣어줄 list


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
        color_layout = rootView.findViewById(R.id.colorpicker_base);
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
/*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });

*/

        //final ColorPicker colorPicker = new ColorPicker(getActivity());  // ColorPicker 객체 생성





/*

        //슬라이더
        slider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                return (int)value + "%";
            }
        });
//        slider.setLabelFormatter { value: Float ->
//                val format = NumberFormat.getCurrencyInstance()
//            format.maximumFractionDigits = 0
//            format.currency = Currency.getInstance("USD")
//            format.format(value.toDouble())
//        }

        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {




            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                int value = (int) slider.getValue();


                ColorDrawable cd = (ColorDrawable) color_layout.getBackground();
                int color = cd.getColor();
                int a = Color.alpha(color);
                int R = Color.red(color);
                int B = Color.blue(color);
                int G = Color.green(color);

                if(value < 50) {
                    int r = Math.round(R * value / 100);
                    int g = Math.round(G * value / 100);
                    int b = Math.round(B * value / 100);
                    int color2 = Color.argb(a,Math.min(r,255), Math.min(g,255), Math.min(b,255));
                    color_layout.setBackgroundColor(color2);
                }
                else if(value > 50){
                    int r = Math.round(R * (value+100) / 100);
                    int g = Math.round(G * (value+100) / 100);
                    int b = Math.round(B * (value+100) / 100);
                    int color2 = Color.argb(a,Math.min(r,255), Math.min(g,255), Math.min(b,255));
                    color_layout.setBackgroundColor(color2);
                }else{
                    color_layout.setBackgroundColor(color);
                }



//                ColorDrawable cd = (ColorDrawable) color_layout.getBackground();
//                int color = cd.getColor();
//                int R = Color.red(color);
//                int B = Color.blue(color);
//                int G = Color.green(color);
//                float[] hsv_value = new float[3];
//                float[] hsv_value2 = new float[3];
//                Color.RGBToHSV(R,G,B,hsv_value);
//
//
//
//                int progress = (int) slider.getValue();
//                hsv_value2 = hsv_value;
//                hsv_value2[2] = hsv_value[2]*progress/100 + hsv_value[2];
//
//
//                int color2 = Color.HSVToColor(color,hsv_value2);
//                color_layout.setBackgroundColor(color2);
            }

        });

*/




        return rootView;

    }
/*

    public void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(getActivity());  // ColorPicker 객체 생성
        ArrayList<String> colors = new ArrayList<>();  // Color 넣어줄 list

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
        colors.add("#e6ee9c");
        colors.add("#fff59d");
        colors.add("#ffe082");
        colors.add("#ffcc80");
        colors.add("#bcaaa4");

        colorPicker.setColors(colors)  // 만들어둔 list 적용
                .setColumns(5)  // 5열로 설정
                .setRoundColorButton(true) // 원형 버튼으로 설정
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void  onChooseColor(int position, int color) {
                        color_layout.setBackgroundColor(color);

                        // OK 버튼 클릭 시 이벤트
                    }

                    @Override
                    public void onCancel() {
                        // Cancel 버튼 클릭 시 이벤트
                    }
                }).show();  // dialog 생성
    }

*/

}
