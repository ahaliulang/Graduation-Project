package com.app.graduationproject.adapter.about;

import android.support.annotation.NonNull;

/**
 * Created by ahaliulang on 2017/2/18.
 */

public class Card {
    @NonNull public final String content;
    @NonNull public final String action;


    public Card(@NonNull String content, @NonNull String action) {
        this.content = content;
        this.action = action;
    }
}
