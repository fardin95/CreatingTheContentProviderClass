package saidul.com.creatingthecontentproviderclass;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import saidul.com.creatingthecontentproviderclass.CustomProvider.ContactProviderStatus;
import saidul.com.creatingthecontentproviderclass.database.DBhelper;

public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String TAG = MainActivity.class.getName();
    private EditText et;
    private ListView lv;
    private Button btInsert;
    private SimpleCursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: ");

        ini();

    }

    private void ini() {
        et = (EditText) findViewById(R.id.editText1);
        lv = (ListView) findViewById(R.id.listView1);
        btInsert = (Button) findViewById(R.id.btInsert);
        btInsert.setOnClickListener(this);

        // Initiation loader manager
       getSupportLoaderManager().initLoader(0, null, this);

        String[] from = new String[] { DBhelper.MEMBER_ID, DBhelper.MEMBER_NAME };
        int[] to = new int[] { R.id.member_id, R.id.member_name };

        adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.view_member_entry, null, from, to, 0);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

    }


    // below all callback method form @LoaderManager.LoaderCallbacks<Cursor>
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // All Columns Name
        String[]  allColumns  = new String[]{DBhelper.MEMBER_ID,DBhelper.MEMBER_NAME};

        // CursorLoader stat and load all data form database
        CursorLoader cursorLoader = new CursorLoader(this, ContactProviderStatus.CONTENT_URI, null,null,null, null);

        Log.i(TAG, "onCreateLoader: ");
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i(TAG, "onLoadFinished: ");
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i(TAG, "onLoaderReset: ");
        adapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btInsert:

                insertDataInroDb();

                break;

        }
    }

    private void insertDataInroDb() {
        String name = et.getText().toString();

        ContentValues values = new ContentValues();
        values.put(DBhelper.MEMBER_NAME, name);
        Uri uri = getContentResolver().insert(ContactProviderStatus.CONTENT_URI, values);
        et.setText("");


    }
}
