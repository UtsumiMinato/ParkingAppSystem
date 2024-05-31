package com.example.parkingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentFragment extends Fragment {
    private ViewPager viewPager;
    private TextCarouselAdapter textCarouselAdapter;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (viewPager != null) {
                int itemCount = textCarouselAdapter.getCount();
                if (itemCount == 0) return;
                int nextItem = (viewPager.getCurrentItem() + 1) % itemCount;
                viewPager.setCurrentItem(nextItem, true);
                handler.postDelayed(this, 3000); // Schedule next swipe in 3 seconds
            }
        }
    };

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentFragment newInstance(String param1, String param2) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        viewPager = view.findViewById(R.id.text_carousel_viewpager);
        initializeViewPager();
        return view;
    }

    private void initializeViewPager() {
        String[] carouselTexts = {"AD", "AD2", "AD3"}; // Customize with your text content
        int backgroundColor = getResources().getColor(R.color.silver);
        int textColor = getResources().getColor(R.color.white);
        textCarouselAdapter = new TextCarouselAdapter(getContext(), carouselTexts, backgroundColor, textColor);
        viewPager.setAdapter(textCarouselAdapter);
    }
    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000); // Start auto-swiping
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); // Stop auto-swiping
    }
}