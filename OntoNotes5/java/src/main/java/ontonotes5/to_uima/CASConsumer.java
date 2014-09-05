package ontonotes5.to_uima;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ontonotes5.common.StatsCollector;
import ontonotes5.to_uima.types.Document;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.impl.Serialization;
import org.apache.uima.cas.impl.XCASSerializer;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.XMLSerializer;
import org.xml.sax.SAXException;


public class CASConsumer extends CasConsumer_ImplBase {
  private File outputDir;

  @Override
  public void initialize() throws ResourceInitializationException {
    outputDir = new File((String) getConfigParameterValue("outputDir"));
    if (!outputDir.exists())
      outputDir.mkdirs();
  }

  @Override
  public void processCas(final CAS cas) throws ResourceProcessException {
    final long startTime = System.currentTimeMillis();

    // Obtain the JCas from the CAS.
    JCas jcas;
    try {
      jcas = cas.getJCas();
    }
    catch (CASException e) {
      throw new ResourceProcessException(e);
    }

    // Obtain the singleton document information annotation.
    final FSIterator<Annotation> sources = jcas.getAnnotationIndex(Document.type).iterator();
    if (!sources.hasNext())
      throw new AnalysisEngineProcessException("No DocumentInformation instances found", null);
    final Document docInfo = (Document) sources.next();

    final String[] docIdParts = docInfo.getDocId().split("/");
    final String outputPath = outputDir.getAbsolutePath() + File.separator + docIdParts[docIdParts.length - 1] + ".data";
    final File outputFile = new File(outputPath);
    try {
      writeXMI(cas, outputFile);
    }
    catch (IOException e) {
      throw new ResourceProcessException(e);
    }
    catch (SAXException e) {
      throw new ResourceProcessException(e);
    }

    final long endTime = System.currentTimeMillis();
    StatsCollector.getInstance().addWritingDelta(endTime - startTime);
  }

  private static void writeXMI(final CAS cas, final File outputFile) throws IOException, SAXException {
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(outputFile);

      //final XMLSerializer xmlSerializer = new XMLSerializer(fos, false);
      //final XmiCasSerializer xmiSerialiser = new XmiCasSerializer(cas.getTypeSystem());
      //xmiSerialiser.serialize(cas, xmlSerializer.getContentHandler());

      //final XMLSerializer xmlSerializer = new XMLSerializer(fos, false);
      //final XCASSerializer serialiser = new XCASSerializer(cas.getTypeSystem());
      //serialiser.serialize(cas, xmlSerializer.getContentHandler());

      //Serialization.serializeCAS(cas, fos);
      Serialization.serializeWithCompression(cas, fos);
    }
    finally {
      if (fos != null)
        fos.close();
    }
  }
}
