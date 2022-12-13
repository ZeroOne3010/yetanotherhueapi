package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.Color;
import io.github.zeroone3010.yahueapi.XAndYAndBrightness;
import io.github.zeroone3010.yahueapi.v2.domain.Xy;
import io.github.zeroone3010.yahueapi.v2.domain.update.Dimming;
import io.github.zeroone3010.yahueapi.v2.domain.update.UpdateLight;

public class UpdateState extends UpdateLight {
  public UpdateState setColor(final Color color) {
    final XAndYAndBrightness xy = XAndYAndBrightness.rgbToXy(color);
    super.setColor(new io.github.zeroone3010.yahueapi.v2.domain.update.Color()
            .setXy(new Xy().setX(xy.getX()).setY(xy.getY())))
        .setDimming(new Dimming().setBrightness(xy.getBrightnessMax100()));
    return this;
  }
}
