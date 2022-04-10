package com.Utilities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

/**
 * Super helpful whenever you want to be tweaking values.
 * Holds the constant variables inside the project.
 */
public class Constants {
    //Screen properties
    public static final int ScreenWidth = 1280;
    public static final int ScreenHeight = 720;
    public static final String Title = "Light's Reckoning";

    //Player properties
    public static final int PlayerWidth = 42;
    public static final int PlayerHeight = 42;
    public static final float JumpForce = -480;
    public static final float FlyForce = -300;
    public static final float PlayerSpeed = 300;

    //Ground properties
    public static final int GroundY = 672;
    public static final int GroundOffsetX = -100;
    public static final int GroundOffsetScreenWidth = 500;

    //Camera properties
    public static final int CameraX = 250;  //initial x position for camera
    public static final int CameraY = 350;  //initial y position for camera
    public static final int CameraOffsetGroundY = 150;      //stops the camera when it gets to this limit so it won't go beneath the ground

    //Independent properties
    public static final float Gravity = 1400;               //gravity force
    public static final float Terminal_Speed = 1900;        //maximum speed

    //Tile properties
    public static final int TileHeight = 42;
    public static final int TileWidth = 42;

    //Buttons properties
    public static final int ButtonOffsetX = 420;        //gap from the left of the screen to the start of the main container
    public static final int ButtonOffsetY = 560;        //gap from the top of the screen to the start of the main container
    public static final int ButtonSpacingHz = 10;       //space between the buttons horizontally
    public static final int ButtonSpacingVt = 5;        //space between the buttons vertically
    public static final int ButtonWidth = 60;
    public static final int ButtonHeight = 60;
    public static final int BigButtonWidth = 95;
    public static final int BigButtonHeight = 95;

    //Color properties
    public static final Color BgColor = new Color(212.0f / 255.0f, 20.0f / 255.0f, 20 / 255.0f, 1.0f);
    public static final Color GroundColor = new Color(208.0f / 255.0f, 70 / 255.0f, 10 / 255.0f, 1.0f);
    public static final float BgSpeed = 35.0f;

    //Tab properties
    public static final int ContainerOffY = 535;
    public static final int TabWidth = 75;
    public static final int TabHeight = 38;
    public static final int TabOffX = 380;
    public static final int TabOffY = 497;
    public static final int TabHzSpacing= 10;

    //Selected object properties
    public static final Stroke Line = new BasicStroke(1.0f);
    public static final Stroke ThickLine = new BasicStroke(3.0f);

    //Scene player start properties
    public static final float PlayerEditorX = 250;
    public static final float PlayerEditorY = 350;
    public static final float PlayerLevelStartX = 150;
    public static final float PlayerLevelStartY = 300;

    //Grid properties
    public static final int GridYLines = 31;
    public static final int GridXLines = 20;

    //MainMenu properties
    public static final Color MenuBg_Color = new Color(174f / 255.0f, 38f / 255.0f, 176f / 255.0f, 1.0f);
    //public static final Color MenuGround_Color = new Color(131f / 255.0f, 13f / 255.0f, 133f / 255.0f, 0.8f);
    public static final int MenuGround_Y = 600;
    public static final int MenuPlayerX = 835;
    public static final int MenuPlayerY = 600 - PlayerHeight;
    public static final int MenuPlayButtonX = 460;
    public static final int MenuPlayButtonY = 289;
    public static final int MenuEditorButtonX = 640;
    public static final int MenuEditorButtonY = 289;

    public static final int LogoSize = 40;


}
