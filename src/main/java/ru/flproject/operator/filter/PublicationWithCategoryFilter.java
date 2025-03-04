package ru.flproject.operator.filter;

import org.apache.flink.api.common.functions.FilterFunction;
import ru.flproject.schema.Publication;

import java.util.Optional;

import static ru.flproject.schema.Category.Enum.VACATION;
import static ru.flproject.schema.Category.Enum.WORK;

public class PublicationWithCategoryFilter implements FilterFunction<Publication> {
    private static long serialVersionUID = 1L;

    @Override
    public boolean filter(Publication publication) {
        return Optional.ofNullable(publication.getCategory())
                .map(category -> WORK.equals(category) || VACATION.equals(category))
                .orElse(false);
    }
}
