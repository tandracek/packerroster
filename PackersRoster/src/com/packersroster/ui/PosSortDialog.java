package com.packersroster.ui;

import java.util.List;

import com.example.packersroster.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class PosSortDialog extends DialogFragment {
	private CharSequence[] pos;
	private int selected;
	
	PosSortInterface listener;

	public interface PosSortInterface {
		public void onPosClickListener(CharSequence position);
	}
	
	public PosSortDialog() {
		super();
		this.pos = new CharSequence[1];
		this.selected = -1;
	}
	
	public void setPositions(List<String> pos) {
		this.pos = pos.toArray(new CharSequence[pos.size()]);
	}
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        	listener = (PosSortInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.menu_position).setSingleChoiceItems(pos, selected, 
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if(which != selected) {
							selected = which;
							listener.onPosClickListener(pos[which]);
							dismiss();
						}
					}
				});
		return builder.create();
	}
}
