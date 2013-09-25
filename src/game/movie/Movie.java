package game.movie;

import sps.text.Text;
import sps.text.TextPool;
import sps.display.Screen;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private class Strip {
        private float _seconds;
        private String _message;

        public Strip(float seconds, String message) {
            _message = message;
            _seconds = seconds;
        }

        public String getMessage() {
            return _message;
        }

        public boolean onScreen(float timeSecs) {
            return timeSecs >= _seconds;
        }
    }

    private List<Strip> _strips;
    private Text _subtitle;

    public Movie() {
        _strips = new ArrayList<Strip>();
        _subtitle = TextPool.get().write("", Screen.pos(5, 50));
    }

    public void addStrip(float timeSeconds, String message) {
        _strips.add(new Strip(timeSeconds, message));
    }

    private boolean _firstFrame = true;

    public void play(float timeSecs) {
        for (int ii = 0; ii < _strips.size(); ii++) {
            if (ii == 0 && _firstFrame || _strips.get(ii).onScreen(timeSecs)) {
                _subtitle.setMessage(_strips.get(ii).getMessage());
                _strips.remove(ii);
                ii--;
                _firstFrame = false;
            }
        }
    }
}
