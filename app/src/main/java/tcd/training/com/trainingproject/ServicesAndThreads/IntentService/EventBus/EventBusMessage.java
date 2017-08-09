package tcd.training.com.trainingproject.ServicesAndThreads.IntentService.EventBus;

/**
 * Created by cpu10661-local on 01/08/2017.
 */

public class EventBusMessage {

    private int mInteger;

    public EventBusMessage(int integer) {
        this.mInteger = integer;
    }

    public int getInteger() {
        return mInteger;
    }

    public void setInteger(int integer) {
        this.mInteger = integer;
    }
}
