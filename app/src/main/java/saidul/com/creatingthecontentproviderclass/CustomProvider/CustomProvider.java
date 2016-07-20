package saidul.com.creatingthecontentproviderclass.CustomProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import saidul.com.creatingthecontentproviderclass.database.DBhelper;

/**
 * Created by Prime Tech on 7/20/2016.
 */
public class CustomProvider extends ContentProvider {
    public static final String TAG = CustomProvider.class.getName();
    // database
    DBhelper dBhelper;

    private static UriMatcher uriMatcher;

    // this uriMatcher to check and match url
    static {
        uriMatcher = new UriMatcher(uriMatcher.NO_MATCH);
        uriMatcher.addURI(ContactProviderStatus.AUTH, DBhelper.TABLE_MEMBER, ContactProviderStatus.members);
        uriMatcher.addURI(ContactProviderStatus.AUTH, DBhelper.TABLE_MEMBER+"/#",ContactProviderStatus.member_id);
    }


    @Override
    public boolean onCreate() {
        // Create a Database
        dBhelper = new DBhelper(getContext());
        Log.i(TAG, "onCreate: ");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DBhelper.TABLE_MEMBER);

        SQLiteDatabase db;
        db = dBhelper.getReadableDatabase();

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            /**
             * Get all member records
             */
            case ContactProviderStatus.members:
                return "vnd.android.cursor.dir/members";
            /**
             * Get a particular member
             */
            case ContactProviderStatus.member_id:
                return "vnd.android.cursor.item/member_id";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db ;
        db = dBhelper.getWritableDatabase();

        // check uri
        if (uriMatcher.match(uri)==ContactProviderStatus.members){
            long insert = db.insert(DBhelper.TABLE_MEMBER, null, contentValues);
            if (insert>0){
                Uri _uri = ContentUris.withAppendedId(ContactProviderStatus.CONTENT_URI, insert);
                getContext().getContentResolver().notifyChange(_uri,null);
                return _uri;
            }
        }
        // close databae
        db.close();

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db;
        db = dBhelper.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriMatcher.match(uri)) {
            case ContactProviderStatus.members:
                rowsDeleted = db.delete(DBhelper.TABLE_MEMBER, selection,
                        selectionArgs);
                break;

            case ContactProviderStatus.member_id:
                String id = uri.getLastPathSegment();
                rowsDeleted = db.delete(DBhelper.TABLE_MEMBER, DBhelper.MEMBER_ID + "=" + id, null);
                break;

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase db;
        db = dBhelper.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriMatcher.match(uri)) {
            case ContactProviderStatus.members:
                rowsUpdated = db.update(DBhelper.TABLE_MEMBER, values, selection, selectionArgs);
                break;

            case  ContactProviderStatus.member_id:
                String id = uri.getLastPathSegment();
                rowsUpdated = db.update(DBhelper.TABLE_MEMBER, values, DBhelper.MEMBER_ID + "=" + id, null);
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
