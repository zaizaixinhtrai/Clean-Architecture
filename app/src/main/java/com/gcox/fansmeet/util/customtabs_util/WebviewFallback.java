package com.gcox.fansmeet.util.customtabs_util;

import android.content.Context;
import android.net.Uri;

/**
 * A Fallback that opens a Webview when Custom Tabs is not available
 */
public class WebviewFallback implements CustomTabActivityHelper.CustomTabFallback {
    @Override
    public void openUri(Context context, Uri uri) {
    }
}