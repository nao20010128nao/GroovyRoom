/*
 *    Copyright 2017 nao20010128nao
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.nao20010128nao.GroovyRoom.settings

import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.view.MenuItem
import com.nao20010128nao.GroovyRoom.Constants
import com.nao20010128nao.GroovyRoom.R
import com.nao20010128nao.GroovyRoom.VH
import groovy.transform.CompileStatic

class SettingListActivity extends AppCompatActivity implements FragmentSwitchable{

    private boolean mTwoPane

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_list)

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        if (supportActionBar) {
            supportActionBar.displayHomeAsUpEnabled=true
        }

        def items=[
                new Item(R.string.general_settings,BasicSettingsFragment),
                new Item(R.string.edit_text_insertion_list,TextInsertSortFragment)
        ]

        RecyclerView recyclerView = findViewById(R.id.setting_list) as RecyclerView
        recyclerView.adapter = new SimpleItemRecyclerViewAdapter(items)

        mTwoPane = !!findViewById(R.id.setting_detail_container)
    }

    @Override
    boolean onOptionsItemSelected(MenuItem item) {
        int id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @Override
    boolean isAllowed() { mTwoPane }

    @Override
    void doSwitch(Class<? extends Fragment> frag) {
        def fragment=frag.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.setting_detail_container, fragment)
                .commit()
    }

    class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final List<Item> mValues

        SimpleItemRecyclerViewAdapter(List<Item> items) {
            mValues = items
        }

        @Override
        ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(layoutInflater.inflate(R.layout.setting_list_content, parent, false))
        }

        @Override
        void onBindViewHolder(final ViewHolder holder, int position) {
            holder.item = mValues[position]
            holder.contentView.text = mValues[position].titleRes

            holder.view.onClickListener = {
                if (mTwoPane) {
                    doSwitch(holder.item.fragment)
                } else {
                    Intent intent = new Intent(SettingListActivity.this, SettingDetailActivity.class)
                    intent.putExtra(Constants.EXTRA_FRAGMENT_CLASS, holder.item.fragment)

                    startActivity(intent)
                }
            }
        }

        @Override
        int getItemCount() {
            return mValues.size()
        }

        class ViewHolder extends VH {
            public final View view
            public final TextView contentView
            public Item item

            ViewHolder(View view) {
                super(view)
                this.view = view
                contentView = (TextView) view.findViewById(R.id.content)
            }

            @Override
            String toString() {
                return "${super.toString()} '$contentView.text'"
            }
        }
    }

    static class Item {
        @StringRes
        public final int titleRes
        public final Class<? extends Fragment> fragment

        Item(@StringRes int titleRes,Class<? extends Fragment> fragment) {
            this.titleRes=titleRes
            this.fragment=fragment
        }
    }
}
