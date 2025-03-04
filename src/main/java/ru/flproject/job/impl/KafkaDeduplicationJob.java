//package ru.flproject.job.impl;
//
//import lombok.AllArgsConstructor;
//import org.apache.flink.api.common.typeinfo.TypeInformation;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.util.OutputTag;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.stereotype.Component;
//import ru.flproject.job.FlinkJob;
//import ru.flproject.operator.filter.PublicationWithCategoryFilter;
//import ru.flproject.operator.mapper.Deduplicator;
//import ru.flproject.operator.process.StreamSpliterator;
//import ru.flproject.properties.KafkaDeduplicationJobProperties;
//import ru.flproject.schema.Category;
//import ru.flproject.schema.Publication;
//import ru.flproject.sink.DeduplicatedPublicationSinkProvider;
//import ru.flproject.source.SourceBinder;
//
//import java.util.Map;
//
//@Component
//@AllArgsConstructor
//@ConditionalOnProperty("jobs.kafka-deduplication-job.enabled")
//public class KafkaDeduplicationJob extends FlinkJob {
//    private final KafkaDeduplicationJobProperties deduplicationJobProperties;
//    private final SourceBinder<Publication> sourceBinder;
//    private final DeduplicatedPublicationSinkProvider sinkProvider;
//
//    @Override
//    public void registerJob(StreamExecutionEnvironment env){
//        final var workTag = new OutputTag<>("WORK", TypeInformation.of(Publication.class));
//        final var vacationTag = new OutputTag<>("VACATION", TypeInformation.of(Publication.class));
//
//        final var splittedByWorkAndVacationStream = sourceBinder.bindSource(env)
//                .filter(new PublicationWithCategoryFilter())
//                .uid("filter_publication_by_category_id").name("filter_publication_by_category")
//                .process(new StreamSpliterator<>(
//                        Map.of(
//                                workTag, publication -> Category.Enum.WORK.equals(publication.getCategory()),
//                                vacationTag, publication -> Category.Enum.VACATION.equals(publication.getCategory())
//                        ),
//                        new OutputTag<>("UNKNOWN", TypeInformation.of(Publication.class))
//                )).uid("split_product_by_category_id").name("split_product_by_category");
//
//        final var workStream = splittedByWorkAndVacationStream.getSideOutput(workTag);
//        final var vacationStream = splittedByWorkAndVacationStream.getSideOutput(vacationTag);
//
//        final var deduplicatedWorkStream = workStream.keyBy(publication -> publication.getCategory().value() + publication.getAuthorNickname())
//                .flatMap(new Deduplicator<>(deduplicationJobProperties.getOperators().getDeduplicator().getTtl(
//                        KafkaDeduplicationJobProperties.OperatorProperties.DeduplicatorProperties.DeduplicatorName.WORK)))
//                .uid("deduplicate_work_publication_id").name("deduplicate_work_publication");
//
//        final var sink = sinkProvider.createSink();
//        vacationStream.union(deduplicatedWorkStream)
//                .sinkTo(sink)
//                .uid("sink_publication_id").name("sink_publication");
//    }
//}
