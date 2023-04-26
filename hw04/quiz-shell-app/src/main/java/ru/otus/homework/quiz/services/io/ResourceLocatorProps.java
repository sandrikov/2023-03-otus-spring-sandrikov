package ru.otus.homework.quiz.services.io;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.otus.homework.quiz.services.localization.AppLocale;

import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

@ConditionalOnProperty({"application.resource.location"})
@Service
public class ResourceLocatorProps implements ResourceLocator {

    private final String location;

    private final AppLocale appLocale;

    public ResourceLocatorProps(@Value("${application.resource.location}") String location,
                                AppLocale appLocale) {
        this.location = location;
        this.appLocale = appLocale;
    }

    @Override
    public String getLocation() {
        var prop = this.location.replace("@{locale}", appLocale.getLocale().toString());
        return CLASSPATH_URL_PREFIX + prop;
    }
}
