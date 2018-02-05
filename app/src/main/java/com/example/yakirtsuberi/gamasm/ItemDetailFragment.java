package com.example.yakirtsuberi.gamasm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yakirtsuberi.gamasm.Helper.CompaniesContent;
import com.example.yakirtsuberi.gamasm.Helper.TracksContent;
import com.example.yakirtsuberi.gamasm.Managers.SessionManager;
import com.example.yakirtsuberi.gamasm.Requests.Requests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.example.yakirtsuberi.gamasm.Managers.SessionManager.KEY_TOKEN;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemDetailFragment}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    SessionManager session;
    Requests requests = new Requests();
    /**
     * The dummy name this fragment is presenting.
     */
    private CompaniesContent.CompanyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy name specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load name from a name provider.
            mItem = CompaniesContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();

            session = new SessionManager(activity.getApplication().getApplicationContext());
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.image);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy name as text in a TextView.
        if (mItem != null) {
            new GetTracks().execute(rootView);
        }

        return rootView;
    }


    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemDetailFragment.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<TracksContent.TrackItem> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TracksContent.TrackItem item = (TracksContent.TrackItem) view.getTag();

            }
        };

        SimpleItemRecyclerViewAdapter(List<TracksContent.TrackItem> items) {
            mValues = items;
        }

        @Override
        public ItemDetailFragment.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_comapny, parent, false);
            return new ItemDetailFragment.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ItemDetailFragment.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.company_name.setText(String.valueOf(mValues.get(position).track_name));
            holder.company_description.setText(mValues.get(position).description);
            holder.company_price.setText(String.valueOf(mValues.get(position).price));
            holder.company_kosher.setChecked(mValues.get(position).kosher);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView company_name;
            final TextView company_description;
            final TextView company_price;
            final CheckBox company_kosher;

            ViewHolder(View view) {
                super(view);
                company_name = (TextView) view.findViewById(R.id.company_name);
                company_description = (TextView) view.findViewById(R.id.company_description);
                company_price = (TextView) view.findViewById(R.id.company_price);
                company_kosher = (CheckBox) view.findViewById(R.id.company_kosher);
            }
        }
    }

    private class GetTracks extends AsyncTask<View, Void, String> { //<Params, Progress, Result>
        ProgressBar loading_spinner;
        View rootView;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading_spinner = (ProgressBar) findViewById(R.id.loading_spinner);
//            loading_spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(View... views) {
            rootView = views[0];
            String token = session.getUserDetails().get(KEY_TOKEN);
            Log.i("TOKEN", token);
            return requests.get("/api/tracks/" + mItem.image, token);

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray companies = new JSONObject(s).getJSONArray("companies");
                RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.item_detail);
                assert recyclerView != null;
                TracksContent tc = new TracksContent();
                for(int i = 0; i < companies.length(); i++){
                    try {
                        tc.addItem(new TracksContent.TrackItem(((JSONObject) companies.get(i))));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("COMPANIES_TEACKS", String.valueOf(tc.ITEMS));
                recyclerView.setAdapter(new ItemDetailFragment.SimpleItemRecyclerViewAdapter(tc.ITEMS));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
