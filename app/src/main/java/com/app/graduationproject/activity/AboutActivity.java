package com.app.graduationproject.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.graduationproject.R;
import com.app.graduationproject.adapter.about.Card;
import com.app.graduationproject.adapter.about.CardViewProvider;
import com.app.graduationproject.adapter.about.Contributor;
import com.app.graduationproject.adapter.about.ContributorViewProvider;
import com.app.graduationproject.adapter.about.Introduce;
import com.app.graduationproject.adapter.about.IntroduceViewProvider;
import com.app.graduationproject.adapter.about.License;
import com.app.graduationproject.adapter.about.LicenseViewProvider;
import com.app.graduationproject.adapter.about.Line;
import com.app.graduationproject.adapter.about.LineViewProvider;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by ahaliulang on 2017/2/18.
 */

public class AboutActivity extends BaseActivity implements View.OnClickListener{

    protected Toolbar toolbar;
    protected CollapsingToolbarLayout collapsingToolbar;

    protected Items items;
    protected MultiTypeAdapter adapter;
    protected TextView slogan,version;


    protected void onCreateHeader(ImageView icon, TextView slogan,TextView version){
        setHeaderContentColor(Color.parseColor("#FFFFFF"));
        setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        icon.setImageResource(R.mipmap.ic_launcher);
        slogan.setText("A third-party Android client of DiyCode.");
        version.setText("version " + "1.0.1");
    }

    protected void onItemsCreated(@NonNull Items items){
        items.add(new Introduce("介绍"));
        items.add(new Card("jdsoiafdsajofjdsofod","ahaliulang"));

        items.add(new Line());

        items.add(new Introduce("Developers"));
        items.add(new Contributor(R.mipmap.ic_launcher,"ahaliulng","Android Developer",
                "https://www.baidu.com/"));

        items.add(new Line());

        items.add(new Introduce("Open Source Licenses"));
        items.add(new License("MultiType", "drakeet", License.APACHE_2,
                "https://github.com/drakeet/MultiType"));
        items.add(new License("about-page", "drakeet", License.APACHE_2,
                "https://github.com/drakeet/about-page"));
        items.add(new License("butterknife", "JakeWharton", License.APACHE_2,
                "https://github.com/JakeWharton/butterknife"));
        items.add(new License("gson", "google", License.APACHE_2, "https://github.com/google/gson"));
        items.add(
                new License("glide", "bumptech", License.APACHE_2, "https://github.com/bumptech/glide"));
        items.add(
                new License("retrofit", "square", License.APACHE_2, "https://github.com/square/retrofit"));
        items.add(new License("eventbus", "greenrobot", License.APACHE_2,
                "https://github.com/greenrobot/EventBus"));
        items.add(new License("SystemBarTint", "jgilfelt", License.APACHE_2,
                "https://github.com/jgilfelt/SystemBarTint"));
    }

    @Nullable
    protected CharSequence onCreateTitle() {
        return null;
    }

    protected void onActionClick(View action) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView icon = (ImageView) findViewById(R.id.icon);
        slogan = (TextView) findViewById(R.id.slogan);
        version = (TextView) findViewById(R.id.version);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        onCreateHeader(icon, slogan, version);

        final CharSequence title = onCreateTitle();
        if (title != null) {
            collapsingToolbar.setTitle(title);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        onSetupRecyclerView(recyclerView);

    }

    private void onSetupRecyclerView(RecyclerView recyclerView) {
        items = new Items();
        onItemsCreated(items);
        adapter = new MultiTypeAdapter(items);
        adapter.register(Introduce.class, new IntroduceViewProvider());
        adapter.register(Card.class, new CardViewProvider(this));
        adapter.register(Line.class, new LineViewProvider());
        adapter.register(Contributor.class, new ContributorViewProvider());
        adapter.register(License.class, new LicenseViewProvider());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.action) {
            onActionClick(view);
        }
    }


    /**
     * Set the header view background to a given resource and replace the default value
     * ?attr/colorPrimary.
     * The resource should refer to a Drawable object or 0 to remove the background.
     *
     * @param resId The identifier of the resource.
     */
    public void setHeaderBackgroundResource(@DrawableRes int resId) {
        if (collapsingToolbar == null) {
            collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        }
        collapsingToolbar.setContentScrimResource(resId);
        collapsingToolbar.setBackgroundResource(resId);
    }

    public void setHeaderContentColor(@ColorInt int color){
        collapsingToolbar.setCollapsedTitleTextColor(color);
        slogan.setTextColor(color);
        version.setTextColor(color);
    }

    public void setNavigationIcon(@DrawableRes int resId){
        toolbar.setNavigationIcon(resId);
    }


    @Override public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override public void setTitle(CharSequence title) {
        collapsingToolbar.setTitle(title);
    }

}







































