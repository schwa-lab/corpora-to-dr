package ontonotes5.to_dr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import ontonotes5.common.StatsCollector;
import ontonotes5.db.ODocumentLoader;
import ontonotes5.models.ODocument;
import ontonotes5.to_dr.models.Doc;
import ontonotes5.to_uima.types.Document;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.schwa.dr.DocSchema;
import org.schwa.dr.Writer;


public class CASConsumer extends CasConsumer_ImplBase {
  private File outputDir;
  private ToDocrepConverter toDocrepConverter;
  private long convertingStartTime;
  private long convertingEndTime;
  private long writingStartTime;
  private long writingEndTime;
  private DocSchema docSchema;

  @Override
  public void initialize() throws ResourceInitializationException {
    outputDir = new File((String) getConfigParameterValue("outputDir"));
    if (!outputDir.exists())
      outputDir.mkdirs();

    toDocrepConverter = new ToDocrepConverter();
    docSchema = DocSchema.create(Doc.class);
  }

  @Override
  public void processCas(final CAS cas) throws ResourceProcessException {
    // Obtain the JCas from the CAS.
    JCas jcas;
    try {
      jcas = cas.getJCas();
    }
    catch (CASException e) {
      throw new ResourceProcessException(e);
    }

    // Obtain the singleton document source annotation.
    final FSIterator<Annotation> sources = jcas.getAnnotationIndex(Document.type).iterator();
    if (!sources.hasNext())
      throw new AnalysisEngineProcessException("No SourceDocumentInformation instances found", null);
    final Document docInfo = (Document) sources.next();

    // Load the document and all of its associated objects from the DB.
    ODocument oDoc = null;
    try {
      oDoc = ODocumentLoader.load(docInfo.getDocId());
    }
    catch (SQLException e) {
      throw new AnalysisEngineProcessException(e);
    }

    // Convert the model into its docrep representation.
    toDocrepConverter.reset();
    convertingStartTime = System.currentTimeMillis();
    final Doc doc = toDocrepConverter.convert(oDoc);
    convertingEndTime = System.currentTimeMillis();
    StatsCollector.getInstance().addConvertingDelta(convertingEndTime - convertingStartTime);

    // Write the docrep file out to disk.
    final String[] docIdParts = docInfo.getDocId().split("/");
    final String outputPath = outputDir.getAbsolutePath() + File.separator + docIdParts[docIdParts.length - 1] + ".dr";
    final File outputFile = new File(outputPath);
    FileOutputStream fos = null;
    writingStartTime = System.currentTimeMillis();
    try {
      fos = new FileOutputStream(outputFile);
      final Writer writer = new Writer(fos, docSchema);
      writer.write(doc);
    }
    catch (IOException e) {
      throw new ResourceProcessException(e);
    }
    finally {
      try {
        if (fos != null)
          fos.close();
      }
      catch (IOException e) {
      }
    }
    writingEndTime = System.currentTimeMillis();

    StatsCollector.getInstance().addWritingDelta(writingEndTime - writingStartTime);
  }
}
