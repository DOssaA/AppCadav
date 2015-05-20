package com.example.usuario.anim;

import android.support.v7.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by Alejo Monstruo on 5/20/2015.
 */
public class AnimationUtils {

    public static void animate(RecyclerView.ViewHolder holder){
//        ObjectAnimator animator = ObjectAnimator.ofFloat(holder.itemView, "translationY",-130 ,0);
//
//        AnimatorSet animatorSet = new AnimatorSet();
//        //ObjectAnimator animator = ObjectAnimator.ofFloat(holder.itemView, "translationX",-50 ,50, -15,15, 0);
//        ObjectAnimator animator2 = ObjectAnimator.ofFloat(holder.itemView, "alpha",0f, 1f);
//        animatorSet.playTogether(animator,animator2);
//        animatorSet.setDuration(1000);
//        animatorSet.start();

        YoYo.with(Techniques.SlideInDown)
                .duration(700)
                .playOn(holder.itemView);
    }
}
