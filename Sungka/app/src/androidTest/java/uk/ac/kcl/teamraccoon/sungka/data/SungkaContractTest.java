package uk.ac.kcl.teamraccoon.sungka.data;

import android.net.Uri;
import android.test.AndroidTestCase;

// In order to test the database we followed an online course provided by Udacity
public class SungkaContractTest extends AndroidTestCase {

    public void testBuildPlayerUri() {
        final int ID_TEST = 1;
        Uri playerUri = SungkaContract.PlayerEntry.buildPlayerUri(ID_TEST);
        assertNotNull("Null Uri returned", playerUri);
        assertEquals("Problem with appending Id to the Uri",
                Integer.toString(ID_TEST), playerUri.getLastPathSegment());
        assertEquals("Uri does not match expected result",
                playerUri.toString(), "content://uk.ac.kcl.teamraccoon.sungka/player/1");
    }

    public void testBuildHighScoresUri() {
        final int ID_TEST = 113;
        Uri highScoresUri = SungkaContract.HighScoresEntry.buildHighScoresUri(ID_TEST);
        assertNotNull("Null Uri returned", highScoresUri);
        assertEquals("Problem with appending Id to the Uri",
                Integer.toString(ID_TEST), highScoresUri.getLastPathSegment());
        assertEquals("Uri does not match expected result",
                highScoresUri.toString(), "content://uk.ac.kcl.teamraccoon.sungka/high_scores/113");
    }


}
