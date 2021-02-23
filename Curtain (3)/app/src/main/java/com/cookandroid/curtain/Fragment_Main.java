package com.cookandroid.curtain;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Fragment_Main extends Fragment {

    State state;
    TextView ctr_state, t1;
    private String name;
    public Fragment_Main() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        ctr_state = rootView.findViewById(R.id.ctr_state);
        t1 = rootView.findViewById(R.id.curtain_step);
        state = (State)getActivity().getApplication();


//        //프래그먼트 새로고침
//        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
//        ft.detach(this).attach(this).commit();






        t1.setText(state.getStep() + "단계에에에");
        ctr_state.setText(state.getStep() + "단계에에에");

        return rootView;


    }
}


