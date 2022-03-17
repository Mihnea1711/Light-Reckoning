package com.Utilities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

public class Constants {
    public static final int ScreenWidth = 1280;
    public static final int ScreenHeight = 720;
    public static final String title = "Light's Reckoning";

    public static final int PlayerWidth = 42;
    public static final int PlayerHeight = 42;
    public static final float JumpForce = -480;
    public static final float FlyForce = -330;
    public static final float PlayerSpeed = 300;

    public static final int GroundY = 672;                  //should be 714 ! check the placing blocks above ground!  or above menu items
    public static final int GroundOffsetX = -100;
    public static final int GroundOffsetScreenWidth = 500;

    public static final int CameraX = 200;
    public static final int CameraY = 500;
    public static final int CameraOffsetGroundY = 150;

    public static final float Gravity = 1500;
    public static final float Terminal_Speed = 1900;

    public static final int TileHeight = 42;
    public static final int TileWidth = 42;

    public static final int ButtonOffsetX = 420;
    public static final int ButtonOffsetY = 560;
    public static final int ButtonSpacingHz = 10;
    public static final int ButtonSpacingVt = 5;
    public static final int ButtonWidth = 60;
    public static final int ButtonHeight = 60;

    public static final Color BgColor = new Color(15.0f / 255.0f, 98.0f / 255.0f, 212.0f / 255.0f, 1.0f);
    public static final Color GroundColor = new Color(28.0f / 255.0f, 70 / 255.0f, 148.0f / 255.0f, 1.0f);

    public static final int ContainerOffY = 535;
    public static final int TabWidth = 75;
    public static final int TabHeight = 38;
    public static final int TabOffX = 380;
    public static final int TabOffY = 497;
    public static final int TabHzPlace= 10;

    public static final Stroke Line = new BasicStroke(1.0f);
    public static final Stroke ThickLine = new BasicStroke(3.0f);

}
