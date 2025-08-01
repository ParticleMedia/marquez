package marquez.service;

import marquez.service.models.BaseEvent;
import marquez.service.models.JobEvent;
import marquez.service.models.LineageEvent.Dataset;
import marquez.service.models.DatasetEvent;
import marquez.service.models.LineageEvent;

public class EventPreprocessService {
    public BaseEvent preprocess(BaseEvent event) {
        if (event instanceof LineageEvent lineageEvent) {
            lineageEvent.getInputs().forEach(this::overrideNamespace);
            lineageEvent.getOutputs().forEach(this::overrideNamespace);
        } else if (event instanceof DatasetEvent datasetEvent) {
            overrideNamespace(datasetEvent.getDataset());
        } else if (event instanceof JobEvent jobEvent) {
            jobEvent.getInputs().forEach(this::overrideNamespace);
            jobEvent.getOutputs().forEach(this::overrideNamespace);
        }
        return event;
    }

    private void overrideNamespace(Dataset dataset) {
        String namespace = dataset.getNamespace();
        if (namespace.startsWith("s3://")) {
            dataset.setNamespace("hive-ads");
        }
    }
}
