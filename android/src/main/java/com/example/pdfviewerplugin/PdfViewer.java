package com.example.pdfviewerplugin;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.link.LinkHandler;
import com.github.barteksc.pdfviewer.model.LinkTapEvent;

import android.content.Intent;
import android.content.Context;
import android.view.View;

import java.io.File;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.platform.PlatformView;

public class PdfViewer implements PlatformView, MethodCallHandler {
    private PDFView pdfView;
    private String filePath;
    LinkHandler linkHandler = new MyLinktHandler();

    PdfViewer(Context context, BinaryMessenger messenger, int id, Map<String, Object> args) {
        MethodChannel methodChannel = new MethodChannel(messenger, "pdf_viewer_plugin_" + id);
        methodChannel.setMethodCallHandler(this);;
        pdfView = new PDFView(context, null);

        if (!args.containsKey("filePath")) {
            return;
        }

        filePath = (String)args.get("filePath");
        loadPdfView();

    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("getPdfViewer")) {
            result.success(null);
        } else {
            result.notImplemented();
        }
    }

    private void loadPdfView() {
        pdfView.fromFile(new File(filePath))
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .linkHandler(linkHandler)
                .load();
    }

    @Override
    public View getView() {
        return pdfView;
    }

    @Override
    public void dispose() {}
}

class MyLinktHandler implements LinkHandler {

    @Override
    public void handleLinkEvent(LinkTapEvent event) {
        event.getLink().getUri();
    }
}