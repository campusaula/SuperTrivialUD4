package org.pmm.SuperTrivialGame;

import org.pmm.SuperTrivialGame.R;

import android.os.Bundle;
import android.widget.TextView;

public class CreditsScreen extends BaseSuperTrivialActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creditsscreen);
		
		final TextView credits = (TextView) findViewById(R.id.tvCredits);
		credits.setText("SUPER TRIVIAL GAME\npara\nProgramaci�n Multimedia y Dispositivos M�viles\n\nAUTORES:\nN�stor Mart�nez (nestor.martinez@campusaula.com)\n\n 2013");
	}
}
