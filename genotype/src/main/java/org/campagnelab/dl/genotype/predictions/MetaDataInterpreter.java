package org.campagnelab.dl.genotype.predictions;

import com.sun.javafx.collections.SortHelper;
import org.campagnelab.dl.framework.domains.prediction.PredictionInterpreter;
import org.campagnelab.dl.genotype.helpers.GenotypeHelper;
import org.campagnelab.dl.genotype.mappers.MetaDataLabelMapper;
import org.campagnelab.dl.genotype.mappers.RecordCountSortHelper;
import org.campagnelab.dl.varanalysis.protobuf.BaseInformationRecords;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * This interpreter extracts meta-data (isVariant, isIndel, etc.) from the meta-data label.
 * Created by fac2003 on 12/22/16.
 */
public class MetaDataInterpreter implements PredictionInterpreter<BaseInformationRecords.BaseInformation,
        MetadataPrediction> {

    private RecordCountSortHelper sortHelper = new RecordCountSortHelper();

    @Override
    public MetadataPrediction interpret(INDArray trueLabels, INDArray output, int predictionIndex) {
        MetadataPrediction p = new MetadataPrediction();
        p.isIndel = trueLabels.getDouble(predictionIndex, MetaDataLabelMapper.IS_INDEL_FEATURE_INDEX) == 1;
        p.isVariant = trueLabels.getDouble(predictionIndex, MetaDataLabelMapper.IS_VARIANT_FEATURE_INDEX) == 1;
        p.referenceGobyIndex = (int) trueLabels.getDouble(predictionIndex, MetaDataLabelMapper.IS_MATCHING_REF_FEATURE_INDEX);
        p.sorted2OriginalCountIndices = new int[]{
                (int) trueLabels.getDouble(predictionIndex, MetaDataLabelMapper.IS_COUNT1_ORIGINAL_INDEX_FEATURE_INDEX),
                (int) trueLabels.getDouble(predictionIndex, MetaDataLabelMapper.IS_COUNT2_ORIGINAL_INDEX_FEATURE_INDEX),
                (int) trueLabels.getDouble(predictionIndex, MetaDataLabelMapper.IS_COUNT3_ORIGINAL_INDEX_FEATURE_INDEX)};
        return p;
    }

    @Override
    public MetadataPrediction interpret(BaseInformationRecords.BaseInformation record, INDArray output) {
        MetadataPrediction p = new MetadataPrediction();
        BaseInformationRecords.SampleInfo sample = record.getSamples(0);
        p.isVariant = sample.getIsVariant();
        final String trueGenotype = record.getTrueGenotype();
        p.isIndel = GenotypeHelper.isIndel(record.getReferenceBase(), trueGenotype);
        p.referenceGobyIndex = MetaDataLabelMapper.calculateReferenceIndex(record);
        BaseInformationRecords.BaseInformation sortedRecord = sortHelper.sort(record);
        // obtain original indices for sorted counts:
        BaseInformationRecords.SampleInfo sortedCountSample = sortedRecord.getSamples(0);
        p.sorted2OriginalCountIndices = new int[]{
                sortedCountSample.getCounts(0).getGobyGenotypeIndex(),
                sortedCountSample.getCounts(1).getGobyGenotypeIndex(),
                sortedCountSample.getCounts(2).getGobyGenotypeIndex(),
        };
        return p;
    }
}
