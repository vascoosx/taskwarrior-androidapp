package org.svij.taskwarriorapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class TaskAddActivity extends Activity {
	private TaskDataSource datasource;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_add);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		TextView tvDueDate = (TextView) findViewById(R.id.tvDueDate);
		tvDueDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerFragment date = new DatePickerFragment();
				date.setCallBack(onDate);
				date.show(getFragmentManager(), "date_dialog");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_task_add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View view) {
		datasource = new TaskDataSource(this);
		datasource.open();

		EditText etTaskAdd = (EditText) findViewById(R.id.etTaskAdd);
		TextView etTaskDate = (TextView) findViewById(R.id.tvDueDate);
		datasource.createTask(etTaskAdd.getText().toString(), etTaskDate
				.getText().toString(), "pending");
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	public void showDatePickerDialog(View view) {
		DatePickerFragment date = new DatePickerFragment();
		date.setCallBack(onDate);
		date.show(getFragmentManager(), "Date Picker");
	}

	@Override
	protected void onPause() {
		try {
			datasource.close();
		} catch (Exception e) {
		} finally {
			super.onPause();
		}
	}

	OnDateSetListener onDate = new OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			String date_rawstring = String.valueOf(year) + "-"
					+ String.valueOf(monthOfYear + 1) + "-"
					+ String.valueOf(dayOfMonth);

			try {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date date = (Date) formatter.parse(date_rawstring);
				String date_string = formatter.format(date);
				TextView etTaskDate = (TextView) findViewById(R.id.tvDueDate);
				etTaskDate.setText(date_string);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

	};
}
