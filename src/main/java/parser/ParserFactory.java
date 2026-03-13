package parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ParserFactory {
    private List<MissionParser> parsers;
    
    public ParserFactory() {
        parsers = new ArrayList<>();
        parsers.add(new JsonMissionParser());
        parsers.add(new XmlMissionParser());
        parsers.add(new TxtMissionParser());
    }
    
    public MissionParser getParser(File file) {
        for (MissionParser parser : parsers) {
            if (parser.canParse(file)) {
                return parser;
            }
        }
        return null;
    }
}