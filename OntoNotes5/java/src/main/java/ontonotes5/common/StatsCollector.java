package ontonotes5.common;

import org.apache.uima.cas.CAS;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.collection.StatusCallbackListener;


public final class StatsCollector {
  private static final StatsCollector singleton = new StatsCollector();

  private long nDocsProcessed = 0;
  private long msLoading = 0;
  private long msConverting = 0;
  private long msWriting = 0;

  private StatsCollector() { }

  public synchronized void addConvertingDelta(long msDelta) {
    msConverting += msDelta;
  }

  public synchronized void addDocProcessed() {
    nDocsProcessed++;
  }

  public synchronized void addLoadingDelta(long msDelta) {
    msLoading += msDelta;
  }

  public synchronized void addWritingDelta(long msDelta) {
    msWriting += msDelta;
  }

  public synchronized void outputStats() {
    System.out.println("Documents processed: " + nDocsProcessed);
    System.out.println("  Loading OntoNotes: " + formatTime(msLoading));
    System.out.println(" Converting to UIMA: " + formatTime(msConverting));
    System.out.println("   Writing UIMA XMI: " + formatTime(msWriting));
  }

  private static String formatTime(final long ms) {
    final long m = (ms / 1000) / 60;
    final long s = (ms / 1000) % 60;
    return String.format("%3dm%02ds", m, s);
  }

  public static StatsCollector getInstance() {
    return singleton;
  }


  public static class StatsCollectorStatusCallbackListener implements StatusCallbackListener {
    @Override
    public void aborted() { }

    @Override
    public void batchProcessComplete() { }

    @Override
    public void collectionProcessComplete() {
      StatsCollector.getInstance().outputStats();
    }

    @Override
    public void initializationComplete() { }

    @Override
    public void paused() { }

    @Override
    public void resumed() { }

    @Override
    public void entityProcessComplete(CAS cas, EntityProcessStatus status) {
      StatsCollector.getInstance().addDocProcessed();
    }
  }
}
