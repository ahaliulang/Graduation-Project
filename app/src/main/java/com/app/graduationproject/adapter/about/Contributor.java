package com.app.graduationproject.adapter.about;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/**
 * Created by ahaliulang on 2017/2/18.
 */

public class Contributor {
    @DrawableRes public final int avatarResId;
    @NonNull public final String name;
    @NonNull public final String desc;
    public final String url;

    public Contributor(int avatarResId, @NonNull String name, @NonNull String desc, String url) {
        this.avatarResId = avatarResId;
        this.name = name;
        this.desc = desc;
        this.url = url;
    }
}
















