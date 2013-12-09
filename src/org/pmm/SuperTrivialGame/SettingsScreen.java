package org.pmm.SuperTrivialGame;

import org.pmm.SuperTrivialGame.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsScreen extends BaseSuperTrivialActivity {

	final static public int QUESTIONS_PREFERENCES_DIALOG = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingscreen);
		
		final Button preferencesButton = (Button) findViewById(R.id.bQuestionsPreferences);
		preferencesButton.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(QUESTIONS_PREFERENCES_DIALOG); // Muestro el diálogo. Se invocará automáticamente al método onCreateDialog(int id) propio de la activity.
			}
		});
	}
	
	@Override
	protected Dialog onCreateDialog(int id) { // Método propio de la activity que será llamado cada vez que se llame al método showDialog(int id) también propio de la activity.
		// TODO Auto-generated method stub
		
		switch(id) {
		case QUESTIONS_PREFERENCES_DIALOG: // Ventana de diálogo tipo lista.
			
			String[] questionsType = {"Sports", "Literature", "Science", "Movies", "History"};
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.questions_preferences_text);
			builder.setMultiChoiceItems(questionsType, null, null);
			
			builder.setPositiveButton(android.R.string.ok, new OnClickListener() { // Le pongo botón positivo y lo gestiono.
				
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Toast.makeText(SettingsScreen.this, "OK", Toast.LENGTH_SHORT).show();
					final TextView tv = (TextView) findViewById(R.id.tvQuestionsPreferences);
					tv.setText(R.string.questions_preferences_set);
					SettingsScreen.this.removeDialog(QUESTIONS_PREFERENCES_DIALOG);
				}
			});
			
			builder.setNegativeButton(android.R.string.cancel, new OnClickListener() { // Le pongo botón negativo y lo gestiono.
				
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Toast.makeText(SettingsScreen.this, "CANCEL", Toast.LENGTH_SHORT).show();
					SettingsScreen.this.removeDialog(QUESTIONS_PREFERENCES_DIALOG);
				}
			});
			
			return builder.create(); // Devuelvo el diálogo recién creado.
		}
		return super.onCreateDialog(id); // Por si no entrase a alguno de los anteriores casos del switch, llamo al padre a su onCreateDialog(int id)
	}
	
}
