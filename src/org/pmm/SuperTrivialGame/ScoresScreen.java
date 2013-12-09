package org.pmm.SuperTrivialGame;

import org.pmm.SuperTrivialGame.R;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ScoresScreen extends BaseSuperTrivialActivity {
	
	static final int HEADER_ROW = 0;
	static final int CONTENT_ROW = 1;
	
	static final String LOCAL_USER_TAB = "local_user";
	static final String USER_FRIENDS_TAB = "user_friends";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scoresscreen);
		
		TabHost host = (TabHost) findViewById(R.id.thScores);
		host.setup(); // Inicializa el TabHost
		
		TabSpec spec = host.newTabSpec(LOCAL_USER_TAB); // Crea una nueva pestaña
		//spec.setIndicator(getLayoutInflater().inflate(R.layout.localusertab, null)); // Creo el contenido de la pestaña a partir de un Layout que he hecho adrede para la pestaña.
		spec.setIndicator("Local User", getResources().getDrawable(R.drawable.usericon)); // Otra forma de hacerlo, diferente a la anterior.
		spec.setContent(R.id.svLocalUser); // Le indico el contenido de dicha pestaña. Le puedo indicar un Layout o una Actividad. En este caso le indico un ScrollView, sustituyendo a un Layout. Dicho ScrollView está definido en el Layout.
		host.addTab(spec);
		
		spec = host.newTabSpec(USER_FRIENDS_TAB);
		spec.setIndicator(getLayoutInflater().inflate(R.layout.userfriendstab, null));
		spec.setContent(R.id.svUserFriends);
		host.addTab(spec);
		
		host.setCurrentTabByTag(LOCAL_USER_TAB);
		
		TableLayout table = (TableLayout) findViewById(R.id.tlLocalUser); // Esta tabla está definida dentro del ScrollView en el Layout de Scores.
		addRowToTable(table, HEADER_ROW, getResources().getString(R.string.username_header), getResources().getString(R.string.score_header), getResources().getString(R.string.ranking_header));
		addRowToTable(table, CONTENT_ROW, "nesmaba", "100", "1");
		addRowToTable(table, CONTENT_ROW, "belen", "50", "2");
		
		table = (TableLayout) findViewById(R.id.tlUserFriends);
		addRowToTable(table, HEADER_ROW, getResources().getString(R.string.username_header), getResources().getString(R.string.score_header), getResources().getString(R.string.ranking_header));
		addRowToTable(table, CONTENT_ROW, "javi", "25", "3");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.scoresoptionsmenu, menu);
    	return true;	
    }
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
    	TabHost host = (TabHost) findViewById(R.id.thScores);
    	if (host.getCurrentTabTag() == USER_FRIENDS_TAB) {
    		menu.findItem(R.id.options_delete).setEnabled(false);
    		menu.findItem(R.id.options_delete).setVisible(false);
    	}
    	else {
    		menu.findItem(R.id.options_delete).setEnabled(true);
    		menu.findItem(R.id.options_delete).setVisible(true);
    	}
    	return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.options_delete) {
			TableLayout table = (TableLayout) findViewById(R.id.tlLocalUser);
			table.removeAllViews(); // Elimino todas las filas de la tabla. Esto me eliminará también las filas cabecera.
			// Añado las filas cabecera de la tabla.
			addRowToTable(table, HEADER_ROW, getResources().getString(R.string.username_header), getResources().getString(R.string.score_header), getResources().getString(R.string.ranking_header));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void addRowToTable(TableLayout table, int type, String user, String score, String ranking) { // Método hecho por mi.
		TableRow row = new TableRow(this);
		addViewToRow(row, type, user);
		addViewToRow(row, type, score);
		addViewToRow(row, type, ranking);
		table.addView(row);
		return; // Lo mismo que si no pongo nada.
	}
	
	private void addViewToRow(TableRow row, int type, String text) { // Método hecho por mi.
		TextView tv = new TextView(this); // Creo un TextView que contendrá los datos que le pase y que lo añadirá a la tabla.
		if (type == HEADER_ROW) { // Si la fila a insertar es de cabecera.
			tv.setTextColor(getResources().getColor(R.color.red));
			tv.setTextSize(getResources().getDimension(R.dimen.TableHeader));
			tv.setTypeface(Typeface.DEFAULT_BOLD);
		}
		else if (type == CONTENT_ROW) { // Si la fila a insertar es de contenido.
			tv.setTextColor(getResources().getColor(R.color.firebrick));
			tv.setTextSize(getResources().getDimension(R.dimen.TableContent));
			tv.setTypeface(Typeface.DEFAULT_BOLD);
		}
		tv.setText(text);
		row.addView(tv); // Añado el TextView que acabo de crear y configurar a la fila de la tabla.
		return; // Lo mismo que si no pongo nada.
	}

}
