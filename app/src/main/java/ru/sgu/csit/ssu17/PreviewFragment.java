package ru.sgu.csit.ssu17;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by User on 04.05.2017.
 */

public class PreviewFragment extends Fragment {
    private String url;
    private WebView webView;

    public PreviewFragment(){
        setArguments(new Bundle());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            this.url = getArguments().getString("url");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.preview_fragment, container, false);
        //WebView previewWebView = (WebView) v.findViewById(R.id.preview_webview);
        this.webView = (WebView) v.findViewById(R.id.preview_webview);
        //previewWebView.loadUrl(url);
        reload();

        return v;
    }

    public void reload(){
        String url = getArguments().getString("url");
        webView.loadUrl(url);
    }
}
