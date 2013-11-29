#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_brightness;
uniform float u_contrast;

void main() {
    vec4 color = v_color * texture2D(u_texture, v_texCoords);
    color.rgb /= color.a;
    color.rgb = ((color.rgb - 0.5f) * max(u_contrast,0)) + 0.5f;
    color.rgb += u_brightness;
    color.rgb *= color.a;
    gl_FragColor = color;
}