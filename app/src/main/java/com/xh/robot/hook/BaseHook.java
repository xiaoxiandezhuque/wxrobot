package com.xh.robot.hook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class BaseHook implements IXposedHookLoadPackage {

    public static ClassLoader classLoader;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (lpparam.packageName.equals("com.tencent.mm")) {
            classLoader = lpparam.classLoader;

            XposedHelpers.findAndHookMethod("com.tencent.mm.ui.HomeUI",
                    lpparam.classLoader,
                    "initActionBar",
                    new HomeUiActionBar(lpparam.classLoader));

            XposedHelpers.findAndHookMethod("com.tencent.mm.booter.notification.b",
                    lpparam.classLoader,
                    "a",
                    XposedHelpers.findClass("com.tencent.mm.booter.notification.b", lpparam.classLoader),
                    String.class,
                    String.class,
                    int.class,
                    int.class,
                    boolean.class,
                    new ReceiveAndSendMessage());

        }

    }


}

