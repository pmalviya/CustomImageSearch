package com.example.pmalviya.customimagesearch.activities;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.pmalviya.customimagesearch.R;
import com.example.pmalviya.customimagesearch.models.SearchFilter;


public class SearchFiltersDialog extends DialogFragment {
	private Spinner spSize;
	private Integer mSize;
	private Spinner spColor;
	private Integer mColor;
	private Spinner spType;
	private Integer mType;
	private EditText etSite;
	private Button btSave;
	
	ArrayAdapter<CharSequence> aColors ;
	ArrayAdapter<CharSequence> aSizes ;
	ArrayAdapter<CharSequence> aTypes;
	
	public SearchFiltersDialog() {
		
	}

	public interface SearchFiltersDialogListener {
		void onDialogDone(SearchFilter filter);
	}

	public SearchFiltersDialogListener dListener;

	public void setDialogListener( SearchFiltersDialogListener searchFiltersDialogListener) {
		dListener = searchFiltersDialogListener;
	}

	public static SearchFiltersDialog newInstance( SearchFilter filter) {
		SearchFiltersDialog frag = new SearchFiltersDialog();
		Bundle args = new Bundle();
		args.putInt("size",filter.getSizeIndex());
		args.putInt("color", filter.getColorIndex());
		args.putInt("type", filter.getTypeIndex());
		args.putString("site", filter.getSite());
		frag.setArguments(args);
		return frag;
	}

	public void setUpClickListeners() {
		// Set the ClickListener for Spinners
		spSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> adapterView,
					View view, int i, long l) {
				mSize = i;
			}

			// If no option selected
			public void onNothingSelected(AdapterView<?> arg0) {
				mSize = 0;
			}
		});
		
		spColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> adapterView,
					View view, int i, long l) {
				mColor = i;
			}

			// If no option selected
			public void onNothingSelected(AdapterView<?> arg0) {
				mColor = 0;
			}
		});
		
		spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> adapterView,
					View view, int i, long l) {
				mType = i;
			}

			// If no option selected
			public void onNothingSelected(AdapterView<?> arg0) {
				mType = 0;
			}
		});
		
		btSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SearchFilter filter = new SearchFilter();
				filter.setColorIndex(mColor);
				filter.setColor(aColors.getItem(mColor).toString());
				
				filter.setSizeIndex(mSize);
				filter.setSize(aSizes.getItem(mSize).toString());
				
				filter.setTypeIndex(mType);
				filter.setType(aTypes.getItem(mType).toString());
				
				filter.setSite(etSite.getText().toString());
				

				dListener.onDialogDone(filter);
				getDialog().dismiss();
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_search_filter, container);
		spSize = (Spinner) view.findViewById(R.id.spSize);
		spColor = (Spinner) view.findViewById(R.id.spColor);
		spType = (Spinner) view.findViewById(R.id.spType);
		etSite = (EditText) view.findViewById(R.id.etSite);
		btSave = (Button) view.findViewById(R.id.btSave);

		getDialog().setTitle("Set Search Filters");

		 aColors = ArrayAdapter.createFromResource(getActivity(), R.array.arr_colors, android.R.layout.simple_spinner_item);
		 aSizes = ArrayAdapter.createFromResource(getActivity(), R.array.arr_sizes, android.R.layout.simple_spinner_item);
		 aTypes = ArrayAdapter.createFromResource(getActivity(), R.array.arr_types, android.R.layout.simple_spinner_item);
		
		aColors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spColor.setAdapter(aColors);
		spColor.setSelection(getArguments().getInt("color"));

		
		aSizes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spSize.setAdapter(aSizes);
		spSize.setSelection(getArguments().getInt("size"));

		
		aTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spType.setAdapter(aTypes);
		spType.setSelection(getArguments().getInt("type"));

		etSite.setText(getArguments().getString("site", ""));

		setUpClickListeners();
		
		// Show soft keyboard automatically
		spSize.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return view;
	}

}
