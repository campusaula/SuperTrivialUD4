package org.pmm.SuperTrivialGame;

import org.pmm.SuperTrivialGame.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends BaseSuperTrivialActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splashscreen);
		
		final ImageView question1 = (ImageView) findViewById(R.id.ivQuestion1); 
		Animation question1Animation = AnimationUtils.loadAnimation(this, R.anim.question1animation);
		question1.setAnimation(question1Animation); // Cargo la animación del interrogante de arriba en un ImageView

		final ImageView question2 = (ImageView) findViewById(R.id.ivQuestion2);
		Animation question2Animation = AnimationUtils.loadAnimation(this, R.anim.question2animation);
		question2.setAnimation(question2Animation); // Cargo la animación del interrogante de abajo en un ImageView
		
		final TextView title = (TextView) findViewById(R.id.tvSplashTitle);
		Animation titleAnimation = AnimationUtils.loadAnimation(this, R.anim.splashtitleanimation);
		titleAnimation.setAnimationListener(new AnimationListener() { // Cargo la animación del título del centro en un TextView
			
			
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			/*
			 * Cuando acabe la animación del texto del medio (es la animación más larga) paso a la actividad MainScreenActivity 
			 * y mato esta actividad de animación para que no pueda volver a ella si pulsan el botón back.
			 */
			public void onAnimationEnd(Animation animation) { // Cuando acabe la animación del texto del medio (es la animación más larga) paso a la actividad MainScreenActivity y mato esta actividad de animación para que no pueda volver a ella si pulsan el botón back.
				// TODO Auto-generated method stub
				startActivity(new Intent(SplashScreen.this, MainScreenActivity.class));
				SplashScreen.this.finish();
			}
		});
		title.setAnimation(titleAnimation);
		
	}
	
}
