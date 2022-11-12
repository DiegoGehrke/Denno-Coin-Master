package com.nenno.dennoearningapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;


public class GradientTextView extends androidx.appcompat.widget.AppCompatTextView
{
    public GradientTextView( Context context )
    {
        super( context, null, -1 );
    }
    public GradientTextView( Context context,
                             AttributeSet attrs )
    {
        super( context, attrs, -1 );
    }
    public GradientTextView( Context context,
                             AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout( boolean changed,
                             int left, int top, int right, int bottom )
    {
        super.onLayout( changed, left, top, right, bottom );
        if(changed)
        {
            getPaint().setShader( new LinearGradient(
                    0, 0, 0, getHeight(),
                    Color.rgb(254, 228, 188), Color.rgb(242, 206, 127),
                    Shader.TileMode.CLAMP ) );
        }
    }
}
