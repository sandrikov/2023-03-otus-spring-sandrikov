package ru.otus.homework.quiz.services.io;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;


@ConditionalOnBean(ResourceLocator.class)
@Service
public class ResourceDataLoader implements DatasourceLoader {

    private final ResourceLocator resourceLocator;

    public ResourceDataLoader(ResourceLocator resourceLocator) {
        this.resourceLocator = resourceLocator;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        var location = resourceLocator.getLocation();
        final Resource resource = new DefaultResourceLoader().getResource(location);
        return resource.getInputStream();
    }

}
