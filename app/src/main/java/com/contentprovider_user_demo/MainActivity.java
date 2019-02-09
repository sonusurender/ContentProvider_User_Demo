package com.contentprovider_user_demo;

import java.util.Random;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
		LoaderManager.LoaderCallbacks<Cursor>, OnClickListener {
	private static TextView resultView;
	private static EditText id;
	private static Button retrieve, update, delete, insert;

	private static CursorLoader cursorLoader;

	private static final String uri = "content://com.contentprovider_demo.Custom_ContentProvider/androhub";// Content
																											// URI

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Find Ids
		resultView = (TextView) findViewById(R.id.result);
		id = (EditText) findViewById(R.id.id);
		retrieve = (Button) findViewById(R.id.btnRetrieve);
		update = (Button) findViewById(R.id.btnUpdate);
		delete = (Button) findViewById(R.id.btnDelete);
		insert = (Button) findViewById(R.id.btnInsert);

		// Implement click listener
		retrieve.setOnClickListener(this);
		update.setOnClickListener(this);
		delete.setOnClickListener(this);
		insert.setOnClickListener(this);

	}

	// On click call representative methods
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRetrieve:
			onClickDisplayData();// Fetch data
			break;
		case R.id.btnUpdate:

			// Update data according to Id
			String getId = id.getText().toString();
			if (!getId.equals("") && getId.length() != 0)
				updateData(new String[] { getId });
			else
				Toast.makeText(MainActivity.this, "Id is empty.",
						Toast.LENGTH_SHORT).show();

			break;

		case R.id.btnDelete:

			// Delete data according to Id
			String getId_ = id.getText().toString();
			if (!getId_.equals("") && getId_.length() != 0)
				deleteData(new String[] { getId_ });
			else
				Toast.makeText(MainActivity.this, "Id is empty.",
						Toast.LENGTH_SHORT).show();

			break;
		case R.id.btnInsert:

			// Insert data
			insertData();
			break;

		}

	}

	private void onClickDisplayData() {
		// Initate loader manager
		getSupportLoaderManager().initLoader(1, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		try {
			cursorLoader = new CursorLoader(this, Uri.parse(uri), null, null,
					null, null);// Get cursor loader from URI
		} catch (NullPointerException e) {
			// if exception occurs toast occurs
			Toast.makeText(
					MainActivity.this,
					"There is no app found corresponding to your content provider.",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(MainActivity.this,
					"Error in fetching data. Try again.", Toast.LENGTH_SHORT)
					.show();
			e.printStackTrace();
		}
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {

		// First check if cursor is not null
		if (cursor != null && cursor.moveToFirst()) {

			StringBuilder result = new StringBuilder();

			// Now loop to all items and append it to string builder
			while (!cursor.isAfterLast()) {
				result.append("Id - "
						+ cursor.getString(cursor.getColumnIndex("id"))
						+ "\nName - "
						+ cursor.getString(cursor.getColumnIndex("name"))
						+ "\nEmail - "
						+ cursor.getString(cursor.getColumnIndex("email"))
						+ "\nContact Number - "
						+ cursor.getString(cursor.getColumnIndex("number"))
						+ "\n\n");
				cursor.moveToNext();
			}
			resultView.setText(result);// Finally set string builder to textview
		} else {
			// If cursor is null then display toast and set empty data.
			resultView.setText("Empty data!!");
			Toast.makeText(
					MainActivity.this,
					"May be there is no app corresponding to your provider or there is null data.",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

	// Random int value generator method between 0-5
	private int RandomInt() {
		Random random = new Random();
		int randomValue = random.nextInt(5);
		return randomValue;
	}

	// Update data method
	private void updateData(String[] id) {
		Cursor cursor = getContentResolver().query(Uri.parse(uri), null, null,
				null, null);// Get cursor from Uri

		// If cursor is not null then update data else show toast
		if (cursor != null) {
			int randomValue = RandomInt();// Get random integer
			ContentValues values = new ContentValues();// Content values to
														// insert data
			values.put("name", Random_Datas.Name[randomValue]);// add data from
																// RandomDatas
																// class
			values.put("email", Random_Datas.Email[randomValue]);
			values.put("number", Random_Datas.Number[randomValue]);
			int count = getContentResolver().update(Uri.parse(uri), values,
					"id = ?", id);// now update provider using ID

			// If count is not 0 then row is updated else no row is updated
			if (count != 0)
				Toast.makeText(MainActivity.this, "Row updated",
						Toast.LENGTH_LONG).show();
			else
				Toast.makeText(
						MainActivity.this,
						"May be there is no row to update or the id you pass is not present in database.",
						Toast.LENGTH_LONG).show();

		} else {
			Toast.makeText(
					MainActivity.this,
					"May be there is no app corresponding to your provider or there is null data.",
					Toast.LENGTH_LONG).show();
		}

	}

	// Delete data
	private void deleteData(String[] id) {

		// Firstly get the cursor then check if it is null or not
		Cursor cursor = getContentResolver().query(Uri.parse(uri), null, null,
				null, null);
		if (cursor != null) {

			int count = getContentResolver().delete(Uri.parse(uri), "id = ? ",
					id);// Delete row according to id
			if (count != 0)
				Toast.makeText(MainActivity.this, "Row deleted",
						Toast.LENGTH_LONG).show();
			else
				Toast.makeText(
						MainActivity.this,
						"May be there is no row to delete or the id you pass is not present in database.",
						Toast.LENGTH_LONG).show();

		} else {
			Toast.makeText(
					MainActivity.this,
					"May be there is no app corresponding to your provider or there is null data.",
					Toast.LENGTH_LONG).show();
		}

	}

	// Insert random data
	private void insertData() {
		Cursor cursor = getContentResolver().query(Uri.parse(uri), null, null,
				null, null);
		// First check if cursor is null or not
		if (cursor != null) {
			int randomValue = RandomInt();
			ContentValues values = new ContentValues();
			values.put("name", Random_Datas.Name[randomValue]);
			values.put("email", Random_Datas.Email[randomValue]);
			values.put("number", Random_Datas.Number[randomValue]);
			Uri uri_ = getContentResolver().insert(Uri.parse(uri), values);// Now
																			// add
																			// the
																			// content
																			// values
																			// to
																			// content
																			// provider

			Toast.makeText(MainActivity.this, "Data inserted.",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(MainActivity.this,
					"May be there is no app corresponding to your provider.",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
