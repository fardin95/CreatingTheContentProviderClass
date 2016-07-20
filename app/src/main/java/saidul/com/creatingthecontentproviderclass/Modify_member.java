package saidul.com.creatingthecontentproviderclass;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import saidul.com.creatingthecontentproviderclass.CustomProvider.ContactProviderStatus;
import saidul.com.creatingthecontentproviderclass.database.DBhelper;

/**
 * Created by Prime Tech on 7/20/2016.
 */
public class Modify_member extends Activity {

    EditText et;

    long memberID;
    Uri uri;
    String member_id, member_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.modify_member);

        et = (EditText) findViewById(R.id.edit_mem_id);

        Intent i = getIntent();
        member_id = i.getStringExtra("member_id");
        member_name = i.getStringExtra("member_name");

        et.setText(member_name);

        memberID = Long.parseLong(member_id);
        uri = Uri.parse(ContactProviderStatus.CONTENT_URI + "/" + memberID);

    }

    public void updateData(View view) {

        String modified_name = et.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(DBhelper.MEMBER_NAME, modified_name);

        getContentResolver().update(uri, cv, null, null);

        finish();
    }

    public void deleteData(View view) {
        getContentResolver().delete(uri, null, null);

        finish();
    }

}
