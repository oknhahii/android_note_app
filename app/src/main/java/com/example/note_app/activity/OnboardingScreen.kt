package com.example.note_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.note_app.R
import com.example.note_app.adapters.OnboardingItemAdapter
import com.example.note_app.databinding.ActivityAddScreenBinding
import com.example.note_app.databinding.ActivityOnboardingScreenBinding
import com.example.note_app.models.onboardingItem
import com.google.android.material.button.MaterialButton

private lateinit var binding: ActivityOnboardingScreenBinding
class onboardingScreen : AppCompatActivity() {
    private  lateinit var onboardingItemAdapter: OnboardingItemAdapter;
    private lateinit var indicatorContainer : LinearLayout;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_screen)
//      hide bar
        supportActionBar?.hide();

        binding = ActivityOnboardingScreenBinding.inflate(layoutInflater);
        setContentView(binding.root);

        setOnboardingItems();
        setupIndicators();
        setCurrentIndicaoter(0);
    }
    private fun setOnboardingItems(){
        onboardingItemAdapter = OnboardingItemAdapter(
            listOf(
                onboardingItem(
                    onboardingImage = R.drawable.add,
                    title = "Thêm ghi chú",
                    desc = "Nhấn vào dấu cộng phía bên phải góc dưới màn hình. Đê thêm mới ghi chú"
                ),
                onboardingItem(
                    onboardingImage = R.drawable.delete,
                    title = "Xóa ghi chú",
                    desc = "Nhấn dữ tại ghi chú cần xóa. Khi form xác nhận hiện ra hãy xác nhận thao tác."
                ),
                onboardingItem(
                    onboardingImage = R.drawable.menu,
                    title = "Menu cập nhận và chỉnh sửa",
                    desc = "Nhấn vào ghi chú một menu sẽ hiện ra và bạn sẽ chọn chỉnh sửa hoặc cập nhật."
                )
            )
        )
        val onboardingViewPager = findViewById<ViewPager2>(R.id.onbroadingViewPager);
        onboardingViewPager.adapter = onboardingItemAdapter;

        onboardingViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position);
                    setCurrentIndicaoter(position);
                }
            }
        )
        (onboardingViewPager.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
        findViewById<ImageView>(R.id.imageNext).setOnClickListener{
            if(onboardingViewPager.currentItem + 1 < onboardingItemAdapter.itemCount)  onboardingViewPager.currentItem += 1;
            else navigateToHome();
        }
        findViewById<TextView>(R.id.textSkip).setOnClickListener{
            navigateToHome();
        }
        findViewById<MaterialButton>(R.id.buttonGetStarted).setOnClickListener{
            navigateToHome();
        }
    }

    private fun navigateToHome(){
        startActivity(Intent(applicationContext,MainActivity::class.java));
        finish();
    }

    private  fun  setupIndicators(){
        indicatorContainer = findViewById(R.id.indicatorsContainer);
        val indicators = arrayOfNulls<ImageView>(onboardingItemAdapter.itemCount);
        val layoutParams : LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
        layoutParams.setMargins(8,0,8,0);
        for(i in indicators.indices){
            indicators[i] = ImageView(applicationContext);
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_backgroud
                    )
                )
                it.layoutParams = layoutParams;
                indicatorContainer.addView(it)
            }
        }
    }

    private fun setCurrentIndicaoter(position: Int){
        val childCount = indicatorContainer.childCount;
        for(i in 0 until childCount){
            val imageView = indicatorContainer.getChildAt(i) as ImageView;
            if(i == position){
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active_backgroud
                    )
                )
            }
            else{
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_backgroud
                    )
                )
            }
        }
    }
}