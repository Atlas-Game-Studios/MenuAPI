package com.ags.menuapi.callbacks;

import com.ags.menuapi.Menu.AnimatedListMenu;
import com.ags.menuapi.Menu.AnimatedMenu;

public interface AnimationCallback {

    void callback(AnimatedMenu menu);

    void callback(AnimatedListMenu menu);

}
