package org.pmm.SuperTrivialGame;

import org.pmm.SuperTrivialGame.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainScreenActivity extends BaseSuperTrivialActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);

        final String[] array = {
        		getResources().getString(R.string.main_menu_list_play),
        		getResources().getString(R.string.main_menu_list_scores),
        		getResources().getString(R.string.main_menu_list_credits)
        };
        final ListView mainMenuList = (ListView) findViewById(R.id.lvMainMenu);
        mainMenuList.setAdapter(new ArrayAdapter<String>(MainScreenActivity.this, R.layout.menu_item, array));
        mainMenuList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				final String itemText = ((TextView) arg1).getText().toString(); 
				if (itemText.equalsIgnoreCase(getResources().getString(R.string.main_menu_list_play))) {
					startActivity(new Intent(MainScreenActivity.this, PlayScreen.class));
				}
				else if (itemText.equalsIgnoreCase(getResources().getString(R.string.main_menu_list_scores))) {
					startActivity(new Intent(MainScreenActivity.this, ScoresScreen.class));
				}
				else if (itemText.equalsIgnoreCase(getResources().getString(R.string.main_menu_list_credits))) {
					startActivity(new Intent(MainScreenActivity.this, CreditsScreen.class));
				}
			}
		});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.mainoptionsmenu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	switch(item.getItemId()) {
	    	case R.id.options_settings:
	    		startActivity(new Intent(MainScreenActivity.this, SettingsScreen.class));
	    		return true;
	    	case R.id.options_help:
	    		startActivity(new Intent(MainScreenActivity.this, HelpScreen.class));
	    		return true;
    	}
    	return super.onOptionsItemSelected(item);
    }
}