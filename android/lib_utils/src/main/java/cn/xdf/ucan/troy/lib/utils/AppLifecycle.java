package cn.xdf.ucan.troy.lib.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author hulijia
 * @createDate 2020/8/30
 * @description AppLifecycle
 */
public class AppLifecycle {
    private final List<SwitchListener> mListeners = new CopyOnWriteArrayList<>();
    private int mResumeActivityCount = 0;

    public void init(Application application) {
        if (application == null) {
            return;
        }

        application.registerActivityLifecycleCallbacks(
                new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(Activity activity,
                                                  Bundle savedInstanceState) {

                    }

                    @Override
                    public void onActivityStarted(Activity activity) {
                        if (mResumeActivityCount == 0) {
                            for (SwitchListener listener : mListeners) {
                                listener.onSwitchToFront();
                            }
                        }
                        mResumeActivityCount++;
                    }

                    @Override
                    public void onActivityResumed(Activity activity) {

                    }

                    @Override
                    public void onActivityPaused(Activity activity) {

                    }

                    @Override
                    public void onActivityStopped(Activity activity) {
                        mResumeActivityCount--;
                        if (mResumeActivityCount == 0) {
                            for (SwitchListener listener : mListeners) {
                                listener.onSwitchToBack();
                            }
                        }
                    }

                    @Override
                    public void onActivitySaveInstanceState(Activity activity,
                                                            Bundle outState) {

                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {

                    }
                });
    }

    public void addSwitchListener(SwitchListener listener) {
        if (listener != null) {
            mListeners.add(listener);
        }
    }

    public void removeSwitchListener(SwitchListener listener) {
        if (listener != null) {
            mListeners.remove(listener);
        }
    }

    public interface SwitchListener {
        /**
         * 切换到前台
         */
        void onSwitchToFront();

        /**
         * 切换到后台
         */
        void onSwitchToBack();
    }

}