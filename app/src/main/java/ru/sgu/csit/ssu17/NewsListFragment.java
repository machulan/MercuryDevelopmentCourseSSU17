package ru.sgu.csit.ssu17;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 04.05.2017.
 */

public class NewsListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Article>> {


    private static final String LOG_TAG = "NewsListActivity";

    private final ArrayList<Article> data = new ArrayList<>();
    private NewsItemAdapter dataAdapter;

    public interface Listener {
        void OnArticleClicked(Article article);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dataAdapter = new NewsItemAdapter(getActivity(), data);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.news_list_fragment, container, false);

        ListView newsList = (ListView) v.findViewById(R.id.news_list);
        newsList.setAdapter(dataAdapter);
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = (Article) parent.getItemAtPosition(position);

                if(isResumed()) {
                    Listener l = (Listener) getActivity();
                    l.OnArticleClicked(article);
                }

//                PreviewFragment fragment = new PreviewFragment();
//                Bundle args = new Bundle();
//                args.putString("url", article.link);
//                fragment.setArguments(args);
//                getFragmentManager().beginTransaction()
//                        .add(R.id.container, fragment)
//                        .addToBackStack(null)
//                        .commit();
            }
        });

        Button refreshBtn = (Button) v.findViewById(R.id.refresh_btn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoaderManager().restartLoader(0, null, NewsListFragment.this);
            }
        });

        return v;
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        return new SguRssLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> loaderData) {
        Log.d(LOG_TAG, "onLoadFinished " + loader.hashCode());
        data.clear();
        data.addAll(loaderData);
        dataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        Log.d(LOG_TAG, "onLoadedReset " + loader.hashCode());
    }
}
