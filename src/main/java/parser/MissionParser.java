package parser;

import model.Mission;
import java.io.File;

public interface MissionParser {
    Mission parse(File file) throws Exception;
    boolean canParse(File file);
}