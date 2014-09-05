package ontonotes5.to_dr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ontonotes5.common.StatsCollector;
import ontonotes5.to_dr.models.Doc;
import ontonotes5.to_uima.types.Document;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.schwa.dr.DocSchema;
import org.schwa.dr.Reader;


public class CollectionReader extends CollectionReader_ImplBase {
  private DocSchema docSchema;
  private int inputFileUpto;
  private List<String> inputFiles;

  @Override
  public void initialize() throws ResourceInitializationException {
    inputFileUpto = 0;
    inputFiles = new ArrayList<String>();
    findInputFiles((String) getConfigParameterValue("inputDir"), inputFiles);

    docSchema = DocSchema.create(Doc.class);
  }

  @Override
  public boolean hasNext() throws IOException, CollectionException {
    return inputFileUpto != inputFiles.size();
  }

  @Override
  public void getNext(final CAS cas) throws IOException, CollectionException {
    final long startTime = System.currentTimeMillis();

    // Obtain the JCas from the CAS.
    JCas jcas;
    try {
      jcas = cas.getJCas();
    }
    catch (CASException e) {
      throw new CollectionException(e);
    }

    // Open the docrep file and read in the singleton document.
    final String inputPath = inputFiles.get(inputFileUpto);
    final Doc doc = loadDoc(inputPath);

    // Construct the fake original document text from the tokens.
    final StringBuilder documentText = new StringBuilder();
    boolean first = true;
    for (ontonotes5.to_dr.models.Token token : doc.tokens) {
      if (first)
        first = false;
      else
        documentText.append(' ');
      documentText.append(token.raw);
    }
    jcas.setDocumentText(documentText.toString());

    // Annotate the document with which document number it is.
    final Document document = new Document(jcas);
    document.setDocId(doc.docId);
    document.setLanguage(doc.lang);
    document.addToIndexes();

    // Increment our progress counter.
    inputFileUpto++;

    final long endTime = System.currentTimeMillis();
    StatsCollector.getInstance().addLoadingDelta(endTime - startTime);
  }

  @Override
  public void close() throws IOException {
    // Do nothing.
  }

  @Override
  public Progress[] getProgress() {
    return new Progress[] { new ProgressImpl(inputFileUpto, inputFiles.size(), Progress.ENTITIES) };
  }

  private Doc loadDoc(final String inputPath) throws IOException, CollectionException {
    final InputStream in = new FileInputStream(inputPath);
    try {
      final Reader<Doc> reader = new Reader<Doc>(in, docSchema);
      if (!reader.hasNext())
        throw new CollectionException("Docrep file '" + inputPath + "' does not contain any documents.", null);
      final Doc doc = reader.next();
      if (reader.hasNext())
        throw new CollectionException("Docrep file '" + inputPath + "' has more than one document.", null);
      in.close();
      return doc;
    }
    finally {
      in.close();
    }
  }

  private static void findInputFiles(final String path, final List<String> files) {
    final File dir = new File(path);
    for (File f : dir.listFiles()) {
      if (f.isDirectory())
        findInputFiles(f.getAbsolutePath(), files);
      else if (f.getName().endsWith(".dr"))
        files.add(f.getAbsolutePath());
    }
  }
}
