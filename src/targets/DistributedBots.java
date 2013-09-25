package targets;

import sps.core.Logger;
import sps.util.Commander;

import java.util.ArrayList;
import java.util.List;

public class DistributedBots {
    public static void main(String[] args) {
        Logger.info("Bot initialization.");
        List<Commander> ps = new ArrayList<>();
        for (int ii = 0; ii < numBots; ii++) {
            ps.add(launch());
        }
        Logger.info("Created " + ps.size() + " bots ");
        while (true) {
            for (int ii = 0; ii < ps.size(); ii++) {
                if (ps.get(ii).isFinished()) {
                    //This should only happen once the process finished
                    Logger.info("A bot has finished. Launching a new one");
                    ps.remove(ii);
                    ii--;
                    ps.add(launch());
                    Logger.info("New bot launched, " + total + " bots since launch");
                }
            }
            while (ps.size() < numBots) {
                ps.add(launch());
            }
        }
    }

    private static final String JDK = "/usr/lib/jvm/jdk7";
    private static final String gameClassPath = "-classpath " + JDK + "/jre/lib/deploy.jar:" + JDK + "/jre/lib/plugin.jar:" + JDK + "/jre/lib/jsse.jar:" + JDK + "/jre/lib/jce.jar:" + JDK + "/jre/lib/resources.jar:" + JDK + "/jre/lib/javaws.jar:" + JDK + "/jre/lib/rt.jar:" + JDK + "/jre/lib/charsets.jar:" + JDK + "/jre/lib/management-agent.jar:" + JDK + "/jre/lib/ext/sunpkcs11.jar:" + JDK + "/jre/lib/ext/dnsns.jar:" + JDK + "/jre/lib/ext/localedata.jar:" + JDK + "/jre/lib/ext/sunjce_provider.jar:/home/kretst/dev/nnue/game/target/classes:/home/kretst/.m2/repository/commons-io/commons-io/2.4/commons-io-2.4.jar:/home/kretst/.m2/repository/org/apache/commons/commons-lang3/3.1/commons-lang3-3.1.jar:/home/kretst/.m2/repository/org/apache/commons/commons-math3/3.2/commons-math3-3.2.jar:/home/kretst/.m2/repository/com/badlogic/gdx/2013.06.20/gdx-2013.06.20.jar:/home/kretst/.m2/repository/com/badlogic/gdx-openal/2013.06.20/gdx-openal-2013.06.20.jar:/home/kretst/.m2/repository/com/badlogic/gdx-natives/2013.06.20/gdx-natives-2013.06.20.jar:/home/kretst/.m2/repository/com/badlogic/gdx-backend-lwjgl/2013.06.20/gdx-backend-lwjgl-2013.06.20.jar:/home/kretst/.m2/repository/com/badlogic/gdx-backend-lwjgl-natives/2013.06.20/gdx-backend-lwjgl-natives-2013.06.20.jar:/home/kretst/.m2/repository/com/badlogic/gdx-controllers/2013.06.20/gdx-controllers-2013.06.20.jar:/home/kretst/.m2/repository/com/badlogic/gdx-controllers-desktop/2013.06.20/gdx-controllers-desktop-2013.06.20.jar:/home/kretst/.m2/repository/com/badlogic/gdx-controllers-desktop-natives/2013.06.20/gdx-controllers-desktop-natives-2013.06.20.jar:/home/kretst/.m2/repository/com/badlogic/gdx-freetype/2013.07.25/gdx-freetype-2013.07.25.jar:/home/kretst/.m2/repository/com/badlogic/gdx-freetype-natives/2013.07.25/gdx-freetype-natives-2013.07.25.jar";
    private static final String gameCmd = "" + JDK + "/bin/java -Xmx2G -Dfile.encoding=UTF-8 " + gameClassPath + " targets.DesktopGame --play-as-bot --debug-bot-launch";
    private static int total = 0;
    private static final boolean captureBotIO = false;
    private static final int numBots = 1;

    private static Commander launch() {
        total++;
        return new Commander(gameCmd, ".", captureBotIO);
    }
}
