package com.packersroster.ui;

import com.example.packersroster.R;

import android.content.Context;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

public class SwitchActionProvider extends ActionProvider {

	private Context context;
	public SwitchActionProvider(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View onCreateActionView() {
	    // Inflate the action view to be shown on the action bar.
	    LayoutInflater layoutInflater = LayoutInflater.from(context);
	    View view = layoutInflater.inflate(R.layout.action_switch, null);
	    
	    ImageButton button = (ImageButton) view.findViewById(R.id.switchBtn);
	    button.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            PopupMenu popup = new PopupMenu(context, v);
	            MenuInflater inflater = popup.getMenuInflater();
	            inflater.inflate(R.menu.swithmenu, popup.getMenu());
	            popup.show();
	        }
	    });

	    return view;
	}

	
}
