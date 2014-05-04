package com.overnightApps.myapplication.app.core.helper;

import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.core.LetterVote;

/**
 * Created by andre on 4/23/14.
 */
public class LetterWithUserVote {
    final private Letter letter;
    final private LetterVote letterVote;

    public LetterWithUserVote(Letter letter, LetterVote letterVote) {
        this.letter = letter;
        this.letterVote = letterVote;
    }

    public Letter getLetter() {
        return letter;
    }

    public LetterVote getLetterVote() {
        return letterVote;
    }
}
