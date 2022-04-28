package com.ags.menuapi.addons;

import org.bukkit.Sound;

public class ClickSound {

    private boolean hasSound;
    private Sound sound;
    private float volume;
    private float pitch;

    public ClickSound() {
        setClickSound();
        setHasSound(true);
    }

    public ClickSound(boolean active) {
        setClickSound();
        setHasSound(active);
    }

    public boolean hasSound() {
        return hasSound;
    }

    public void setHasSound(boolean hasSound) {
        this.hasSound = hasSound;
    }

    public Sound getSound() {
        return sound;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }

    public void setClickSound() {
        this.sound = Sound.UI_BUTTON_CLICK;
        this.volume = 0.1f;
        this.pitch = 2.0f;
    }

    public void setClickSound(Sound sound) {
        this.hasSound = true;
        this.sound = sound;
        this.volume = 0.1f;
        this.pitch = 2.0f;
    }

    public void setClickSound(Sound sound, float volume, float pitch) {
        this.hasSound = true;
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }


}
