package tcd.training.com.trainingproject.CommunicationBetweenActivities.UsingApplicationObject;

import android.app.Application;

import tcd.training.com.trainingproject.CommunicationBetweenActivities.DemoObject;

/**
 * Created by cpu10661-local on 24/07/2017.
 */

public class MyApplication extends Application {

    private DemoObject object;

    public DemoObject getObject() {
        return object;
    }

    public void setObject(DemoObject object) {
        this.object = object;
    }
}
