package com.example.yakirtsuberi.gamasm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.yakirtsuberi.gamasm.Helper.CompaniesContent;
import com.example.yakirtsuberi.gamasm.Helper.TracksContent;
import com.example.yakirtsuberi.gamasm.Managers.SessionManager;
import com.example.yakirtsuberi.gamasm.Requests.Requests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
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
    public static JSONObject tracks = new JSONObject();
    public static String sales = "{tracks:{'<track_id>':[{'sim_num':'<sim_num>', 'phone_num':'<phone_num>'}]}}";
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

    public static int getTracksLength() {
        int length = 0;
        Iterator<?> keys = tracks.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                length += ((JSONArray) tracks.get(key)).length();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return length;
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

            assert activity != null;
            session = new SessionManager(activity.getApplication().getApplicationContext());
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.name);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
                Log.i("ITEM_CLICK", String.valueOf(item.id));

                NewIctuvDialog nd = new NewIctuvDialog(getActivity(), item, tracks);
                nd.show();
                if (tracks != null) {
                    Log.i("TRACKS", tracks.toString());
                }


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

            holder.action_add.setTag(mValues.get(position));
            holder.action_add.setOnClickListener(mOnClickListener);
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
            final TextView action_add;

            ViewHolder(View view) {
                super(view);
                company_name = (TextView) view.findViewById(R.id.company_name);
                company_description = (TextView) view.findViewById(R.id.company_description);
                company_price = (TextView) view.findViewById(R.id.company_price);
                company_kosher = (CheckBox) view.findViewById(R.id.company_kosher);
                action_add = (TextView) view.findViewById(R.id.action_add);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetTracks extends AsyncTask<View, Void, String> { //<Params, Progress, Result>
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
                JSONObject jo = new JSONObject(s);
                JSONArray companies = jo.getJSONArray("companies");
                Log.i("COMPANY_LIST", String.valueOf(companies));
                RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.item_detail);
                assert recyclerView != null;
                TracksContent tc = new TracksContent();
                for (int i = 0; i < companies.length(); i++) {
                    try {
                        tc.addItem(new TracksContent.TrackItem(((JSONObject) companies.get(i))));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("COMPANIES_TRACKS", String.valueOf(tc.ITEMS));
                recyclerView.setAdapter(new ItemDetailFragment.SimpleItemRecyclerViewAdapter(tc.ITEMS));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
}
