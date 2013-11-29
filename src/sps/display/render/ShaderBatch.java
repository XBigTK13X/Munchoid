package sps.display.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

//Modified from: https://github.com/mattdesl/lwjgl-basics/wiki/LibGDX-Brightness-&-Contrast
public class ShaderBatch extends SpriteBatch {
    private Integer _brightnessLocation = null;
    private Integer _contrastLocation = null;
    private float _brightness = 0f;
    private float _contrast = -1f;
    private float _lastBrightness = Float.MAX_VALUE;
    private float _lastContrast = Float.MAX_VALUE;
    private ShaderProgram _shaders;

    public ShaderBatch(ShaderProgram shaders) {
        _shaders = shaders;
        _brightnessLocation = _shaders.getUniformLocation("u_brightness");
        _contrastLocation = _shaders.getUniformLocation("u_contrast");
        setBrightness(_brightness);
        setContrast(_contrast);
        setShader(_shaders);
    }

    public void begin() {
        super.begin();
    }

    public void setBrightness(float brightness) {
        _brightness = brightness;
        if (_lastBrightness != _brightness) {
            _shaders.setUniformf(_brightnessLocation, _brightness);
            _lastBrightness = _brightness;
        }
    }

    public void setContrast(float contrast) {
        _contrast = contrast;
        if (_lastContrast != _contrast) {
            _shaders.setUniformf(_contrastLocation, _contrast);
            _lastContrast = _contrast;
        }
    }
}