package com.tayek.util.audio;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
public final class AudioClips {
    private AudioClips() {}
    public static boolean playWav(Class<?> resourceBase,String wavName,float gainDb,boolean wait) {
        if(resourceBase==null||wavName==null) return false;
        try {
            InputStream raw=resourceBase.getResourceAsStream(wavName);
            if(raw==null) {
                logger.warning("audio resource not found: "+wavName);
                return false;
            }
            BufferedInputStream buffered=new BufferedInputStream(raw);
            AudioInputStream inputStream=AudioSystem.getAudioInputStream(buffered);
            Clip clip=AudioSystem.getClip();
            clip.open(inputStream);
            FloatControl gainControl=(FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(gainDb);
            clip.start();
            if(wait) while(clip.getMicrosecondLength()!=clip.getMicrosecondPosition()) Thread.sleep(1);
            clip.close();
            inputStream.close();
            return true;
        } catch(Exception e) {
            logger.warning("audio play failed for "+wavName+": "+e);
            return false;
        }
    }
    private static final Logger logger=Logger.getLogger(AudioClips.class.getName());
}
