//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hhxk.app.util.record;

import android.media.AudioRecord;

/**
 * @title  音频记录仪
 * @date   2019/03/13
 * @author enmaoFu
 */
public class PcmRecorder extends Thread {
    private final short DEFAULT_BIT_SAMPLES;
    private static final int RECORD_BUFFER_TIMES_FOR_FRAME = 4;
    private static final short DEFAULT_CHANNELS = 1;
    private byte[] mDataBuffer;
    private AudioRecord mRecorder;
    private PcmRecorder.PcmRecordListener mOutListener;
    private PcmRecorder.PcmRecordListener mStopListener;
    private volatile boolean exit;
    private double checkDataSum;
    private double checkStandDev;
    public static final int RATE16K = 16000;
    public static final int READ_INTERVAL40MS = 40;
    private int mRate;
    private int mInterval;
    private int mReadInterval;
    private int mAudioSource;

    public PcmRecorder(int sampleRate, int timeInterval) {
        this(sampleRate, timeInterval, 1);
    }

    public PcmRecorder(int sampleRate, int timeInterval, int audioSource) {
        this.DEFAULT_BIT_SAMPLES = 16;
        this.mDataBuffer = null;
        this.mRecorder = null;
        this.mOutListener = null;
        this.mStopListener = null;
        this.exit = false;
        this.checkDataSum = 0.0D;
        this.checkStandDev = 0.0D;
        this.mInterval = 40;
        this.mReadInterval = 40;
        this.mAudioSource = audioSource;
        this.mRate = sampleRate;
        this.mInterval = timeInterval;
        if(this.mInterval < 40 || this.mInterval > 100) {
            this.mInterval = 40;
        }

        this.mReadInterval = 10;
    }

    protected void initRecord(short channels, int sampleRate, int timeInterval) throws SpeechError {
        if(this.mRecorder != null) {
            this.release();
        }

        byte bitSamples = 16;
        int framePeriod = sampleRate * timeInterval / 1000;
        int recordBufferSize = framePeriod * 4 * bitSamples * channels / 8;
        int channelConfig = channels == 1?2:3;
        byte audioFormat = 2;
        int min = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
        if(recordBufferSize < min) {
            recordBufferSize = min;
        }

        this.mRecorder = new AudioRecord(this.mAudioSource, sampleRate, channelConfig, audioFormat, recordBufferSize);
        this.mDataBuffer = new byte[framePeriod * channels * bitSamples / 8];
        if(this.mRecorder.getState() != 1) {
            throw new SpeechError(20006);
        }
    }

    private int readRecordData() throws SpeechError {
        if(this.mRecorder != null && this.mOutListener != null) {
            int count1 = this.mRecorder.read(this.mDataBuffer, 0, this.mDataBuffer.length);
            if(count1 > 0 && this.mOutListener != null) {
                this.mOutListener.onRecordBuffer(this.mDataBuffer, 0, count1);
            } else if(0 > count1) {
                throw new SpeechError(20006);
            }
            return count1;
        } else {
            return 0;
        }
    }

    private double checkAudio(byte[] pcmData, int length) {
        if(null != pcmData && length > 0) {
            double dataAvg = 0.0D;
            double dataSum = 0.0D;
            byte[] frameSum = pcmData;
            int var8 = pcmData.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                byte data = frameSum[var9];
                dataSum += (double)data;
            }

            dataAvg = dataSum / (double)pcmData.length;
            double var14 = 0.0D;
            byte[] var15 = pcmData;
            int var13 = pcmData.length;

            for(int var11 = 0; var11 < var13; ++var11) {
                byte data1 = var15[var11];
                var14 += Math.pow((double)data1 - dataAvg, 2.0D);
            }

            return Math.sqrt(var14 / (double)(pcmData.length - 1));
        } else {
            return 0.0D;
        }
    }

    public void stopRecord(boolean forceStop) {
        this.exit = true;
        if(null == this.mStopListener) {
            this.mStopListener = this.mOutListener;
        }

        this.mOutListener = null;
        if(forceStop) {
            synchronized(this) {
                try {
                    if(this.mRecorder != null) {
                        if(3 == this.mRecorder.getRecordingState() && 1 == this.mRecorder.getState()) {
                            this.mRecorder.release();
                            this.mRecorder = null;
                        }

                        if(null != this.mStopListener) {
                            this.mStopListener.onRecordReleased();
                            this.mStopListener = null;
                        }
                    }
                } catch (Exception e) {
                    //
                }
            }
        }
    }

    public void startRecording(PcmRecorder.PcmRecordListener listener){
        this.mOutListener = listener;
        this.setPriority(10);
        this.start();
    }

    public void run() {
        try {
            int e = 0;

            while(!this.exit) {
                try {
                    this.initRecord((short)1, this.mRate, this.mInterval);
                    break;
                } catch (Exception var7) {
                    ++e;
                    if(e >= 10) {
                        throw new SpeechError(20006);
                    }
                    sleep(40L);
                }
            }
            e = 0;
            while(!this.exit) {
                try {
                    this.mRecorder.startRecording();
                    if(this.mRecorder.getRecordingState() != 3) {
                        throw new SpeechError(20006);
                    }
                    break;
                } catch (Exception var8) {
                    ++e;
                    if(e >= 10) {
                        throw new SpeechError(20006);
                    }

                    sleep(40L);
                }
            }

            if(null != this.mOutListener) {
                this.mOutListener.onRecordStarted(true);
            }

            boolean CHECK_TIME = true;
            long timeStep = System.currentTimeMillis();
            boolean checkAudio = true;

            while(!this.exit) {
                int count = this.readRecordData();
                if(checkAudio) {
                    this.checkDataSum += (double)count;
                    this.checkStandDev += this.checkAudio(this.mDataBuffer, this.mDataBuffer.length);
                    if(System.currentTimeMillis() - timeStep >= 1000L) {
                        checkAudio = false;
                        if(this.checkDataSum == 0.0D || this.checkStandDev == 0.0D) {
                            throw new SpeechError(20006);
                        }
                    }
                }

                if(this.mDataBuffer.length > count) {
                    sleep((long)this.mReadInterval);
                }
            }
        } catch (Exception var9) {
            if(this.mOutListener != null) {
                this.mOutListener.onError(new SpeechError(20006));
            }
        }

        this.release();
    }

    protected void finalize() throws Throwable {
        this.release();
        super.finalize();
    }

    private void release() {
        synchronized(this) {
            try {
                if(this.mRecorder != null) {
                    this.mRecorder.release();
                    this.mRecorder = null;
                    if(null != this.mStopListener) {
                        this.mStopListener.onRecordReleased();
                        this.mStopListener = null;
                    }
                }
            } catch (Exception var4) {
                //
            }
        }
    }

    public interface PcmRecordListener {
        void onRecordBuffer(byte[] data, int var2, int var3);

        void onError(SpeechError error);

        void onRecordStarted(boolean var1);

        void onRecordReleased();
    }
}
