package com.raaise.android.Utilities.textPaint;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.widget.TextView;

public class TextPaint {
    public static void getGradientColor(TextView text){
        android.text.TextPaint paint = text.getPaint();
        float width = paint.measureText("Tianjin, China");

        Shader textShader = new LinearGradient(0, 0, width, text.getTextSize(),
                new int[]{
                        Color.parseColor("#EE22AA"),
                        Color.parseColor("#AF00FD"),
                }, null, Shader.TileMode.CLAMP);
        text.getPaint().setShader(textShader);
    }

}
