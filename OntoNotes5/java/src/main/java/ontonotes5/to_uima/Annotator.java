package ontonotes5.to_uima;

import java.sql.SQLException;

import ontonotes5.common.StatsCollector;
import ontonotes5.db.ODocumentLoader;
import ontonotes5.models.ODocument;
import ontonotes5.to_uima.types.Document;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;


public class Annotator extends JCasAnnotator_ImplBase {
  private long convertingStartTime;
  private long convertingEndTime;
  private ToUIMAConverter toUimaConverter;

  @Override
  public void initialize(final UimaContext context) {
    toUimaConverter = new ToUIMAConverter();
  }

  @Override
  public void process(final JCas jcas) throws AnalysisEngineProcessException {
    // Obtain the singleton document information annotation.
    final FSIterator<Annotation> sources = jcas.getAnnotationIndex(Document.type).iterator();
    if (!sources.hasNext())
      throw new AnalysisEngineProcessException("No Document instances found", null);
    final Document docInfo = (Document) sources.next();

    // Load the document and all of its associated objects from the DB.
    ODocument oDoc = null;
    try {
      oDoc = ODocumentLoader.load(docInfo.getDocId());
    }
    catch (SQLException e) {
      throw new AnalysisEngineProcessException(e);
    }

    // Convert the model into its UIMA representation.
    toUimaConverter.reset();
    convertingStartTime = System.currentTimeMillis();
    toUimaConverter.convert(oDoc, jcas);
    convertingEndTime = System.currentTimeMillis();
    StatsCollector.getInstance().addConvertingDelta(convertingEndTime - convertingStartTime);

    System.out.println(oDoc);
  }
}
