package com.sandves.help;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

public class Clipboard extends DialogFragment {
	
	private String textToCopy;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		textToCopy = getArguments().getString("textToCopy");
		return new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
		.setTitle(R.string.copy_to_clipboard)
		.setMessage(this.textToCopy)
		.setPositiveButton(R.string.copy, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE); 
				ClipData clip = ClipData.newPlainText("Latitude/Longtitude", textToCopy);
				clipboard.setPrimaryClip(clip);
				Toast.makeText(getActivity(), R.string.success_copy, Toast.LENGTH_LONG).show();
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getDialog().dismiss();
			}
		})
		.create();
	}
}
