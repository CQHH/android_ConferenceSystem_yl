package com.em.banner;

import android.support.v4.view.ViewPager.PageTransformer;

import com.em.banner.transformer.AccordionTransformer;
import com.em.banner.transformer.BackgroundToForegroundTransformer;
import com.em.banner.transformer.CubeInTransformer;
import com.em.banner.transformer.CubeOutTransformer;
import com.em.banner.transformer.DefaultTransformer;
import com.em.banner.transformer.DepthPageTransformer;
import com.em.banner.transformer.FlipHorizontalTransformer;
import com.em.banner.transformer.FlipVerticalTransformer;
import com.em.banner.transformer.ForegroundToBackgroundTransformer;
import com.em.banner.transformer.RotateDownTransformer;
import com.em.banner.transformer.RotateUpTransformer;
import com.em.banner.transformer.ScaleInOutTransformer;
import com.em.banner.transformer.StackTransformer;
import com.em.banner.transformer.TabletTransformer;
import com.em.banner.transformer.ZoomInTransformer;
import com.em.banner.transformer.ZoomOutSlideTransformer;
import com.em.banner.transformer.ZoomOutTranformer;


public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
