package com.example.android.baking.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.android.baking.widget.WidgetListRemoteViewsFactory;

public class WidgetListRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
