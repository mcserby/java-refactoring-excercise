import com.mcserby.training.refactoring.Details;
import com.mcserby.training.refactoring.InternationalValues;
import com.mcserby.training.refactoring.Locale;
import com.mcserby.training.refactoring.Translation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Mapper {

    public static Map<String, Function<InternationalValues, Details>> LOCALE_EXTRACTORS = new HashMap<>();

    static {
        LOCALE_EXTRACTORS.put("en_uk", InternationalValues::getUk);
        LOCALE_EXTRACTORS.put("nl", InternationalValues::getNl);
        LOCALE_EXTRACTORS.put("de", InternationalValues::getDe);
        LOCALE_EXTRACTORS.put("fr", InternationalValues::getFr);
        LOCALE_EXTRACTORS.put("es", InternationalValues::getEs);
        LOCALE_EXTRACTORS.put("it", InternationalValues::getIt);
        LOCALE_EXTRACTORS.put("at", InternationalValues::getAt);
        LOCALE_EXTRACTORS.put("ch_fr", InternationalValues::getChFr);
        LOCALE_EXTRACTORS.put("ch_de", InternationalValues::getChDe);
        LOCALE_EXTRACTORS.put("ch_it", InternationalValues::getChIt);
        LOCALE_EXTRACTORS.put("be_fr", InternationalValues::getBeFr);
        LOCALE_EXTRACTORS.put("lux_de", InternationalValues::getLuDe);
        LOCALE_EXTRACTORS.put("es_ca", InternationalValues::getCaEs);
        LOCALE_EXTRACTORS.put("lux_fr", InternationalValues::getLuFr);
    }

    private static List<Translation> mapNames(List<InternationalValues> values) {
        return extractValues(values, Details::getName).entrySet().stream().map(es -> {
            String name = es.getValue().stream().distinct().collect(Collectors.joining(" "));
            return new Translation(Collections.singletonList(name), new Locale(es.getKey()));
        }).collect(Collectors.toList());
    }

    private static List<Translation> mapDescriptions(List<InternationalValues> values) {
        return extractValues(values, Details::getDescription).entrySet().stream().map(es -> {
            String description = es.getValue().stream().findFirst().orElse("");
            return new Translation(Collections.singletonList(description), new Locale(es.getKey()));
        }).collect(Collectors.toList());
    }

    private static Map<String, List<String>> extractValues(List<InternationalValues> values, Function<Details, String> detailsExtractor) {
        return LOCALE_EXTRACTORS.entrySet().stream().map(extractor -> {
            List<String> extractedValues = values.stream().map(v -> detailsExtractor.apply(extractor.getValue().apply(v))).collect(Collectors.toList());
            String locale = extractor.getKey();
            return Map.entry(locale, extractedValues);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static String trimTo253(String value) {
        return value.substring(0, Math.min(value.length(), 253)).trim();
    }
}
