package com.wedley.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends Activity {

    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Transparent status + nav bar
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0xFF000000);       // dark transparent
            window.setNavigationBarColor(0xFF000000);   // dark transparent
            window.getDecorView().setSystemUiVisibility(0); // light icons
        }

        web = new WebView(this);
        setContentView(web);

        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        // Disable zoom
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        web.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return event.getPointerCount() > 1;
                }
            });

        // Load the website
        web.setWebViewClient(new WebViewClient());
        web.loadUrl("https://monomixs.github.io/Kia-Ai/");

        // File creation
        File folder = new File(getExternalFilesDir(null), "Kia secrets");
        if (!folder.exists()) folder.mkdirs();

        File file = new File(folder, "Kia-info.txt");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write("there is nothing here");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Show popup only once
        SharedPreferences prefs = getSharedPreferences("popup_pref", MODE_PRIVATE);
        boolean shown = prefs.getBoolean("shown", false);
        if (!shown) {
            showCustomPopup();
            prefs.edit().putBoolean("shown", true).apply();
        }
    }

    private void showCustomPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Yo!");
        builder.setMessage("This popup only shows once.");
        builder.setPositiveButton("Cool", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) {
            web.goBack();
        } else {
            super.onBackPressed();
        }
    }
}