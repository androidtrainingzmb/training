package tcd.training.com.trainingproject.ServiceAndThreadDemos.IntentService.RxAndroid;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import tcd.training.com.trainingproject.ServiceAndThreadDemos.IntentService.EventBus.EventBusMessage;

/**
 * Created by cpu10661-local on 01/08/2017.
 */

public class RxBus {

    private static RxBus mRxBus;
    private RxBus() {}
    public static RxBus getInstance() {
        if (mRxBus == null) {
            mRxBus = new RxBus();
        }
        return mRxBus;
    }

    private static PublishSubject<EventBusMessage> mSubjects = PublishSubject.create();

    public static Disposable subscribe(@NonNull Consumer<EventBusMessage> action) {
        return mSubjects.subscribe(action);
    }

    public static void publish(@NonNull EventBusMessage message) {
        mSubjects.onNext(message);
    }

}
