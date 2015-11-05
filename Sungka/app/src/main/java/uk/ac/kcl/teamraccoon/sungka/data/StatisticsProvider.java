package uk.ac.kcl.teamraccoon.sungka.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * A content provider manages access to a central repository of data.
 * Decided to implement it to provide the additional abstraction layer.
 */
public class StatisticsProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private SungkaDbHelper mDbHelper;

    static final int PLAYER = 100;
    static final int HIGH_SCORES = 101;

    @Override
    public boolean onCreate() {
        mDbHelper = new SungkaDbHelper(getContext());
        return true;
    }

    // Retrieve data from a provider
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor returnCursor;

        switch(sUriMatcher.match(uri)) {
            case PLAYER: {
                returnCursor = mDbHelper.getReadableDatabase().query(
                        SungkaContract.PlayerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case HIGH_SCORES: {
                returnCursor = mDbHelper.getReadableDatabase().query(
                        SungkaContract.HighScoresEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    // Return the MIME type corresponding to a content URI
    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PLAYER:
                return SungkaContract.PlayerEntry.CONTENT_TYPE;
            case HIGH_SCORES:
                return SungkaContract.HighScoresEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    // Insert a row into a provider
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case PLAYER: {
                long _id = db.insert(SungkaContract.PlayerEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = SungkaContract.PlayerEntry.buildPlayerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case HIGH_SCORES: {
                long _id = db.insert(SungkaContract.HighScoresEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = SungkaContract.HighScoresEntry.buildHighScoresUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    // Delete rows from a provider
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        // a null deletes all rows
        if (selection == null) selection = "1";

        switch (match) {
            case PLAYER: {
                rowsDeleted = db.delete(
                        SungkaContract.PlayerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case HIGH_SCORES: {
                rowsDeleted = db.delete(
                        SungkaContract.HighScoresEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    // Update existing rows in a provider
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case PLAYER: {
                rowsUpdated = db.update(
                        SungkaContract.PlayerEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case HIGH_SCORES: {
                rowsUpdated = db.update(
                        SungkaContract.HighScoresEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return rowsUpdated;
    }

    // The UriMatcher matches each URI to the PLAYER and HIGH_SCORES constants
    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SungkaContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, SungkaContract.PATH_PLAYER, PLAYER);
        matcher.addURI(authority, SungkaContract.PATH_HIGH_SCORES, HIGH_SCORES);
        return matcher;
    }
}
